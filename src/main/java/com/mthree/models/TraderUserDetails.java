package com.mthree.models;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TraderUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private transient Trader user;
	
	public TraderUserDetails(Trader user) {
		this.user = user;
	}

	
	/** 
	 * @return Collection<? extends GrantedAuthority>
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return Arrays.asList(authority);
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String getUsername() {
		return user.getUsername();
	}

	
	/** 
	 * @return boolean
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	
	/** 
	 * @return boolean
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	
	/** 
	 * @return boolean
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
	/** 
	 * @return boolean
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
