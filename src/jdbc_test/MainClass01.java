package jdbc_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import common.DBConnection;

class MemberDTO {
	private int age;
	private String name, id;
	public void setName(String name) { this.name = name; }
	public String getName() { return name; }
	public void setId(String id) { this.id = id; }
	public String getId() { return id; }
	public void setAge(int age) { this.age = age; }
	public int getAge() { return age; }
}
class DB {
	// 데이터베이스 연결 객체
	Connection con;
	// DB명령어 전송 객체
	PreparedStatement ps;
	// DB명령어 후 결과 얻어오는 객체
	ResultSet rs;
	public DB() {
		try {
			con = DBConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public DB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이브 로드 성공");
			// 오라클 연결된 연결 객체를 얻어오는 기능
			String id = "c##java", pwd = "1234";
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			con = DriverManager.getConnection(url, id, pwd);
			System.out.println("연결 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public void select() {
		String sql = "select * from newst";
		try {
			ps = con.prepareStatement(sql);
			// select는 무조건 executeQuery를 사용함
			rs = ps.executeQuery();
			while(rs.next()) {
			//System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			}
			/*
			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			
			System.out.println(rs.next());
			System.out.println(rs.getString("id"));
			System.out.println(rs.getString("name"));
			System.out.println(rs.getInt("age"));
			
			System.out.println(rs.next());
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<MemberDTO> select2() {
		String sql = "select * from newst";
		ArrayList<MemberDTO> list = new ArrayList<>();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public MemberDTO search(String id) {
		String sql = "select * from newst where id = '" + id + "'";
		MemberDTO dto = null;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getInt("age"));
				/*
				System.out.println(rs.getString("id"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getInt("age"));
				*/
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	public int register(MemberDTO dd) {
		int result = 0;
		String sql = "insert into newst(id, name, age) values(?, ?, ?)";
		try {
			ps = con.prepareStatement(sql);
			
			ps.setString(1, dd.getId());
			ps.setString(2, dd.getName());
			ps.setInt(3, dd.getAge());
			
			//ps.executeQuery();
			// select : executeQuery 사용
			// select를 제외한 나머지 : update 사용
			result = ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public int delete(String id) {
		String sql = "delete from newst where id = ?";
		int result1 = 0;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			result1 = ps.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result1;
	}
}
public class MainClass01 {
	public static void main(String[] args) {
		DB db = new DB();
		//db.select();
		Scanner sc = new Scanner(System.in);
		int num, age = 0;
		String id = null, name = null;
		while(true) {
			System.out.println("1. 모든 사용자 보기");
			System.out.println("2. 검색");
			System.out.println("3. 회원가입");
			System.out.println("4. 회원삭제");
			num = sc.nextInt();
			switch(num) {
			case 1 : 
				ArrayList<MemberDTO> list = db.select2();
				System.out.println("id\tname\tage");
				System.out.println("====================");
				for(MemberDTO m : list) {
					System.out.print(m.getId() + "\t");
					System.out.print(m.getName() + "\t");
					System.out.println(m.getAge());
					System.out.println("--------------------");
				}
				break;
			case 2 : 
				System.out.print("검색 id 입력 : ");
				id = sc.next();
				MemberDTO d = db.search(id);
				if(d == null) {
					System.out.println("존재하지않는 아이디");
				}
				else {
					System.out.println("id : " + d.getId());
					System.out.println("name : " + d.getName());
					System.out.println("age : " + d.getAge());
				}
				break;
			case 3 : 
				System.out.print("아이디입력 : ");
				id = sc.next();
				System.out.print("이름입력 : ");
				name = sc.next();
				System.out.print("나이입력 : ");
				age = sc.nextInt();
				MemberDTO dd = new MemberDTO();
				dd.setId(id); dd.setName(name); dd.setAge(age);
				int result = db.register(dd);
				if(result == 0) {
					System.out.println("동일한 아이디가 존재합니다");
				} else {
					System.out.println("회원가입완료");
				}
				break;
			case 4 : 
				System.out.print("삭제할 id 입력 : ");
				id = sc.next();
				int result1 = db.delete(id);
				if(result1 == 0) {
					System.out.println("존재하지않는 아이디");
				}
				else {
					System.out.println(id + "님은 삭제!");
				}
				break;
			}
		}
	}
}
