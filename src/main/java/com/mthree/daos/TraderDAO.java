package com.mthree.daos;

public interface TraderDAO {
	
	String hashPassword(String password);
	boolean checkPasswordMatches(String candidate, String hashedPassword);

}
