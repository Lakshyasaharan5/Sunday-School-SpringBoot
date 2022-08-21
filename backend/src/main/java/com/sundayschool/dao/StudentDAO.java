package com.sundayschool.dao;

public class StudentDAO {
	
	private int id_student = 0;
	private String uniqueID = "";
	private String first_name = "";
	private String surname = "";
	private String church = "";
	private String assigned_class = "";
	private String mobile = "";
	
	public StudentDAO() {
		
	}

	public int getId_student() {
		return id_student;
	}

	public void setId_student(int id_student) {
		this.id_student = id_student;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getChurch() {
		return church;
	}

	public void setChurch(String church) {
		this.church = church;
	}

	public String getAssigned_class() {
		return assigned_class;
	}

	public void setAssigned_class(String assigned_class) {
		this.assigned_class = assigned_class;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	
}
