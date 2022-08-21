package com.sundayschool.dao;

public class TeacherDAO {
	
	private int 		id_teacher 			= 0;
	private String 		username 			= "";
	private String 		teacher_name 		= "";
	private String 		church 				= "";
	private String 		mobile 				= "";
	private String 		assigned_class		= "";
	
	public TeacherDAO() {
		super();
	}

	public int getId_teacher() {
		return id_teacher;
	}

	public void setId_teacher(int id_teacher) {
		this.id_teacher = id_teacher;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getChurch() {
		return church;
	}

	public void setChurch(String church) {
		this.church = church;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAssigned_class() {
		return assigned_class;
	}

	public void setAssigned_class(String assigned_class) {
		this.assigned_class = assigned_class;
	}

	
}
