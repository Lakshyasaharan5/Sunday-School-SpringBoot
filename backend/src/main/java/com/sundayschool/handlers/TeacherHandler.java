package com.sundayschool.handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sundayschool.dao.AssessmentMarksDAO;
import com.sundayschool.dao.TeacherDAO;
import com.sundayschool.databaseutility.ConnectionUtility;

public class TeacherHandler {

	public TeacherHandler() {
		super();
	}
	
	public TeacherDAO parseJsonData(String newTeacherJsonString) {
		
		JSONObject newTeacherJson = new JSONObject(newTeacherJsonString);
		TeacherDAO teacherDao = new TeacherDAO();
		if(newTeacherJson.has("id_teacher")) {
			teacherDao.setId_teacher(newTeacherJson.getInt("id_teacher"));
		}
		if(newTeacherJson.has("username")) {
			teacherDao.setUsername(newTeacherJson.getString("username"));
		}else {
			String username = createUsername(newTeacherJson.getString("teacher_name"), newTeacherJson.getString("mobile"));
			teacherDao.setUsername(username);
		}
		teacherDao.setTeacher_name(newTeacherJson.getString("teacher_name"));
		teacherDao.setChurch(newTeacherJson.getString("church"));
		teacherDao.setMobile(newTeacherJson.getString("mobile"));
		teacherDao.setAssigned_class(newTeacherJson.getString("assigned_class"));
		return teacherDao;
		
	}
	
	public String createUsername(String teacher_name, String mobile) {
		
		String first_name = teacher_name.split(" ")[0].toLowerCase();
		String last_five_digits_mobile = mobile.substring(mobile.length()-5);
		
		return first_name + last_five_digits_mobile;
	}

	public boolean storeNewTeacher(String newTeacherJson) throws SQLException {
		TeacherDAO teacherDao = new TeacherDAO();
		teacherDao = parseJsonData(newTeacherJson);
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		try {
				String query = "INSERT INTO teacher (username, teacher_name, church, assigned_class, mobile, enabled) VALUES (?, ?, ?, ?, ?, ?)";
				ps = con.prepareStatement(query);
				ps.setString(1, teacherDao.getUsername());
				ps.setString(2, teacherDao.getTeacher_name());
				ps.setString(3, teacherDao.getChurch());
				ps.setString(4, teacherDao.getAssigned_class());
				ps.setString(5, teacherDao.getMobile());
				ps.setInt(6, 1);
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
	
	public String getListOfTeacher(String church) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		String JsonResponse = "";
		ArrayList<TeacherDAO> teacherDaoList = new ArrayList<TeacherDAO>();
		
		try {
				String query = "SELECT * FROM teacher WHERE church=\""+church+"\" AND enabled=1;";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				
				while(rs.next()) {
					TeacherDAO teacherDao = new TeacherDAO();
					teacherDao.setId_teacher(rs.getInt("id_teacher"));
					teacherDao.setUsername(rs.getString("username"));
					teacherDao.setTeacher_name(rs.getString("teacher_name"));
					teacherDao.setChurch(rs.getString("church"));
					teacherDao.setAssigned_class(rs.getString("assigned_class"));
					teacherDao.setMobile(rs.getString("mobile"));
					teacherDaoList.add(teacherDao);
				}
				
				JsonResponse = createJsonResponse(teacherDaoList);
			
		}catch (Exception e) {
			e.printStackTrace();			
			return "";
		}
		finally {
			con.close();
		}
		return JsonResponse;		
	}
	
	public boolean editTeacher(String teacherJson) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		TeacherDAO teacherDao = new TeacherDAO();
		teacherDao = parseJsonData(teacherJson);
		
		try {
			String query = "UPDATE teacher SET teacher_name=?, church=?, assigned_class=?, mobile=?, username=? WHERE username=\""+teacherDao.getUsername()+"\" AND enabled=1";
			ps = con.prepareStatement(query);
			ps.setString(1, teacherDao.getTeacher_name());
			ps.setString(2, teacherDao.getChurch());
			ps.setString(3, teacherDao.getAssigned_class());
			ps.setString(4, teacherDao.getMobile());
			String username = createUsername(teacherDao.getTeacher_name(), teacherDao.getMobile());
			ps.setString(5, username);
			status = !ps.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			con.close();
		}
		return status;
	}

	public String createJsonResponse(ArrayList<TeacherDAO> teacherDaoList) {
		
		JSONObject responseJson = new JSONObject();
		JSONArray teachersArray = new JSONArray();
		
		for(int i=0; i<teacherDaoList.size(); i++) {
			JSONObject tempObj = new JSONObject();
			tempObj.put("username", teacherDaoList.get(i).getUsername());
			tempObj.put("teacher_name", teacherDaoList.get(i).getTeacher_name());			
			tempObj.put("church", teacherDaoList.get(i).getChurch());
			tempObj.put("assigned_class", teacherDaoList.get(i).getAssigned_class());
			tempObj.put("mobile", teacherDaoList.get(i).getMobile());
			teachersArray.put(tempObj);
		}
		
		responseJson.put("teachers", teachersArray);
		return responseJson.toString();
	}

	public boolean deleteTeacher(String username) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		
		try {
			String query = "UPDATE teacher SET enabled=0 WHERE username=\""+username+"\"";
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

	public String getTeacherDataByUsername(String username) throws SQLException {
		ConnectionUtility.loadDriver();
		Connection con = ConnectionUtility.getConnection();
		boolean status = false;
		PreparedStatement ps;
		String JsonResponse = "";
		TeacherDAO teacherDao = new TeacherDAO();
		
		try {
				String query = "SELECT * FROM teacher WHERE username=\""+username+"\" AND enabled=1;";
				ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				
				
				while(rs.next()) {
					teacherDao.setId_teacher(rs.getInt("id_teacher"));
					teacherDao.setUsername(rs.getString("username"));
					teacherDao.setTeacher_name(rs.getString("teacher_name"));
					teacherDao.setChurch(rs.getString("church"));
					teacherDao.setAssigned_class(rs.getString("assigned_class"));
					teacherDao.setMobile(rs.getString("mobile"));
				}
				
				JsonResponse = createJsonResponse(teacherDao);
			
		}catch (Exception e) {
			e.printStackTrace();			
			return "";
		}
		finally {
			con.close();
		}
		return JsonResponse;
	}

	private String createJsonResponse(TeacherDAO teacherDao) {
		JSONObject tempObj = new JSONObject();
		tempObj.put("username", teacherDao.getUsername());
		tempObj.put("teacher_name", teacherDao.getTeacher_name());			
		tempObj.put("church", teacherDao.getChurch());
		tempObj.put("assigned_class", teacherDao.getAssigned_class());
		tempObj.put("mobile", teacherDao.getMobile());
		return tempObj.toString();
	}
}
