package com.sundayschool.databaseutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtility {

	public static String dbUrl = "jdbc:mysql://localhost:3306/sundayschool_db1";
	public static String dbUname = "root";
	public static String dbPasswd = "chandan1997";
	public static String dbDriver = "com.mysql.cj.jdbc.Driver";
	
	
	public static void loadDriver() {		
		try {
			Class.forName(dbDriver);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() 
	{
		Connection con = null;
		try {
			con = DriverManager.getConnection(dbUrl, dbUname, dbPasswd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
		
	}
	
}
