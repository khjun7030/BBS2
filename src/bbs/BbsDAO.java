package bbs;



import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.ArrayList;



public class BbsDAO {

		// dao : 데이터베이스 접근 객체의 약자

	

		private Connection conn; // connection:db에접근하게 해주는 객체

		//private PreparedStatement pstmt;

		private ResultSet rs;



		// mysql 처리부분

		public BbsDAO() {

			// 생성자를 만들어준다.

			try {

				String dbURL = "jdbc:mysql://localhost:3306/BBS?&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

				String dbID = "root";

				String dbPassword = "root";

				Class.forName("com.mysql.jdbc.Driver");

				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		

		//현재의 시간을 가져오는 함수

		public String getDate() { 

			String SQL = "SELECT NOW()";

			try {

				PreparedStatement pstmt = conn.prepareStatement(SQL);

				rs = pstmt.executeQuery();

				if(rs.next()) {

					return rs.getString(1);

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

			return ""; //데이터베이스 오류

		}

		

		//bbsID 게시글 번호 가져오는 함수

			public int getNext() { 

				String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";

				try {

					PreparedStatement pstmt = conn.prepareStatement(SQL);

					rs = pstmt.executeQuery();

					if(rs.next()) {

						return rs.getInt(1) + 1;

					}

					return 1;//첫 번째 게시물인 경우

				} catch (Exception e) {

					e.printStackTrace();

				}

				return -1; //데이터베이스 오류

			}

			

			//실제로 글을 작성하는 함수

			public int write(String bbsTitle, String userID, String bbsContent) { 

				String SQL = "INSERT INTO BBS VALUES(?, ?, ?, ?, ?, ?)";

				try {

					

					PreparedStatement pstmt = conn.prepareStatement(SQL);

					pstmt.setInt(1, getNext());

					pstmt.setString(2, bbsTitle);

					pstmt.setString(3, userID);

					pstmt.setString(4, getDate());

					pstmt.setString(5, bbsContent);

					pstmt.setInt(6,1);

					

					return pstmt.executeUpdate();

					

				} catch (Exception e) {

					e.printStackTrace();

				}

				return -1; //데이터베이스 오류

			}
			
			public ArrayList<Bbs> getList(int pageNumber) {
				String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
				ArrayList<Bbs> list = new ArrayList<Bbs>();
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						Bbs bbs = new Bbs();
						bbs.setBbsID(rs.getInt(1));
						bbs.setBbsTitle(rs.getString(2));
						bbs.setUserID(rs.getString(3));
						bbs.setBbsDate(rs.getString(4));
						bbs.setBbsContent(rs.getString(5));
						bbs.setBbsAvailable(rs.getInt(6));
						list.add(bbs);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				return list;
			}
			
			
			public boolean nextPage(int pageNumber) {
				String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						return true;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				return false;
			}
			
		public Bbs getBbs(int bbsID) {
			String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, bbsID);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					return bbs;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//게시글 수정 메소드
		public int update(int bbsID, String bbsTitle, String bbsContent) {
			String sql = "update bbs set bbsTitle = ?, bbsContent = ? where bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bbsTitle);
				pstmt.setString(2, bbsContent);
				pstmt.setInt(3, bbsID);
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
		}
		
		//게시글 삭제 메소드
		public int delete(int bbsID) {
			//실제 데이터를 삭제하는 것이 아니라 게시글 유효숫자를 '0'으로 수정한다
			String sql = "update bbs set bbsAvailable = 0 where bbsID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bbsID);
				return pstmt.executeUpdate();
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류 
		}
		

	}