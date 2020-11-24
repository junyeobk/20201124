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
			System.out.println("R: ȸ������ L:ȸ����� S:IDã�� D:ȸ��Ż�� E:ȸ������ I:�α��� O:�α׾ƿ�");
			try {
				protocol = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (protocol.equalsIgnoreCase("r")) { // ȸ������
				if (session != null) {
					System.out.println("�̹� �α��� ���Դϴ�.");
					continue;
				}
				try {
					System.out.println("���̵��Է�:");
					id = input.readLine();
					System.out.println("�н������Է�:");
					pw = input.readLine();
					System.out.println("�ּ��Է�:");
					addr = input.readLine();
					System.out.println("��ȭ��ȣ�Է�:");
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
				System.out.println(cnt + "�� ȸ����ϵǾ����ϴ�.");

			} else if (protocol.equalsIgnoreCase("l")) { // ȸ�����
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

			} else if (protocol.equalsIgnoreCase("s")) { // IDã��
				System.out.println("ã�� ID�� �Է�:");
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
						System.out.println("ã�� ���̵� �����ϴ�.");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else if (protocol.equalsIgnoreCase("d")) { // ȸ��Ż��
				if (session != null) {
					try {
						stmt = conn.createStatement();
						sql = "delete from member where id = '" + id + "'";
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("ȸ��Ż��Ǿ����ϴ�.");
					session = null;
				} else if (session == null) {
					System.out.println("�α��� ���� ���ּ���");
				}
			} else if (protocol.equalsIgnoreCase("e")) { // ȸ������
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
						System.out.println("���̵��Է�:");
						String id2 = input.readLine();
						System.out.println("�н������Է�:");
						String pw2 = input.readLine();
						System.out.println("�ּ��Է�:");
						String addr2 = input.readLine();
						System.out.println("��ȭ��ȣ�Է�:");
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
					System.out.println(cnt + "�� ȸ���� �����Ǿ����ϴ�.");
				} else if (session == null) {
					System.out.println("�α��� ���� ���ּ���");
				}
			} else if (protocol.equalsIgnoreCase("i")) { // �α���
				System.out.println("���̵��Է�:");
				try {
					id = input.readLine();
					System.out.println("�н������Է�:");
					pw = input.readLine();
					stmt = conn.createStatement();
					sql = "select id,pw,addr,tel from member where id = '" + id + "'";
					sql2 = "select id,pw,addr,tel from member where pw = '" + pw + "'";
					cnt = stmt.executeUpdate(sql);
					cnt2 = stmt.executeUpdate(sql2);
					if (cnt == 0)
						System.out.println("���� ���̵��Դϴ�.");
					else if (cnt2 == 0)
						System.out.println("��й�ȣ�� Ʋ�Ƚ��ϴ�.");
					else if (cnt != 0 || cnt2 != 0) {
						System.out.println("ȯ���մϴ�. �α��εǾ����ϴ�.");
						session = id;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else if (protocol.equalsIgnoreCase("o")) { // �α׾ƿ�
				if (session != null) {
					System.out.println("�α׾ƿ��Ǿ����ϴ�.");
					session = null;
				} else if (session == null) {
					System.out.println("�α��� ���� ���ּ���.");
				}
			}
		}
	}
}
