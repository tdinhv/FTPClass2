package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LibraryConnectDb {
	private Connection conn;
	private String url;
	private String user;
	private String password;
	
	public LibraryConnectDb(){
		
		this.url = "jdbc:mysql://localhost:3306/ftp?useUnicode=true&characterEncoding=UTF-8";
		this.user = "root";
		this.password ="";
		
	}
	public Connection getConnectMySql(){
		//nạp driver
		try {
			//sql server sua doi lai, gg
			Class.forName("com.mysql.jdbc.Driver");
			//tạo chuỗi conn
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println("Không thể nạp driver");
			//khi lam du an sau nay nen throw ra de xu ly
			e.printStackTrace();
		}
		return conn;
	}
	public static void main(String[] args) {
		LibraryConnectDb lDb = new LibraryConnectDb();
		System.out.println(lDb.getConnectMySql());
	}
}
