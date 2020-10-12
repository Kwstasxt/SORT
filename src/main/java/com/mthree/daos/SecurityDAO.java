package com.mthree.daos;

public interface SecurityDAO {
	
	String findLoggedInUsername();
    void autoLogin(String username, String password);

}
