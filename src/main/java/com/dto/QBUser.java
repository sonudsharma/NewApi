package com.dto;

import java.io.Serializable;

public class QBUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String login;
	
	private String password;
	
	public QBUser() {
		
	}
	
	

	public QBUser(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}



	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	@Override
	public String toString() {
		return "user[login]=" + login + "&user[password]=" + password;
	}
	
	
	
}
