package com.models;

//import java.sql.Timestamp;
import java.util.Date;

public class UserRegistration {

	private String email;
	private String phone;
	private String firstname;
	private String lastname;
	private String ispatient;
	private String isdoctor;
	private String password;
	private String recordStatusFlg;
	private Date recordStatusDate;
	private Integer hospitalid;
	// private Integer addresslid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getIspatient() {
		return ispatient;
	}

	public void setIspatient(String ispatient) {
		this.ispatient = ispatient;
	}

	public String getIsdoctor() {
		return isdoctor;
	}

	public void setIsdoctor(String isdoctor) {
		this.isdoctor = isdoctor;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRecordStatusFlg() {
		return "Active";
	}

	public void setRecordStatusFlg(String recordStatusFlg) {
		this.recordStatusFlg = "Active";
	}

	public Date getRecordStatusDate() {

		Date recordStatusDate = new Date(new Date().getTime());

		return recordStatusDate;
	}

	public void setRecordStatusDate(Date recordStatusDate) {
		this.recordStatusDate = recordStatusDate;
	}

	public Integer getHospitalid() {
		return hospitalid;
	}

	public void setHospitalid(Integer hospitalid) {
		if(hospitalid == null)
			hospitalid = 1;
		this.hospitalid = hospitalid;
	}

	/*
	 * public Integer getAddresslid() { return addresslid; }
	 * 
	 * public void setAddresslid(Integer addresslid) { this.addresslid =
	 * addresslid; }
	 */

}
