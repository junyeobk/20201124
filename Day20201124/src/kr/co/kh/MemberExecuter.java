package kr.co.kh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberExecuter {
	public static String session;

	static {
		session = null;
	}

	public static void main(String[] args) {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Statement stmt = null;
		Connection conn = null;
		String sql = null;
		String sql2 = null;
		String protocol = null;
		String id = null;
		String pw = null;
		String addr = null;
		String tel = null;
		int cnt = 0;
		int cnt2 = 0;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "khbclass", "dkdlxl");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		while (true) {
			System.out.println("R: 회원가입 L:회원목록 S:ID찾기 D:회원탈퇴 E:회원수정 I:로그인 O:로그아웃");
			try {
				protocol = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (protocol.equalsIgnoreCase("r")) { // 회원가입
				if (session != null) {
					System.out.println("이미 로그인 중입니다.");
					continue;
				}
				try {
					System.out.println("아이디입력:");
					id = input.readLine();
					System.out.println("패스워드입력:");
					pw = input.readLine();
					System.out.println("주소입력:");
					addr = input.readLine();
					System.out.println("전화번호입력:");
					tel = input.readLine();
					stmt = conn.createStatement();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sql = "insert into member(id,pw,addr,tel) values ('" + id + "','" + pw + "','" + addr + "','" + tel
						+ "')";
				try {
					cnt = stmt.executeUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println(cnt + "명 회원등록되었습니다.");

			} else if (protocol.equalsIgnoreCase("l")) { // 회원목록
				try {
					stmt = conn.createStatement();
					sql = "select id,pw,addr,tel from member";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String id1 = rs.getString("id");
						String pw1 = rs.getString("pw");
						String addr1 = rs.getString("addr");
						String tel1 = rs.getString("tel");
						System.out.println(id1 + "\t" + pw1 + "\t" + addr1 + "\t" + tel1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else if (protocol.equalsIgnoreCase("s")) { // ID찾기
				System.out.println("찾는 ID를 입력:");
				try {
					String searchId = input.readLine();
					stmt = conn.createStatement();
					sql = "select id,pw,addr,tel from member where id = '" + searchId + "'";
					cnt = stmt.executeUpdate(sql);
					ResultSet rs = stmt.executeQuery(sql);

					while (rs.next()) {
						String id1 = rs.getString("id");
						String pw1 = rs.getString("pw");
						String addr1 = rs.getString("addr");
						String tel1 = rs.getString("tel");
						System.out.println(id1 + "\t" + pw1 + "\t" + addr1 + "\t" + tel1);
					}
					if (cnt == 0)
						System.out.println("찾는 아이디가 없습니다.");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else if (protocol.equalsIgnoreCase("d")) { // 회원탈퇴
				if (session != null) {
					try {
						stmt = conn.createStatement();
						sql = "delete from member where id = '" + id + "'";
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("회원탈퇴되었습니다.");
					session = null;
				} else if (session == null) {
					System.out.println("로그인 먼저 해주세요");
				}
			} else if (protocol.equalsIgnoreCase("e")) { // 회원수정
				if (session != null) {
					try {
						stmt = conn.createStatement();
						sql = "select id,pw,addr,tel from member where id = '" + session + "'";
						ResultSet rs = stmt.executeQuery(sql);
						while (rs.next()) {
							String id1 = rs.getString("id");
							String pw1 = rs.getString("pw");
							String addr1 = rs.getString("addr");
							String tel1 = rs.getString("tel");
							System.out.println(id1 + "\t" + pw1 + "\t" + addr1 + "\t" + tel1);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						System.out.println("아이디입력:");
						String id2 = input.readLine();
						System.out.println("패스워드입력:");
						String pw2 = input.readLine();
						System.out.println("주소입력:");
						String addr2 = input.readLine();
						System.out.println("전화번호입력:");
						String tel2 = input.readLine();
						stmt = conn.createStatement();
						sql = "update member SET id = '" + id2 + "',pw = '" + pw2 + "', addr = '" + addr2 + "', tel = '"
								+ tel2 + "' WHERE id = '" + session + "'";
						cnt = stmt.executeUpdate(sql);
						session = id2;
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					System.out.println(cnt + "명 회원이 수정되었습니다.");
				} else if (session == null) {
					System.out.println("로그인 먼저 해주세요");
				}
			} else if (protocol.equalsIgnoreCase("i")) { // 로그인
				System.out.println("아이디입력:");
				try {
					id = input.readLine();
					System.out.println("패스워드입력:");
					pw = input.readLine();
					stmt = conn.createStatement();
					sql = "select id,pw,addr,tel from member where id = '" + id + "'";
					sql2 = "select id,pw,addr,tel from member where pw = '" + pw + "'";
					cnt = stmt.executeUpdate(sql);
					cnt2 = stmt.executeUpdate(sql2);
					if (cnt == 0)
						System.out.println("없는 아이디입니다.");
					else if (cnt2 == 0)
						System.out.println("비밀번호가 틀렸습니다.");
					else if (cnt != 0 || cnt2 != 0) {
						System.out.println("환영합니다. 로그인되었습니다.");
						session = id;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else if (protocol.equalsIgnoreCase("o")) { // 로그아웃
				if (session != null) {
					System.out.println("로그아웃되었습니다.");
					session = null;
				} else if (session == null) {
					System.out.println("로그인 먼저 해주세요.");
				}
			}
		}
	}
}
