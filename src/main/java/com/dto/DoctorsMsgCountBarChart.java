package com.dto;

import java.io.Serializable;

public class DoctorsMsgCountBarChart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object name;
	
	private Object msgSent;
	
	private Object msgReceived;
	
	

	public DoctorsMsgCountBarChart() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DoctorsMsgCountBarChart(Object name, Object msgSent, Object msgReceived) {
		super();
		this.name = name;
		this.msgSent = msgSent;
		this.msgReceived = msgReceived;
	}



	public Object getName() {
		return name;
	}



	public void setName(Object name) {
		this.name = name;
	}



	public Object getMsgSent() {
		return msgSent;
	}



	public void setMsgSent(Object msgSent) {
		this.msgSent = msgSent;
	}



	public Object getMsgReceived() {
		return msgReceived;
	}



	public void setMsgReceived(Object msgReceived) {
		this.msgReceived = msgReceived;
	}

	
	

}
