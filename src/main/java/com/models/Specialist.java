package com.models;

import java.io.Serializable;

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
@Table(name = "specialist")
public class Specialist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DNA_ID")
	private Integer id;

	@Column(name = "SPE_SPECIALIST")
	private String specialist;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "SPE_PER_ID")
	private Persons persons;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecialist() {
		return specialist;
	}

	public void setSpecialist(String specialist) {
		this.specialist = specialist;
	}

	public Persons getPersons() {
		return persons;
	}

	public void setPersons(Persons persons) {
		this.persons = persons;
	}

}
