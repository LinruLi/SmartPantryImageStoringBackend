package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.TokenUtils;

public class DBUtils {
	private Connection conn;
	private String url = "jdbc:mysql://127.0.0.1:3306/Login"; // specify url for database connection
	private String user = "root"; // specify username
	private String password = "1213"; // specify password

	private Statement sta;
	private ResultSet rs;

	// open up database connection
	public void openConnect() {
		try {
			// load up database driver for connection
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);// build up connection
			if (conn != null) {
				System.out.println("Successfully start up database connection!"); // give out feedback once got connected
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public ResultSet getUserInfo() {
		try {
			sta = conn.createStatement();
			rs = sta.executeQuery("select * from userinfo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

//	public ResultSet getUser() {
//
//		try {
//			sta = conn.createStatement();
//
//			rs = sta.executeQuery("select * from user");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rs;
//	}

    // insert data to database
	public boolean insertDataToDB(String col, String row, String imageCode) {
		String token = TokenUtils.getToken(col, row);
		System.out.println("path------->" + imageCode);
		String imagePath = "http://10.118.0.84:8080/home/dunediniot/Pictures/pantryImages/" + imageCode;
		try {
			sta = conn.createStatement();
            String sql = " insert into Login.userinfo ( pantry_column, pantry_row, token, image_code ) values ( " + "'" + col
                    + "', " + "'" + row + "', " + "'" + token + "', " + "'" + imagePath + "' )";

			return sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//update photo in database
	public boolean updateDataToDB(String col, String row, String imageCode) {
		System.out.println("try to update photo in which column = " + col + "and row = " + row);
		String imagePath = "http://10.118.0.84:8080/home/dunediniot/Pictures/pantryImages/" + imageCode;
		try {
			sta = conn.createStatement();
			String sql = " UPDATE Login.userinfo SET image_code = " + "'" + imagePath + "'" +
                    "WHERE pantry_column =" + "'" + col + "'" + "AND pantry_row =" + "'" + row + "'";
			return sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	// check if any data that is existed in database
	public boolean isExistInDB(String col, String row) {
		boolean isFlag = false;
		// 创建 statement对象
		try {
			sta = conn.createStatement();
			// 执行SQL查询语句
			rs = sta.executeQuery("select * from userinfo");
			if (rs != null) {
				while (rs.next()) {
					if (rs.getString("pantry_column").equals(col) && rs.getString("pantry_row").equals(row)) {
						isFlag = true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isFlag = false;
		}
		return isFlag;
	}

	// Close up database connection
	public void closeConnect() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (sta != null) {
				sta.close();
			}
			if (conn != null) {
				conn.close();
			}
			System.out.println("Successfully close up database connection!");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}