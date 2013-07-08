package ie.wombat.landedestates;

import java.util.HashSet;
import java.util.Set;

public class Barony {

	private Long id;
	private String name;
	private Double latitude;
	private Double longitude;
	private Set<Property> houses = new HashSet<Property>();
	private Integer projectPhase;
	
	public Integer getProjectPhase() {
		return projectPhase;
	}
	public void setProjectPhase(Integer projectPhase) {
		this.projectPhase = projectPhase;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get latitude of centroid in degrees.
	 * @return
	 */
	public Double getLatitude() {
		return latitude;
	}
	
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Get longitude of centroid
	 * @return
	 */
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Set<Property> getHouses() {
		return houses;
	}
	public void setHouses(Set<Property> houses) {
		this.houses = houses;
	}
	
	
}
