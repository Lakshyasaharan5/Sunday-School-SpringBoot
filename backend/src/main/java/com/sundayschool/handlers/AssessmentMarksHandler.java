package com.sundayschool.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.*;

import com.sundayschool.databaseutility.*;
import com.sundayschool.dao.AssessmentMarksDAO;
import com.sundayschool.dao.StudentDAO;
import com.sundayschool.dao.TeacherDAO;


public class AssessmentMarksHandler {

	public AssessmentMarksHandler() {
		
	}
	
	public ArrayList<AssessmentMarksDAO> parseJsonData(String assessmentMarksDataJsonString) throws JSONException{
		JSONObject assessmentMarksJson = new JSONObject(assessmentMarksDataJsonString);
		JSONArray studentsMarksArray = assessmentMarksJson.getJSONArray("studentsMarks");
		ArrayList<AssessmentMarksDAO> assessmentMarksDaoList = new ArrayList<AssessmentMarksDAO>();
		
		
		for(int i=0; i<studentsMarksArray.length(); i++) {
			JSONObject eachStudentData = studentsMarksArray.getJSONObject(i);
			AssessmentMarksDAO assessmentMarksDao = new AssessmentMarksDAO();
			if(eachStudentData.has("id_assessmentMarks")) {
				assessmentMarksDao.setId_assessmentMarks(eachStudentData.getInt("id_assessmentMarks"));
			}
			assessmentMarksDao.setUniqueID(eachStudentData.getString("uniqueID"));
			assessmentMarksDao.setChurch(eachStudentData.getString("church"));
			assessmentMarksDao.setAssigned_class(eachStudentData.getString("class"));
			assessmentMarksDao.setDate(eachStudentData.getString("date"));
			assessmentMarksDao.setFirst_name(eachStudentData.getString("first_name"));
			assessmentMarksDao.setSurname(eachStudentData.getString("surname"));
			assessmentMarksDao.setAttendance(eachStudentData.getInt("attendance"));
			assessmentMarksDao.setSongs_4(eachStudentData.getInt("songs_4"));
			assessmentMarksDao.setWorship_message(eachStudentData.getInt("worship_message"));
			assessmentMarksDao.setTable_message(eachStudentData.getInt("table_message"));
			assessmentMarksDao.setBehaviour(eachStudentData.getInt("behaviour"));
			assessmentMarksDao.setMemory_verses(eachStudentData.getInt("memory_verses"));
			assessmentMarksDao.setTotal(eachStudentData.getInt("total"));
			assessmentMarksDao.setRemarks(eachStudentData.getString("remarks"));
			assessmentMarksDaoList.add(assessmentMarksDao);
		}
				
		return assessmentMarksDaoList;
	}
	
	public boolean storeNewAssessmentMarksData(String studentsMarksJson) throws Exception {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		
		ArrayList<AssessmentMarksDAO> assessmentMarksDaoList = new ArrayList<AssessmentMarksDAO>();
		assessmentMarksDaoList = parseJsonData(studentsMarksJson);
		
		try {
				
				for(int i=0; i<assessmentMarksDaoList.size(); i++) {
					status = false;
					AssessmentMarksDAO assessmentMarksDao = new AssessmentMarksDAO();
					assessmentMarksDao = assessmentMarksDaoList.get(i);
					String query = "INSERT INTO assessmentMarks (uniqueID, church, assigned_class, date, first_name, surname, attendance, songs_4, worship_message, table_message, behaviour, memory_verses, total, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					ps = con.prepareStatement(query);
					ps.setString(1, assessmentMarksDao.getUniqueID());
					ps.setString(2, assessmentMarksDao.getChurch());
					ps.setString(3, assessmentMarksDao.getAssigned_class());
					ps.setString(4, assessmentMarksDao.getDate());
					ps.setString(5, assessmentMarksDao.getFirst_name());
					ps.setString(6, assessmentMarksDao.getSurname());
					ps.setInt(7, assessmentMarksDao.getAttendance());
					ps.setInt(8, assessmentMarksDao.getSongs_4());
					ps.setInt(9, assessmentMarksDao.getWorship_message());
					ps.setInt(10, assessmentMarksDao.getTable_message());
					ps.setInt(11, assessmentMarksDao.getBehaviour());
					ps.setInt(12, assessmentMarksDao.getMemory_verses());
					ps.setInt(13, assessmentMarksDao.getTotal());
					ps.setString(14, assessmentMarksDao.getRemarks());
					status = !ps.execute();
					if(!status) {
						return false;
					}
				}						
			
		}catch (Exception e) {
			e.printStackTrace();			
			return false;
		}
		finally {
			con.close();
		}
		return status;
		
	}

