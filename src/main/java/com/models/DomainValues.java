package com.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "domain_values")
public class DomainValues implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DVA_ID")
	private Integer id;

	@Column(name = "DVA_VALUE_TERM")
	private String valueterm;

	@Column(name = "DVA_VALUE_TEXT")
	private String valuetext;

	@Column(name = "DVA_VALUE_CODE")
	private String valuecode;

	@Column(name = "DVA_DISPLAY_ORDER")
	private String displayorder;

	@Column(name = "DVA_RECORD_STATUS_FLAG")
	private String recordStatusFlg;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DVA_RECORD_STATUS_DATE")
	private Date recordStatusDate;

	@Column(nullable = true, name = "DVA_CREATED_USER_ID")
	private Integer createUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DVA_CREATED_DATE")
	private Date createdDate;

	@Column(nullable = true, name = "DVA_LAST_CHANGED_USER_ID")
	private Integer lastChangeUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DVA_LAST_CHANGED_DATE")
	private Date lastChangeDate;

	@Column(nullable = true, name = "DVA_TRANSACTION_GUID")
	private Character transactionGuid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DVA_RECORD_TIMESTAMP")
	private Date recordTimeStamp;

	@Column(name = "DVA_QC_FLAG_DONE")
	private String qcFlagDone;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DVA_DNA_ID")
	private DomainNames domainname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValueterm() {
		return valueterm;
	}

	public void setValueterm(String valueterm) {
		this.valueterm = valueterm;
	}

	public String getValuetext() {
		return valuetext;
	}

	public void setValuetext(String valuetext) {
		this.valuetext = valuetext;
	}

	public String getValuecode() {
		return valuecode;
	}

	public void setValuecode(String valuecode) {
		this.valuecode = valuecode;
	}

	public String getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(String displayorder) {
		this.displayorder = displayorder;
	}

	public String getRecordStatusFlg() {
		return recordStatusFlg;
	}

	public void setRecordStatusFlg(String recordStatusFlg) {
		this.recordStatusFlg = recordStatusFlg;
	}

	public Date getRecordStatusDate() {
		return recordStatusDate;
	}

	public void setRecordStatusDate(Date recordStatusDate) {
		this.recordStatusDate = recordStatusDate;
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

	public DomainNames getDomainname() {
		return domainname;
	}

	public void setDomainname(DomainNames domainname) {
		this.domainname = domainname;
	}

}
