package com.mthree.daos;

public interface TraderDAO {
	
	void login();
	void register();
	String hashPassword(String password);
	boolean checkPasswordMatches(String candidate, String hashedPassword);

}
