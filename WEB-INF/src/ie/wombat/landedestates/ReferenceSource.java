package ie.wombat.landedestates;

public class ReferenceSource implements Indexable {

	private Long id;
	private String name;
	private String description;
	
	//private String categoryName;
	private ReferenceCategory category;
	//private int categoryId;
	
	private String contactPerson;
	private String contactTelephone;
	private String contactEmail;
	
	private String url;
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactTelephone() {
		return contactTelephone;
	}
	public void setContactEmail (String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ReferenceCategory getCategory() {
		return category;
	}
	public void setCategory(ReferenceCategory category) {
		this.category = category;
	}
	
	public String getLuceneId () {
		return "R" + this.id;
	}
	
	
}
