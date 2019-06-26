package ie.strix.osmproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caching proxy server for OpenStreetMap tiles. Fetch a tile only once, and limit fetch rate.
 * 
 * @author Joe Desbonnet
 *
 */

@WebServlet(urlPatterns="/tile/std/*")

public class OSMTileCachingProxy extends HttpServlet{
	
	// A delay (in ms) introduced before fetching tile from OSM tile server to limit fetch rate.
	private static int FETCH_DELAY = 500;
	private static String OSM_TILE_SERVER_ROOT = "http://b.tile.openstreetmap.org";
	private static final File cacheRoot = new File("/var/tmp/osmcache");

	private static final Logger log = LoggerFactory.getLogger(OSMTileCachingProxy.class);
	
	private static int requestCounter = 0;
	private static int cacheHitCounter = 0;
	
	private static final long serialVersionUID = 1L;

	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String tile = request.getPathInfo();
		log.info("tile=" + tile);
		
		requestCounter++;

		log.info("requestCounter=" + requestCounter 
				+ " cacheHitCounter=" + cacheHitCounter 
				+ " cacheHitRate=" + ((cacheHitCounter*100)/requestCounter) +"%");
		
		//
		// Check cache for file: serve from cache if found
		//
		File tileFile = new File(cacheRoot,tile);
		log.info("checking cache entry " + tileFile.getPath());
		if (tileFile.exists()) {
			cacheHitCounter++;
			long len = tileFile.length();
			response.setContentType("image/png");
			response.setContentLength((int)tileFile.length());
			FileInputStream in = new FileInputStream(tileFile);
			IOUtils.copy(in,response.getOutputStream());
			in.close();
			return;
		}
		
		//
		// Not found in cache: fetch from OSM tile server
		//
		
		// Pause first to limit fetch rate
		try {
			Thread.sleep(FETCH_DELAY);
		} catch (Exception e) {
		}
		
		HttpClient client = new HttpClient();
		String url = OSM_TILE_SERVER_ROOT + request.getPathInfo();
		GetMethod get = new GetMethod(url);
		int status = client.executeMethod(get);
		log.info("GET " + url + " status=" + status);
		
		log.info("creating cache entry " + tileFile.getPath());
		
		// Make sure directory exists
		if ( ! tileFile.getParentFile().exists()) {
			log.info("mkdir " + tileFile.getParentFile());
			tileFile.getParentFile().mkdirs();
		}
		// Create cache entry
		FileOutputStream out = new FileOutputStream(tileFile);
		IOUtils.copy(get.getResponseBodyAsStream(), out);
		out.close();
		
		// Write back to http response
		response.setContentType("image/png");
		response.setContentLength((int)tileFile.length());
		FileInputStream in = new FileInputStream(tileFile);
		IOUtils.copy(in,response.getOutputStream());
		in.close();
		
	}
}
