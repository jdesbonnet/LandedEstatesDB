package ie.wombat.landedestates;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Optional map tile layers.
 * @author Joe Desbonnet
 *
 */
@Entity
@Table(name = "map_tile_layer")
public class MapTileLayer {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String url;
	
	private String attribution;
	
	private Integer minZoom;
	private Integer maxZoom;
	private Integer maxNativeZoom;
	
	/**
	 * If true tiles confirm to Tile Map Service specification (TMS). See 
	 * https://wiki.osgeo.org/wiki/Tile_Map_Service_Specification
	 */
	@Convert(converter=BooleanToYNConverter.class)
	@Column(length=1)
	private boolean tms;
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public Integer getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(Integer minZoom) {
		this.minZoom = minZoom;
	}

	public Integer getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(Integer maxZoom) {
		this.maxZoom = maxZoom;
	}

	public Integer getMaxNativeZoom() {
		return maxNativeZoom;
	}

	public void setMaxNativeZoom(Integer maxNativeZoom) {
		this.maxNativeZoom = maxNativeZoom;
	}

	public boolean isTms() {
		return tms;
	}

	public void setTms(boolean tms) {
		this.tms = tms;
	}

	
}
