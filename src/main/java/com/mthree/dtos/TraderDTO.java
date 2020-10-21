package com.mthree.dtos;

import com.mthree.models.Region;

public class TraderDTO {

    private int id;
    private String username;
    private String password;
    private Region region;
	private String passwordConfirm;
	
	public TraderDTO() {}
    
    public TraderDTO(int id, String username, String password, Region region, String passwordConfirm) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.region = region;
        this.passwordConfirm = passwordConfirm;
	}
	
	
	/** 
	 * @return int
	 */
	public int getId() {
		return id;
	}
	
	
	/** 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/** 
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	
	/** 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	
	/** 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	
	/** 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/** 
	 * @return Region
	 */
	public Region getRegion() {
		return region;
	}

	
	/** 
	 * @param Region
	 */
	public void setRegion(Region region) {
		this.region = region;
	}
	
	/** 
	 * @return String
	 */
	public String getPasswordConfirm() {
        return passwordConfirm;
    }

    
	/** 
	 * @param passwordConfirm
	 */
	public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Trader [id=" + id + ", username=" + username + ", password=" + region + ", region=" +"]";
	}
}
