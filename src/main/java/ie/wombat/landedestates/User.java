package ie.wombat.landedestates;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ie.wombat.landedestates.api.Role;

@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//@Column (name="name")
	//private String name;
	

	/**
	 * Username and email are (currently) identical
	 */
	@Column(name="username")
	private String username;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="write_access")
	private boolean writeAccess = false;
	
	/**
	 * Admin user can create/edit users
	 */
	@Column(name="admin", nullable=false, columnDefinition = "TINYINT(1)")
	private boolean admin = false;
	
	/**
	 * Super admin user has access to backups etc
	 */
	//@Column(name="super_admin")
	//private boolean superAdmin = false;
	
	@Column(name="last_login")
	private Date lastLogin;
	
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public boolean hasRole (Role role) {
		return true;
	}
	
}
