package ie.wombat.landedestates;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OSMGeocode {
	
	private static Logger log = LoggerFactory.getLogger(OSMGeocode.class);
	
	/**
	 * Convert latitude,longitude to address including administrative regions.
	 * 
	 * In the Irish context 'address' properties:
	 * suburb = townland
	 * city_district = DED
	 * state_district = province
	 * 
	 * Ref https://wiki.openstreetmap.org/wiki/Tag%3aboundary=administrative
	 * Level 5 = provence
	 * Level 6 = County
	 * Level 7 = Admin county
	 * Level 8 = Borough & Town council, Dublin Postal Districts
	 * Level 9 = ED
	 * Level 10 = Townloand
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String getReverseGeocodeJson (Double latitude, Double longitude) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	
		String url = "https://nominatim.openstreetmap.org/reverse"
			+ "?format=json"
			+ "&lat=" + latitude 
			+ "&lon=" + longitude
			+ "&zoom=18&addressdetails=1";
		
		log.info("url=" + url);
		
		HttpClient httpClient = HttpClients
			.custom()
			.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
			.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
			.build();
		
		HttpGet get = new HttpGet(url);
		
		HttpResponse response = httpClient.execute(get);
		
		
		String json = EntityUtils.toString(response.getEntity());
		log.info("json="+json);
		return json;
		
		/*
			return Request
					.Get(url)
					.execute()
					.returnContent()
					.asString();
		*/
	}
	
	
	/**
	 * Convert latitude,longitude to address.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String getAddress (Double latitude, Double longitude) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		
		String json = getReverseGeocodeJson(latitude, longitude);
		
		// Parse JSON
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();
		return obj.get("display_name").getAsString();
	}
	
	public static HttpClientBuilder createTrustAllHttpClientBuilder() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		  SSLContextBuilder builder = new SSLContextBuilder();
		  builder.loadTrustMaterial(null, (chain, authType) -> true);           
		  SSLConnectionSocketFactory sslsf = new 
		  SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
		  return HttpClients.custom().setSSLSocketFactory(sslsf);
	}
	
	

}
