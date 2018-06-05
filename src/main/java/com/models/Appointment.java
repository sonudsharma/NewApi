package com.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APP_ID")
	private Integer id;

	@Column(name = "APP_DATE")
	private Date date;

	@Column(name = "APP_PURPOSE")
	private String purpose;

	@Column(name = "APP_HOSPITAL")
	private String hospitalName;

	@Column(name = "APP_DOCTORNAME")
	private String doctorName;

	@Column(name = "APP_PATIENTNAME")
	private String patientName;

	@Column(name = "APP_TIME")
	private Time time;

	@ManyToOne
	@JoinColumn(name = "APP_USR_ID", nullable = false)
	private Users user;

	public static final String MORNING_TIME_STRING = "Morning";
	public static final String AFTERNOON_TIME_STRING = "Afternoon";
	public static final String EVENING_TIME_STRING = "Evening";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date = new Date();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public static String getMorningTimeString() {
		return MORNING_TIME_STRING;
	}

	public static String getAfternoonTimeString() {
		return AFTERNOON_TIME_STRING;
	}

	public static String getEveningTimeString() {
		return EVENING_TIME_STRING;
	}

}