package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.TokenUtils;

public class DBUtils {
	private Connection conn;
	private String url = "jdbc:mysql://127.0.0.1:3306/Login"; // 指定连接数据库的URL
	private String user = "root"; // 指定连接数据库的用户名
	private String password = "1213"; // 指定连接数据库的密码

	private Statement sta;
	private ResultSet rs;

	// 打开数据库连接
	public void openConnect() {
		try {
			// 加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);// 创建数据库连接
			if (conn != null) {
				System.out.println("Successfully start up database connection!"); // 连接成功的提示信息
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}

	public ResultSet getUserInfo() {
		// 创建 statement对象
		try {
			sta = conn.createStatement();
			// 执行SQL查询语句
			rs = sta.executeQuery("select * from userinfo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

//	public ResultSet getUser() {
//		// 创建 statement对象
//		try {
//			sta = conn.createStatement();
//			// 执行SQL查询语句
//			rs = sta.executeQuery("select * from user");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rs;
//	}

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

//	// 注册 将用户名和密码插入到数据库(id设置的是自增长的，因此不需要插入)
//	public boolean insertDataToDB(String username, String password) {
//		String sql = " insert into user ( user_name , user_pwd ) values ( " + "'" + username + "', " + "'" + password
//				+ "' )";
//		try {
//			sta = conn.createStatement();
//			// 执行SQL查询语句
//			return sta.execute(sql);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

//	public boolean isExistInDB(String username, String password) {
//		boolean isFlag = false;
//		// 创建 statement对象
//		try {
//			sta = conn.createStatement();
//			// 执行SQL查询语句
//			rs = sta.executeQuery("select * from user");
//			if (rs != null) {
//				while (rs.next()) {
//					if (rs.getString("user_name").equals(username) && rs.getString("user_pwd").equals(password)) {
//						isFlag = true;
//						break;
//					}
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			isFlag = false;
//		}
//		return isFlag;
//	}

	// 判断数据库中是否存在某个用户名,注册的时候判断
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