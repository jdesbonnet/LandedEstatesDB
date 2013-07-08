package ie.wombat.landedestates;
import java.util.Date;


/**
 * Experiment to capture a 'heat map' of areas of interest. Each pan/zoom
 * operation will record the viewport area
 * @author joe
 *
 */
public class HeatMapRecord {

	private Long id;
	private Date timestamp;
	private String ipNumber;
	private String sessionId;
	private String agent;
	private Double swLatitude;
	private Double swLongitude;
	private Double neLatitude;
	private Double neLongitude;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getIpNumber() {
		return ipNumber;
	}
	public void setIpNumber(String ipNumber) {
		this.ipNumber = ipNumber;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Double getSwLatitude() {
		return swLatitude;
	}
	public void setSwLatitude(Double swLatitude) {
		this.swLatitude = swLatitude;
	}
	public Double getSwLongitude() {
		return swLongitude;
	}
	public void setSwLongitude(Double swLongitude) {
		this.swLongitude = swLongitude;
	}
	public Double getNeLatitude() {
		return neLatitude;
	}
	public void setNeLatitude(Double neLatitude) {
		this.neLatitude = neLatitude;
	}
	public Double getNeLongitude() {
		return neLongitude;
	}
	public void setNeLongitude(Double neLongitude) {
		this.neLongitude = neLongitude;
	}
	
	
}