	public String getStudentsListByTeacherUsername(String teacherUsername) throws SQLException {
		
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		PreparedStatement ps;
		ArrayList<StudentDAO> studentDaoList = new ArrayList<StudentDAO>();
		String JsonResponse = "";
		String church = getChurchFromTeacherUsername(teacherUsername);
		String assigned_class = getClassFromTeacherUsername(teacherUsername);
		String query = "";
		
		try {
				if(!church.isEmpty() && !assigned_class.isEmpty()) {
					query = "SELECT * FROM student WHERE church=\""+church+"\" AND assigned_class=\""+assigned_class+"\" AND enabled=1";
				}else {
					return "";
				}				
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
			
		}catch (Exception e) {
			e.printStackTrace();			
			return null;
		}
		try {
			JsonResponse = createJsonResponse(studentDaoList);
			
		}catch (Exception e) {
			e.printStackTrace();			
			return null;
		}
		finally {
			con.close();
		}
		return JsonResponse;
	}
	
	private String getClassFromTeacherUsername(String teacherUsername) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		PreparedStatement ps;
		String assigned_class = "";
		
		try {
				String query = "SELECT assigned_class FROM teacher WHERE username=\""+teacherUsername+"\" AND enabled=1";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					assigned_class = rs.getString("assigned_class");
				}
				
			
			
		}catch (Exception e) {
			e.printStackTrace();			
			return "";
		}finally {
			con.close();
		}
		
		return assigned_class;
	}

	private String getChurchFromTeacherUsername(String teacherUsername) throws SQLException {		
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		PreparedStatement ps;
		String church = "";
		
		try {
				String query = "SELECT church FROM teacher WHERE username=\""+teacherUsername+"\"";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					church = rs.getString("church");
				}
				
			
			
		}catch (Exception e) {
			e.printStackTrace();			
			return "";
		}finally {
			con.close();
		}
		
		return church;
	}

	private String createJsonResponse(ArrayList<StudentDAO> studentDaoList) {
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
		
		responseJson.put("studentsMarks", studentsArray);
		return responseJson.toString();
	}

	public String getChurchClassByTeacherUsernameFromUsers(String teacherUsername) throws SQLException {		
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		PreparedStatement ps;
		String churchClass = "";
		
		try {
				String query = "SELECT (church_class) FROM users WHERE username="+teacherUsername+";";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					churchClass = rs.getString("church_class");
				}
			
		}catch (Exception e) {
			e.printStackTrace();			
			return null;
		}
		finally {
			con.close();
		}
		
		return churchClass;
	}

	public String getStudentsAssessmentMarks(String teacherUsername, String date) throws SQLException {
		
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		PreparedStatement ps;
		ArrayList<AssessmentMarksDAO> assessmentMarksDaoList = new ArrayList<AssessmentMarksDAO>();
		String JsonResponse = "";
		String church = getChurchFromTeacherUsername(teacherUsername);
		String assigned_class = getClassFromTeacherUsername(teacherUsername);
		String query = "";
		
		try {
				if(!church.isEmpty() && !assigned_class.isEmpty()) {
					query = "SELECT * FROM assessmentMarks WHERE church=\""+church+"\" AND assigned_class=\""+assigned_class+"\" AND date=\""+date+"\"";
				}else {
					return "";
				}				
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					AssessmentMarksDAO assessmentMarksDao = new AssessmentMarksDAO();
					assessmentMarksDao.setUniqueID(rs.getString("uniqueID"));
					assessmentMarksDao.setChurch(rs.getString("church"));
					assessmentMarksDao.setAssigned_class(rs.getString("assigned_class"));
					assessmentMarksDao.setDate(rs.getString("date"));
					assessmentMarksDao.setFirst_name(rs.getString("first_name"));
					assessmentMarksDao.setSurname(rs.getString("surname"));
					assessmentMarksDao.setAttendance(rs.getInt("attendance"));
					assessmentMarksDao.setSongs_4(rs.getInt("songs_4"));
					assessmentMarksDao.setWorship_message(rs.getInt("worship_message"));
					assessmentMarksDao.setTable_message(rs.getInt("table_message"));
					assessmentMarksDao.setBehaviour(rs.getInt("behaviour"));
					assessmentMarksDao.setMemory_verses(rs.getInt("memory_verses"));
					assessmentMarksDao.setTotal(rs.getInt("total"));
					assessmentMarksDao.setRemarks(rs.getString("remarks"));
					assessmentMarksDaoList.add(assessmentMarksDao);
				}
			
		}catch (Exception e) {
			e.printStackTrace();			
			return null;
		}
		try {
			JsonResponse = createJsonResponseForAssessmentMarks(assessmentMarksDaoList);
			
		}catch (Exception e) {
			e.printStackTrace();			
			return null;
		}
		finally {
			con.close();
		}
		return JsonResponse;
	}

	private String createJsonResponseForAssessmentMarks(ArrayList<AssessmentMarksDAO> assessmentMarksDaoList) {
		JSONObject responseJson = new JSONObject();
		JSONArray assessmentMarksArray = new JSONArray();
		
		for(int i=0; i<assessmentMarksDaoList.size(); i++) {
			JSONObject tempObj = new JSONObject();
			tempObj.put("uniqueID", assessmentMarksDaoList.get(i).getUniqueID());
			tempObj.put("church", assessmentMarksDaoList.get(i).getChurch());
			tempObj.put("class", assessmentMarksDaoList.get(i).getAssigned_class());
			tempObj.put("date", assessmentMarksDaoList.get(i).getDate());
			tempObj.put("first_name", assessmentMarksDaoList.get(i).getFirst_name());
			tempObj.put("surname", assessmentMarksDaoList.get(i).getSurname());						
			tempObj.put("attendance", assessmentMarksDaoList.get(i).getAttendance());
			tempObj.put("songs_4", assessmentMarksDaoList.get(i).getSongs_4());
			tempObj.put("worship_message", assessmentMarksDaoList.get(i).getWorship_message());
			tempObj.put("table_message", assessmentMarksDaoList.get(i).getTable_message());
			tempObj.put("behaviour", assessmentMarksDaoList.get(i).getBehaviour());
			tempObj.put("memory_verses", assessmentMarksDaoList.get(i).getMemory_verses());
			tempObj.put("total", assessmentMarksDaoList.get(i).getTotal());
			tempObj.put("remarks", assessmentMarksDaoList.get(i).getRemarks());
			assessmentMarksArray.put(tempObj);
		}
		
		responseJson.put("studentsMarks", assessmentMarksArray);
		return responseJson.toString();
	}

	public boolean editStudentsAssessmentMarks(String studentsMarksJson) throws SQLException {		
		
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		
		ArrayList<AssessmentMarksDAO> assessmentMarksDaoList = new ArrayList<AssessmentMarksDAO>();
		assessmentMarksDaoList = parseJsonData(studentsMarksJson);
		String teacherUsername = getTeacherUsernameFromJson(studentsMarksJson);
		String church = getChurchFromTeacherUsername(teacherUsername);
		String assigned_class = getClassFromTeacherUsername(teacherUsername);
		
		try {
				
				for(int i=0; i<assessmentMarksDaoList.size(); i++) {
					status = false;
					AssessmentMarksDAO assessmentMarksDao = new AssessmentMarksDAO();
					assessmentMarksDao = assessmentMarksDaoList.get(i);
					String query = "UPDATE assessmentMarks SET attendance=?, songs_4=?, worship_message=?, table_message=?, behaviour=?, memory_verses=?, total=?, remarks=? WHERE church=\""+church+"\" AND assigned_class=\""+assigned_class+"\" AND uniqueID=\""+assessmentMarksDao.getUniqueID()+"\"";
					ps = con.prepareStatement(query);
					ps.setInt(1, assessmentMarksDao.getAttendance());
					ps.setInt(2, assessmentMarksDao.getSongs_4());
					ps.setInt(3, assessmentMarksDao.getWorship_message());
					ps.setInt(4, assessmentMarksDao.getTable_message());
					ps.setInt(5, assessmentMarksDao.getBehaviour());
					ps.setInt(6, assessmentMarksDao.getMemory_verses());
					ps.setInt(7, assessmentMarksDao.getTotal());
					ps.setString(8, assessmentMarksDao.getRemarks());
					status = !ps.execute();
					if(!status) {
						return false;
					}
				}						
			
		}catch (Exception e) {
			e.printStackTrace();			
			return false;
		}
		finally {
			con.close();
		}
		return status;
	}

	private String getTeacherUsernameFromJson(String studentsMarksJson) {
		JSONObject assessmentMarksJson = new JSONObject(studentsMarksJson);
		return assessmentMarksJson.getString("username");
	}

}
