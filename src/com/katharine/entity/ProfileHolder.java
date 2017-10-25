package com.katharine.entity;

public class ProfileHolder {

	private Profession profession;
	
	private String area, name, lname, email;
	private int experience;

	public ProfileHolder(Profession profession, String area, int experience, String name, String lname, String email ) {
		this.area = area;
		this.experience = experience;
		this.name = name;
		this.lname = lname;
		this.email = email;
		this.profession = profession;
		
	}
	
	public Profession getProfession() {
		return profession;
	}
	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	
}
