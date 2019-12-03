package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.net.ssl.SSLException;

public class Main {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// update USER, PASS and DB URL according to credentials provided by the website:
	// https://remotemysql.com/
	// in future move these hard coded strings into separated config file or even better env variables
	static private final String DB = "ynF9e9kJi3";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "ynF9e9kJi3";
	static private final String PASS = "i0InE30YTj";

	public static void main(String[] args) throws SSLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			System.out.println("\t============");

			String sql = "SELECT * FROM flights";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int num = rs.getInt("num");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");
				int price = rs.getInt("price");

				System.out.format("Number %5s Origin %15s destinations %18s Distance %5d Price %5d\n", num, origin, destination, distance, price);
			}

			System.out.println("\t============");

			sql = "SELECT origin, destination, distance, num FROM flights";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");

				System.out.print("From: " + origin);
				System.out.print(",\tTo: " + destination);
				System.out.println(",\t\tDistance: " + distance);
			}

			System.out.println("\t============");
			
			sql = "SELECT origin, destination FROM flights WHERE distance > ?";
			PreparedStatement prep_stmt = conn.prepareStatement(sql);
			prep_stmt.setInt(1, 200);
			rs = prep_stmt.executeQuery();
			while (rs.next()) {
				String origin = rs.getString("origin");
				System.out.println("From: " + origin);
			}
			
			
			// Answer to a
	
			System.out.println("\t============");
			sql = "update flights set price = ? where num = ?";
			PreparedStatement prep_stmt2 = conn.prepareStatement(sql);
			prep_stmt2.setInt(1, 2019);
			prep_stmt2.setInt(2, 387);
			prep_stmt2.executeUpdate();
			
			
			
			
			
			
			// Answer to b
			System.out.println("\t============");
			System.out.println("Section b\n");
			sql = "SELECT * FROM flights WHERE num= ?";
			PreparedStatement prep_stmt3 = conn.prepareStatement(sql);
			prep_stmt3.setInt(1, 387);
			rs = prep_stmt3.executeQuery();
			while (rs.next()) {
				int num = rs.getInt("num");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");
				int price = rs.getInt("price");
				System.out.format("Number %5s Origin %15s destinations %18s Distance %5d Price %5d\n", num, origin, destination, distance, price);
			}
			
			
			
			// Answer to c.1
			System.out.println("\t============");
			System.out.println("Section c.1\n");
			sql="SELECT * FROM flights WHERE distance > 1000";
			Statement stmt1=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet uprs=stmt1.executeQuery(sql);
			while (uprs.next()) {
				uprs.updateInt("price", uprs.getInt("price")+100);
				uprs.updateRow();
			}
			printAll(conn);
			
			
			// Answer to c.2
			System.out.println("\t============");
			System.out.println("Section c.2\n");
			sql="SELECT * FROM flights WHERE price < 300";
			uprs=stmt1.executeQuery(sql);
			while (uprs.next()) {
				uprs.updateInt("price", uprs.getInt("price")-25);
				uprs.updateRow();
			}
			printAll(conn);
			
			
			// Answer to d.1
			System.out.println("\t============");
			System.out.println("Section d.1\n");
			sql = "update flights set price = price + ? where distance > ?";
			PreparedStatement prep_stmt4 = conn.prepareStatement(sql);
			prep_stmt4.setInt(1, 100);
			prep_stmt4.setInt(2, 1000);
			prep_stmt4.executeUpdate();
			printAll(conn);
			
			
			
			// Answer to d.2
			System.out.println("\t============");
			System.out.println("Section d.2\n");
			sql = "update flights set price = price - ? where price < ? AND price > 24";
			PreparedStatement prep_stmt6 = conn.prepareStatement(sql);
			prep_stmt6.setInt(1, 25);
			prep_stmt6.setInt(2, 300);
			prep_stmt6.executeUpdate();
			printAll(conn);
			
			
			
			
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	public static void printAll(Connection conn ) {
		String sql = "SELECT * FROM flights";
		PreparedStatement prep_stmt5;
		try {
			prep_stmt5 = conn.prepareStatement(sql);
			ResultSet rs = prep_stmt5.executeQuery();
			while (rs.next()) {
				int num = rs.getInt("num");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");
				int price = rs.getInt("price");
				System.out.format("Number %5s Origin %15s destinations %18s Distance %5d Price %5d\n", num, origin, destination, distance, price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
