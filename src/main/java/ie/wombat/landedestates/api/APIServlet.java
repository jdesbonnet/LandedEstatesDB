package ie.wombat.landedestates.api;


import ie.wombat.landedestates.DB;
import ie.wombat.landedestates.ExportExclusionStrategy;
import ie.wombat.landedestates.HibernateUtil;
import ie.wombat.landedestates.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * All API calls go though this servlet. Having just one servlet facilitates a
 * consistent API. Also basic security checks are centralized.
 * 
 * @author Joe Desbonnet
 *
 */

@WebServlet(urlPatterns = "/api/v0/*")

public class APIServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(APIServlet.class);
	
	// Use pretty (nicely formatted) JSON output. Helps with line level diffs.
	private Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.setExclusionStrategies(new ExportExclusionStrategy())
			//.excludeFieldsWithModifiers(modifiers)
			.create();
	

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		long totalRequestTime = -System.currentTimeMillis();

		String[] path = request.getPathInfo().split("/");

		String apiMethodName = path[1];

		log.info("API " + request.getMethod() + " request, api_method=" + apiMethodName + " from "
				+ request.getRemoteAddr() + " at " + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

		// Log HTTP headers, cookies, parameters etc
		logHttpInfo(request);

	

		// Get API handler object
		String rootPackageName = DB.class.getPackage().getName();
		String apiMethodHandlerClassName = rootPackageName + ".api.handlers." + apiMethodName;
		log.debug("Loading API handler " + apiMethodHandlerClassName);
		APIMethodHandler apiMethod = null;

		try {
			apiMethod = (APIMethodHandler) Class.forName(apiMethodHandlerClassName).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.error("API Handler " + apiMethodHandlerClassName + " not found.");
			reportError(response, apiMethodName, -100, "error loading service handler for " + apiMethodName);
			return;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			log.error("API Handler " + apiMethodHandlerClassName + " error loading: " + e.getMessage());
			reportError(response, apiMethodName, -100, "error loading service handler for " + apiMethodName);
			return;
		}

		if (apiMethod == null) {
			log.error("API Handler " + apiMethodHandlerClassName + " error loading or not found");
			reportError(response, apiMethodName, -101, "service handler not for " + apiMethodName + " not found ");
			return;
		}

		// Check if requested method (GET, POST, PUT, DELETE etc) is allowed by handler
		String httpMethod = request.getMethod();
		if ("GET".equals(httpMethod) && (!apiMethod.getClass().isAnnotationPresent(GetAllowed.class))) {
			reportError(response, apiMethodName, -102, "GET not supported on service " + apiMethodName);
			return;
		}
		if ("POST".equals(httpMethod) && !apiMethod.getClass().isAnnotationPresent(PostAllowed.class)) {
			reportError(response, apiMethodName, -102, "POST not supported on service " + apiMethodName);
			return;
		}
		if ("PUT".equals(httpMethod) && !apiMethod.getClass().isAnnotationPresent(PutAllowed.class)) {
			reportError(response, apiMethodName, -102, "PUT not supported on service " + apiMethodName);
			return;
		}
		if ("DELETE".equals(httpMethod) && !apiMethod.getClass().isAnnotationPresent(DeleteAllowed.class)) {
			reportError(response, apiMethodName, -102, "DELETE not supported on service " + apiMethodName);
			return;
		}

		// Check for @LocalhostOnly annotation: request must be from 127.0.0.1 /
		// 0:0:0:0:0:0:0:1 (localhost) if so
		if (apiMethod.getClass().isAnnotationPresent(LocalhostOnly.class)) {
			// TODO: need better test for a localhost address: suspect there are other
			// variants of IPv6 formatting
			String remoteAddr = request.getRemoteAddr();
			if (!(remoteAddr.equals("127.0.0.1") || remoteAddr.equals("0:0:0:0:0:0:0:1"))) {
				reportError(response, apiMethodName, -103,
						"localhost access only on " + apiMethodName + " remoteAddr=" + request.getRemoteAddr());
				return;
			}
		}

		EntityManager em = HibernateUtil.getEntityManager();
		em.getTransaction().begin();

		// Check if user has appropriate role
		for (Annotation a : apiMethod.getClass().getAnnotations()) {
			log.info("handler annotation: " + a);
		}

		Long userId = (Long) request.getSession().getAttribute("user_id");

		// API calls require valid session unless has role RoleNotLoggedIn or
		// LocalhostAllowed
		User user;
		if (userId == null) {

			if (apiMethod.getClass().isAnnotationPresent(RoleNotLoggedIn.class)) {
				user = null;
			} else {
				if (apiMethod.getClass().isAnnotationPresent(LocalhostAllowed.class)) {
					log.info("requests from localhost do not require authenticated session host="
							+ request.getRemoteHost());
					// TODO: need a better way to detect localhost
					if (request.getRemoteAddr().equals("127.0.0.1")) {
						log.info("request ok");
						user = null;
					} else {
						reportError(response, apiMethodName, -200, "Not logged in.");
						return;
					}
				} else {
					reportError(response, apiMethodName, -200, "Not logged in.");
					return;
				}
			}

		} else {
			user = em.find(User.class, userId);
		}

		// Check for API methods only available to SYSADMIN users.
		if (apiMethod.getClass().isAnnotationPresent(RoleSysAdmin.class)) {
			if (!user.hasRole(Role.SYSADMIN)) {
				reportError(response, apiMethodName, -201, Role.SYSADMIN + " is required");
				return;
			}
		}
		
		// Log user 
		if (user == null) {
			log.info("user=anonymous");
		} else {
			log.info("user=" + user.getEmail() + " User#" + user.getId() + " " + user.getUsername());
		}

		// If POST or PUT and Content-Type application/json parse JSON here
		JSONObject requestJson = null;
		log.info("request contentType: " + request.getContentType());
		if ("application/json".equals(request.getContentType())) {
			// Parse JSON and pass to API method.
			String postBody = IOUtils.toString(request.getInputStream());
			log.info("request JSON: " + postBody);
			requestJson = JSONObject.fromObject(postBody);
		}

		//
		// Execute handler code
		//
		log.info("executing handler");
		long timeToExecute = -System.currentTimeMillis();
		APIMethodResponse opResponse = new APIMethodResponse();
		try {
			// TODO: we now have 5 parameters, one of which is optional: time to move to one
			// parameter object?
			apiMethod.execute(opResponse, em, user, request, requestJson);
		} catch (Throwable e) {
			e.printStackTrace();
			reportError(response, apiMethodName, -300, e.toString());
			return;
		}
		timeToExecute += System.currentTimeMillis();

		log.info("timeToExecuteHandler: handler=" + apiMethodName + " time=" + timeToExecute + "ms");

		// Log POST API call
		if ("POST".equals(request.getMethod())) {
			//EventLogEntry event = EventLog.createLogRecord(user, request, "api." + apiMethodName);
			//em.persist(event);
		}

		//
		// Send API method response
		//

		long timeToSend = -System.currentTimeMillis();

		// TODO: auto detect binary response by looking at result object. If byte[] or
		// InputStream
		// go binary, anything else attempt to serialize to JSON.

		if (opResponse.isBinary()) {
			// Experimental alternative binary response
			sendBinaryResponse(response, opResponse);
		} else {
			// Regular JSON or JSONP response
			sendJsonResponse(apiMethod, request, response, opResponse);
		}

		// Simulate network delay
		/*
		 * try { Thread.sleep(1000 + (long)(Math.random()*1000)); } catch (Exception e)
		 * { }
		 */

		timeToSend += System.currentTimeMillis();
		totalRequestTime += System.currentTimeMillis();

		log.info("timeToSendResponse: handler=" + apiMethodName + " time=" + timeToSend + "ms");
		log.info("totalRequestTime: handler=" + apiMethodName + " time=" + totalRequestTime + "ms");

		// Experimental feature to have the API add a redirect response header
		// to facilitate POST from form to API.
		String redirectOnSuccess = request.getParameter("redirect_on_success");
		if (redirectOnSuccess != null) {
			log.info("redirecting to " + redirectOnSuccess);
			response.sendRedirect(redirectOnSuccess);
		}

	}

	/**
	 * Report an API error message. Always return immediately after calling this.
	 * 
	 * @param w
	 * @param opName
	 * @param errorCode
	 * @param errorText
	 * @throws IOException
	 */
	private static void reportError(HttpServletResponse response, String opName, int errorCode, String errorText)
			throws IOException {
		log.error("error executing " + opName + ": code=" + errorCode + " text=" + errorText);
		Writer w = response.getWriter();
		w.write("{\"status\":" + errorCode);
		w.write(",\"service\":" + JSONUtils.quote(opName));
		w.write(",\"errorText\":" + JSONUtils.quote(errorText));
		w.write("}");
	}

	/**
	 * Log as much possible information about the HTTP request for debugging. Will
	 * try to remove sensitive information (eg passwords) from log.
	 * 
	 * @param request
	 */
	private static void logHttpInfo(HttpServletRequest request) {

		// Log HTTP headers
		Enumeration<String> en0 = request.getHeaderNames();
		while (en0.hasMoreElements()) {
			String name = en0.nextElement();
			log.info("header[" + name + "]=" + request.getHeader(name));
		}

		// Log HTTP cookies
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				log.info("cookie[" + cookie.getName() + "]=" + " " + cookie.getDomain() + " " + cookie.getPath() + " "
						+ cookie.getMaxAge() + " " + cookie.getValue());
			}
		}

		// Log HTTP parameters
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = en.nextElement();
			// Redact passwords from logs.
			String value = name.equals("password") ? "******" : request.getParameter(name);
			log.info("param[" + name + "]=" + value);
		}
	}

	private void sendBinaryResponse(HttpServletResponse response, APIMethodResponse opResponse) throws IOException {

		log.debug("sending binary response to API request");

		response.setContentType(opResponse.getMimeType());

		if (opResponse.getContentDisposition() != null) {
			response.addHeader("Content-Disposition", opResponse.getContentDisposition());
		}

		if ("application/json".equals(opResponse.getMimeType())) {
			Writer w = response.getWriter();
			w.write((String) opResponse.getResult());
			w.flush();
			return;
		}

		OutputStream out = response.getOutputStream();
		if (opResponse.getResult() instanceof byte[]) {
			byte[] data = (byte[]) opResponse.getResult();
			log.debug("binary length=" + data.length + " bytes");
			out.write(data);
		} else if (opResponse.getResult() instanceof InputStream) {
			InputStream in = (InputStream) opResponse.getResult();
			IOUtils.copy(in, out);
		}
		out.flush();
	}

	private void sendJsonResponse(APIMethodHandler apiMethod, HttpServletRequest request, HttpServletResponse response,
			APIMethodResponse opResponse) throws IOException {

		log.debug("sending JSON response to API request");

		// All responses MIME type application/json, UTF-8 encoded.
		response.setContentType("application/json; charset=UTF-8");
		Writer w = response.getWriter();

		// Experimental JSONP support, see https://en.wikipedia.org/wiki/JSONP
		boolean jsonpResponse = (apiMethod.getClass().isAnnotationPresent(JSONPResponse.class));

		if (jsonpResponse) {
			w.write("feJsonpResponse(");
		}

		w.write("{\"status\":" + opResponse.getStatus());

		// Server timestamp
		w.write(",\"ts\":" + System.currentTimeMillis());

		// When was store last modified
		// w.write (",\"lm\":" + PageStore.getInstance().getLastModifiedTime());

		if (opResponse.getErrorText() != null) {
			w.write(",\"errorText\":" + JSONUtils.quote(opResponse.getErrorText()));
		}

		// experimental t=lastRequestTime (used to determine what alerts etc to send)
		long lastReq = 0;
		if (request.getParameter("t") != null) {
			lastReq = Long.parseLong(request.getParameter("t"));
		}

		// Associated notes/alert. TODO use Jackson or Gson to serialize.
		//List<Alert> alerts = opResponse.getAlerts();
		w.write(",\"alerts\": [");
		/*
		for (int i = 0; i < alerts.size(); i++) {
			Alert alert = alerts.get(i);
			w.write("{\"type\": " + JSONUtils.quote(alert.getType().toString()) + ", \"text\":"
					+ JSONUtils.quote(alert.getText()) + "},");
		}
		*/
		w.write("{}]");

		// Use Jackson to serialize API result object to JSON.
		w.write(",\"result\":");

		if (opResponse.isJsonString()) {
			w.write((String) opResponse.getResult());
		} else {

			//Gson gson = new Gson();
			
			//ObjectMapper mapper = new ObjectMapper();

			// Field serialization exclusion options.
			// See FilterMixIn class for documentation.
			//ObjectWriter writer = mapper.writer();
			if (opResponse.getExcludedFieldNames() != null) {

				/*
				mapper.addMixIn(Object.class, FilterMixIn.class);

				String[] exclFields = opResponse.getExcludedFieldNames();

				SimpleBeanPropertyFilter filter;
				filter = SimpleBeanPropertyFilter.serializeAllExcept(exclFields);

				FilterProvider filters = new SimpleFilterProvider().addFilter("apiFilter", filter);
				writer = mapper.writer(filters);
				*/
			}

			// NB: this seems to fail: not writing last curly bracket.
			// mapper.writeValue(w, opResponse.getResult());
			String resultJson = gson.toJson(opResponse.getResult());
			w.write(resultJson);

			log.info("resultLength=" + resultJson.length());

			if (resultJson.length() < 250) {
				log.debug("json=" + resultJson);
			} else {
				log.debug("json=" + resultJson.substring(0, 249) + "...");
			}
		}

		// End of API response
		w.write("}");

		if (jsonpResponse) {
			w.write(")");
		}

	}
}
