package com.katharine.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity  //(name = "profession")
@Table(name = "profession")
public class Profession {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "professionId")
	private int professionId;
	
	@Column(name = "area")
	private String area;
	
	@Column(name = "experience")
	private int experiance;
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
	@JoinColumn(name="owner")
	private Person person;

	
	public Profession() {}
	
	public Profession(int id, String area, int experiance, Person person) {
		super();
		this.professionId = id;
		this.area = area;
		this.experiance = experiance;
		this.person = person;
	}
	
	public Profession(String area, int experiance, Person person) {
		super();
		this.area = area;
		this.experiance = experiance;
		this.person = person;
	}
		
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public int getProfessionId() {
		return professionId;
	}
	public void setProfessionId(int professionId) {
		this.professionId = professionId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getExperiance() {
		return experiance;
	}
	public void setExperiance(int experiance) {
		this.experiance = experiance;
	}
	
	

}
