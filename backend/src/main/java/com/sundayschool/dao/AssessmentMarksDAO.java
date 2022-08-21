package com.sundayschool.dao;

public class AssessmentMarksDAO {
	
	private int id_assessmentMarks = 0;
	private String uniqueID = "";
	private String church = "";
	private String assigned_class = "";
	private String date = "";
	private String first_name = "";
	private String surname = "";
	private int attendance = 0;
	private int songs_4 = 0;
	private int worship_message = 0;
	private int table_message = 0;
	private int behaviour = 0;
	private int memory_verses = 0;
	private int total = 0;
	private String remarks = "";
	
	public AssessmentMarksDAO() {
		super();
	}

	public int getId_assessmentMarks() {
		return id_assessmentMarks;
	}

	public void setId_assessmentMarks(int id_assessmentMarks) {
		this.id_assessmentMarks = id_assessmentMarks;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public int getSongs_4() {
		return songs_4;
	}

	public void setSongs_4(int songs_4) {
		this.songs_4 = songs_4;
	}

	public int getWorship_message() {
		return worship_message;
	}

	public void setWorship_message(int worship_message) {
		this.worship_message = worship_message;
	}

	public int getTable_message() {
		return table_message;
	}

	public void setTable_message(int table_message) {
		this.table_message = table_message;
	}

	public int getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(int behaviour) {
		this.behaviour = behaviour;
	}

	public int getMemory_verses() {
		return memory_verses;
	}

	public void setMemory_verses(int memory_verses) {
		this.memory_verses = memory_verses;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

		
}
