package com.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "CHAT_HISTORY")
public class QBChatMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CHAT_ID")
	private long chatId;
	
	@Column(name = "CHAT_FROM")
	private int chatFrom;
	
	@Column(name = "CHAT_TO")
	private int chatTo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHAT_TIMESTAMP")
	private Date chatTimeStamp;
	
	@Column(name = "MESSAGE")
	private String message;
	
	@Column(name = "READ_STATUS")
	private int read;
	
	@Transient
	private String chatFromName;
	
	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public Date getChatTimeStamp() {
		return chatTimeStamp;
	}

	public void setChatTimeStamp(Date chatTimeStamp) {
		this.chatTimeStamp = chatTimeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getChatFrom() {
		return chatFrom;
	}

	public void setChatFrom(int chatFrom) {
		this.chatFrom = chatFrom;
	}

	public int getChatTo() {
		return chatTo;
	}

	public void setChatTo(int chatTo) {
		this.chatTo = chatTo;
	}

	public String getChatFromName() {
		return chatFromName;
	}

	public void setChatFromName(String chatFromName) {
		this.chatFromName = chatFromName;
	}
	
	
	
	

}
