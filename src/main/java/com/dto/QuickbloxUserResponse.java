package com.dto;

import java.io.Serializable;

public class QuickbloxUserResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String full_name;
	
	private String email;
	
	private String login;
	
	private String phone;
	
	private String website;
	
	private String created_at;
	
	private String updated_at;
	
	private String last_request_at;
	
	private int external_user_id;
	
	private String facebook_id;
	
	private String twitter_id;
	
	private int twitter_digits_id;
	
	private String blob_id;
	
	private String custom_data;
	
	private String user_tags;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getLast_request_at() {
		return last_request_at;
	}

	public void setLast_request_at(String last_request_at) {
		this.last_request_at = last_request_at;
	}

	public int getExternal_user_id() {
		return external_user_id;
	}

	public void setExternal_user_id(int external_user_id) {
		this.external_user_id = external_user_id;
	}

	public String getFacebook_id() {
		return facebook_id;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	public String getTwitter_id() {
		return twitter_id;
	}

	public void setTwitter_id(String twitter_id) {
		this.twitter_id = twitter_id;
	}

	public int getTwitter_digits_id() {
		return twitter_digits_id;
	}

	public void setTwitter_digits_id(int twitter_digits_id) {
		this.twitter_digits_id = twitter_digits_id;
	}

	public String getBlob_id() {
		return blob_id;
	}

	public void setBlob_id(String blob_id) {
		this.blob_id = blob_id;
	}

	public String getCustom_data() {
		return custom_data;
	}

	public void setCustom_data(String custom_data) {
		this.custom_data = custom_data;
	}

	public String getUser_tags() {
		return user_tags;
	}

	public void setUser_tags(String user_tags) {
		this.user_tags = user_tags;
	}
	
	
	
}
