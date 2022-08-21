package com.sundayschool.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sundayschool.dao.StudentDAO;
import com.sundayschool.dao.TeacherDAO;
import com.sundayschool.databaseutility.ConnectionUtility;

public class StudentHandler {

	public StudentHandler() {
		super();
	}
	
	public StudentDAO parseJsonData(String newStudentJsonString) {
		
		JSONObject newStudentJson = new JSONObject(newStudentJsonString);
		StudentDAO studentDao = new StudentDAO();
		if(newStudentJson.has("id_student")) {
			studentDao.setId_student(newStudentJson.getInt("id_student"));
		}
		if(newStudentJson.has("uniqueID")) {
			studentDao.setUniqueID(newStudentJson.getString("uniqueID"));
		}else {
			String uniqueID = createUniqueID(newStudentJson.getString("first_name"), newStudentJson.getString("surname"), newStudentJson.getString("mobile"));
			studentDao.setUniqueID(uniqueID);
		}
		studentDao.setFirst_name(newStudentJson.getString("first_name"));
		studentDao.setSurname(newStudentJson.getString("surname"));
		studentDao.setChurch(newStudentJson.getString("church"));
		studentDao.setAssigned_class(newStudentJson.getString("class"));
		studentDao.setMobile(newStudentJson.getString("mobile"));
		return studentDao;
		
	}

	private String createUniqueID(String first_name, String surname, String mobile) {	
		return first_name+"-"+mobile+"-"+surname;
	}

	public boolean storeNewStudent(String newStudentJson) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		StudentDAO studentDao = new StudentDAO();
		studentDao = parseJsonData(newStudentJson);
		
		try {
				String query = "INSERT INTO student (uniqueID, first_name, surname, church, assigned_class, mobile, enabled) VALUES (?, ?, ?, ?, ?, ?, ?)";
				ps = con.prepareStatement(query);
				ps.setString(1, studentDao.getUniqueID());
				ps.setString(2, studentDao.getFirst_name());
				ps.setString(3, studentDao.getSurname());
				ps.setString(4, studentDao.getChurch());
				ps.setString(5, studentDao.getAssigned_class());
				ps.setString(6, studentDao.getMobile());
				ps.setInt(7, 1);
				status = !ps.execute();
			
		}catch (Exception e) {
			e.printStackTrace();			
			return false;
		}
		finally {
			con.close();
		}
		return status;		
	}

	public String getListOfStudent(String church, String assigned_class) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		String JsonResponse = "";
		ArrayList<StudentDAO> studentDaoList = new ArrayList<StudentDAO>();
		
		try {
				String query = "SELECT * FROM student WHERE church=\""+church+"\" AND enabled=1 AND assigned_class=\""+assigned_class+"\"";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				
				while(rs.next()) {
					StudentDAO studentDao = new StudentDAO();
					studentDao.setUniqueID(rs.getString("uniqueID"));
					studentDao.setFirst_name(rs.getString("first_name"));
					studentDao.setSurname(rs.getString("surname"));
					studentDao.setChurch(rs.getString("church"));
					studentDao.setAssigned_class(rs.getString("assigned_class"));
					studentDao.setMobile(rs.getString("mobile"));
					studentDaoList.add(studentDao);
				}
				
				JsonResponse = createJsonResponse(studentDaoList);
			
		}catch (Exception e) {
			e.printStackTrace();			
			return "";
		}
		finally {
			con.close();
		}
		return JsonResponse;	
	}
	
	public boolean editStudent(String studentJson) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		StudentDAO studentDao = new StudentDAO();
		studentDao = parseJsonData(studentJson);
		
		try {
			String query = "UPDATE student SET first_name=?, surname=?, church=?, assigned_class=?, mobile=?, uniqueID=? WHERE uniqueID=\""+studentDao.getUniqueID()+"\" AND enabled=1";
			ps = con.prepareStatement(query);
			ps.setString(1, studentDao.getFirst_name());
			ps.setString(2, studentDao.getSurname());
			ps.setString(3, studentDao.getChurch());
			ps.setString(4, studentDao.getAssigned_class());
			ps.setString(5, studentDao.getMobile());
			String uniqueID = createUniqueID(studentDao.getFirst_name(), studentDao.getSurname(), studentDao.getMobile());
			ps.setString(6, uniqueID);
			status = !ps.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			con.close();
		}
		return status;
	}

	
	public String createJsonResponse(ArrayList<StudentDAO> studentDaoList) {
			
		JSONObject responseJson = new JSONObject();
		JSONArray studentsArray = new JSONArray();
		
		for(int i=0; i<studentDaoList.size(); i++) {
			JSONObject tempObj = new JSONObject();
			tempObj.put("uniqueID", studentDaoList.get(i).getUniqueID());
			tempObj.put("first_name", studentDaoList.get(i).getFirst_name());
			tempObj.put("surname", studentDaoList.get(i).getSurname());			
			tempObj.put("church", studentDaoList.get(i).getChurch());
			tempObj.put("class", studentDaoList.get(i).getAssigned_class());
			tempObj.put("mobile", studentDaoList.get(i).getMobile());
			studentsArray.put(tempObj);
		}
		
		responseJson.put("students", studentsArray);
		return responseJson.toString();
	}

	public boolean deleteStudent(String uniqueID) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		try {
			String query = "UPDATE student SET enabled=0 WHERE enabled=1 AND uniqueID=\""+uniqueID+"\"";
			ps = con.prepareStatement(query);
			status = !ps.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			con.close();
		}
		return status;
	}


}
