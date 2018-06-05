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

@Entity
@Table(name = "domain_names")
public class DomainNames implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DNA_ID")
	private Integer id;

	@Column(name = "DNA_NAME")
	private String name;

	@Column(name = "DNA_TEXT")
	private String text;

	@Column(name = "DNA_RECORD_STATUS_FLAG")
	private String recordStatusFlag;

	@Column(name = "DNA_RECORD_STATUS_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordStatusDate;

	@Column(name = "DNA_CREATED_USER_ID")
	private Integer createdUserId;

	@Column(name = "DNA_CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "DNA_LAST_CHANGED_USER_ID")
	private Integer lastChangedUserId;

	@Column(name = "DNA_LAST_CHANGED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChangedDate;

	@Column(name = "DNA_TRANSACTION_GUID")
	private Character transactionGuid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DNA_RECORD_TIMESTAMP")
	private Date recordTimeStamp;

	@Column(name = "DNA_QC_FLAG_DONE")
	private String qcFlagDone;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecordStatusFlag() {
		return recordStatusFlag;
	}

	public void setRecordStatusFlag(String recordStatusFlag) {
		this.recordStatusFlag = recordStatusFlag;
	}

	public Date getRecordStatusDate() {
		return recordStatusDate;
	}

	public void setRecordStatusDate(Date recordStatusDate) {
		this.recordStatusDate = recordStatusDate;
	}

	public Integer getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Integer createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getLastChangedUserId() {
		return lastChangedUserId;
	}

	public void setLastChangedUserId(Integer lastChangedUserId) {
		this.lastChangedUserId = lastChangedUserId;
	}

	public Date getLastChangedDate() {
		return lastChangedDate;
	}

	public void setLastChangedDate(Date lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public Character getTransactionGuid() {
		return transactionGuid;
	}

	public void setTransactionGuid(Character transactionGuid) {
		this.transactionGuid = transactionGuid;
	}

	public Date getRecordTimeStamp() {
		return recordTimeStamp;
	}

	public void setRecordTimeStamp(Date recordTimeStamp) {
		this.recordTimeStamp = recordTimeStamp;
	}

	public String getQcFlagDone() {
		return qcFlagDone;
	}

	public void setQcFlagDone(String qcFlagDone) {
		this.qcFlagDone = qcFlagDone;
	}

}
