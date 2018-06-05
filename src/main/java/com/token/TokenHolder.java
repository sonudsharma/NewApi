package com.token;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class TokenHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, String> tokens = new HashMap<Integer, String>();

	public Map<Integer, String> getTokens() {
		return tokens;
	}

	public void setTokens(Map<Integer, String> tokens) {
		this.tokens = tokens;
	}
	
	public void addToken(Integer externalUserId, String token) {
		tokens.put(externalUserId, token);
	}
	
	public String generateNewToken(Integer externalUserId) {
		//Implementation remaining
		return "";
	}
	

}
