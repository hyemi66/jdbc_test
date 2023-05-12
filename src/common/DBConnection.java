package common;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	public static Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("드라이브 로드 성공");
		// 오라클 연결된 연결 객체를 얻어오는 기능
		String id = "c##java", pwd = "1234";
		String url = "jdbc:oracle:thin:@localhost:1521/xe";
		Connection con = DriverManager.getConnection(url, id, pwd);
		return con;
	}
}
