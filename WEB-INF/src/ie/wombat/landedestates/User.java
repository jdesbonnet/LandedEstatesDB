package ie.wombat.landedestates;


public class User {

	private Long id;
	private String username;
	private String password;
	private boolean writeAccess = false;
	
	public User () {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isWriteAccess() {
		return writeAccess;
	}
	public void setWriteAccess(boolean writeAccess) {
		this.writeAccess = writeAccess;
	}
	public boolean hasWriteAccess() {
		return writeAccess;
	}
	
	
}
