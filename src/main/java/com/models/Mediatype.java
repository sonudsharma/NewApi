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

@Entity
@Table(name = "mediatype")
public class Mediatype implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MED_ID")
	private Integer id;

	@Column(name = "MED_TITLE")
	private String title;

	@Column(name = "MED_TYPE")
	private String type;

	@Column(name = "MED_CATEGORY")
	private String category;

	@Column(name = "MED_URL")
	private String url;

	@Column(name = "MED_DESCRIPTION")
	private String description;

	@Column(name = "MED_UPLOADDATE")
	private Date uploadDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MED_DnameId")
	private DomainNames domainname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public DomainNames getDomainname() {
		return domainname;
	}

	public void setDomainname(DomainNames domainname) {
		this.domainname = domainname;
	}

}
