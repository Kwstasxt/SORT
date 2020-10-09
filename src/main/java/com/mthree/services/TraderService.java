package com.mthree.services;

import org.springframework.stereotype.Service;

import com.mthree.daos.TraderDAO;

@Service
public class TraderService implements TraderDAO {
	
	@Override
	public void login() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String hashPassword(String password) {
//		// hash password for the first time
//		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
//	
//		// gensalt's log_rounds parameter determines the complexity the work factor is 2**log_rounds, and the default is 10
//		hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
//		
//		return hashed;
		
		return "";
	}

	@Override
	public boolean checkPasswordMatches(String candidate, String hashedPassword) {
		// check that an unencrypted password matches one that has previously been hashed
//		return BCrypt.checkpw(candidate, hashedPassword);
		return false;
	}
}
