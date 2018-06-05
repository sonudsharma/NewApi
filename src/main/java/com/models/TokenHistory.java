package com.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tokenhistory")
public class TokenHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOH_TOKEN_ID")
	private Integer id;

	@Column(name = "TOH_TOKEN")
	private String token;

	@Column(name = "TOH_TOKEN_STATUS")
	private String tokenStatus;

	@Column(name = "TOH_EXPIRE_DATE")
	private Date expireDate;

	@Column(nullable = true, name = "TOH_CREATED_USER_ID")
	private Integer createUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TOH_CREATED_DATE")
	private Date createdDate = new Date();

	@Column(nullable = true, name = "TOH_LAST_CHANGED_USER_ID")
	private Integer lastChangeUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TOH_LAST_CHANGED_DATE")
	private Date lastChangeDate;

	@ManyToOne
	@JoinColumn(name = "TOH_USR_ID", nullable = false)
	private Users user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(String tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getLastChangeUserId() {
		return lastChangeUserId;
	}

	public void setLastChangeUserId(Integer lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public Users getUser() {
		return user;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
