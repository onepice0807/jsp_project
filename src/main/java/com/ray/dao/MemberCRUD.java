package com.ray.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.ray.etc.Paginginfo;
import com.ray.etc.UploadedFile;
import com.ray.vo.Member;
import com.ray.vo.PointLog;
import com.ray.vo.searchCritrtia;

public class MemberCRUD implements MemberDAO {

	private static MemberCRUD instance = null;

	private MemberCRUD() {
	}

	public static MemberCRUD getInstance() {
		if (instance == null) {
			instance = new MemberCRUD();
		}

		return instance;
	}

	@Override
	public Member duplicateUserId(String tmpUserId) throws NamingException, SQLException {
		Member result = null;

		Connection con = DBConnection.getInstance().dbConnect();

		String query = "select * from member where userId = ?";
		PreparedStatement pstmt = con.prepareStatement(query);

		pstmt.setString(1, tmpUserId);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			result = new Member(rs.getString("userId"), rs.getString("userPwd"), rs.getString("userEmail"),
					rs.getTimestamp("regusterDate"), rs.getInt("userImg"), rs.getInt("userpont"));
		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return result;
	}

	@Override
	public int registerMemberWithFile(UploadedFile uf, Member newMember, String why, int howmuch)
			throws NamingException, SQLException {

		int result = -1;

		Connection con = DBConnection.getInstance().dbConnect();
		con.setAutoCommit(false); // 트랜잭션 처리를 시작하겠다

//		(1) 업로드파일이 있다면 업로드된 파일의 정보를 uploadedFile 테이블에 insert
		int no = -1;
		int insertCnt = -1;
		no = insertUploadedFileInfo(con, uf);

//		(2) 회원가입 (순수한 회원 데이터 저장 + (1)번에서 저장된 no )
		if (no != -1) {
			newMember.setUserImg(no);
			insertCnt = insertMember(newMember, con);
		}

		int logCnt = -1;
//		(3) pointlog 테이블에 회원가입 포인트 로그를 남겨야 함
		if (insertCnt == 1) {
			logCnt = insertPointLog(newMember.getUserId(), why, howmuch, con);
		}

		if (no != -1 && insertCnt == 1 && logCnt == 1) {
			con.commit();
			result = 0;
		} else {
			con.rollback();
		}

		con.setAutoCommit(true); // 트랜잭션 처리 끝
		con.close();

		return result;
	}

	@Override
	public int insertPointLog(String userId, String why, int howmuch, Connection con)
			throws NamingException, SQLException {
		int result = -1;

		String query = "insert into pointlog(why, howmuch, who) values(?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(query);

		pstmt.setString(1, why);
		pstmt.setInt(2, howmuch);
		pstmt.setString(3, userId);

		result = pstmt.executeUpdate();

		return result;
	}

	@Override
	public int insertUploadedFileInfo(Connection con, UploadedFile uf) throws NamingException, SQLException {
		// (1) 업로드파일이 있다면 업로드된 파일의 정보를 uploadedFile 테이블에 insert
		int result = -1;

		String query = "insert into uolodadedfile(originalFIleName, ext, newFileName, fileSize) " + "values(?,?,?,?)";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, uf.getOriginalFileName());
		pstmt.setString(2, uf.getExt());
		pstmt.setString(3, uf.getNewFeileName());
		pstmt.setLong(4, uf.getSize());

		pstmt.executeUpdate();
		pstmt.close();

		result = getUploadedFileNo(con, uf);

		return result; // 현재 업로드된 파일의 저장 번호
	}

	private int getUploadedFileNo(Connection con, UploadedFile uf) throws NamingException, SQLException {
		int no = -1;

		String query = "select no from uolodadedfile where newFileName=?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, uf.getNewFeileName());

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			no = rs.getInt("no");
		}

		rs.close();
		pstmt.close();

		return no;
	}

	@Override
	public int insertMember(Member newMember, Connection con) throws NamingException, SQLException {

		int result = -1;

		String query = "insert into member(userId, userPwd, userEmail, userImg) " + "values(?, sha1(md5(?)), ?, ?)";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, newMember.getUserId());
		pstmt.setString(2, newMember.getUserPwd());
		pstmt.setString(3, newMember.getUserEmail());
		pstmt.setInt(4, newMember.getUserImg());

		result = pstmt.executeUpdate();

		pstmt.close();

		return result; // 가입된 회원 명 수
	}

	public int insertMember(Member newMember, Connection con, boolean userImg) throws NamingException, SQLException {

		int result = -1;

		String query = "insert into member(userId, userPwd, userEmail) " + "values(?, sha1(md5(?)), ?)";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, newMember.getUserId());
		pstmt.setString(2, newMember.getUserPwd());
		pstmt.setString(3, newMember.getUserEmail());

		result = pstmt.executeUpdate();

		pstmt.close();

		return result; // 가입된 회원 명 수
	}

	@Override
	public int registerMember(Member newMemeber, String why, int howmuch) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.getInstance().dbConnect();
		con.setAutoCommit(false);

		// (1) 회원가입(순수한 회원 데이터 저장, userImg컬럼의 값 1(default))
		int insertCnt = insertMember(newMemeber, con, false);
		int logCnt = -1;
		if (insertCnt == 1) {
			// (2) pointlog 테이블에 회원가입 포인트 로그를 남겨야 함
			logCnt = insertPointLog(newMemeber.getUserId(), why, howmuch, con);
		}

		if (insertCnt == 1 && logCnt == 1) {
			con.commit();
			result = 0;
		} else {
			con.rollback();
		}

		con.setAutoCommit(true);
		con.close();

		return result;
	}

	@Override
	public Member loginMember(String userId, String userPwd) throws NamingException, SQLException {
		Member loginMember = null;

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "select * from member m inner join uolodadedfile u on m.userImg = u.no where userid=? and userpwd= sha1(md5(?))";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, userId);
		pstmt.setString(2, userPwd);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			loginMember =  new Member(rs.getString("userId"), rs.getString("userPwd"), rs.getString("userEmail"),
					rs.getTimestamp("regusterDate"), rs.getInt("userImg"), rs.getInt("userpont"),
					rs.getString("newFileName"), rs.getString("isAdmin"));

		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return loginMember;
	}

	public boolean addPointToMember(String userId, String why, int howMuch, Connection con)
			throws NamingException, SQLException {
		boolean resultBool = false;

		String query = "update member set userPont = userpont + ? where userId = ?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, howMuch);
		pstmt.setString(2, userId);

		int result = pstmt.executeUpdate();

		pstmt.close();

		if (result == 1) {
			int afterPointLog = insertPointLog(userId, why, howMuch, con);

			if (afterPointLog == 1) {
				resultBool = true;
			}
		}

		return resultBool;
	}

	@Override
	public int addPointToMember(String userId, String why, int howMuch) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.getInstance().dbConnect();
		con.setAutoCommit(false);

		String query = "update member set userPont = userpont + ? where userId = ?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, howMuch);
		pstmt.setString(2, userId);

		result = pstmt.executeUpdate();

		pstmt.close();

		if (result == 1) {
			int afterPointLog = insertPointLog(userId, why, howMuch, con);

			if (afterPointLog == 1) {
				con.commit();
				result = 0;
			} else {
				con.rollback();
			}
		}

		con.setAutoCommit(true);
		con.close();

		return result;
	}

	@Override
	public Member getMemberInfo(String userId) throws NamingException, SQLException {
		Member memberInfo = null;

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "select m.*, u.newFileName " + "from member m inner join uolodadedfile u "
				+ "on m.userImg = u.no " + "where userid=?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, userId);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			memberInfo = new Member(rs.getString("userId"), rs.getString("userPwd"), rs.getString("userEmail"),
					rs.getTimestamp("regusterDate"), rs.getInt("userImg"), rs.getInt("userpont"),
					rs.getString("newFileName"), rs.getString("isAdmin"));

		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return memberInfo;
	}

	@Override
	public List<PointLog> getPointLog(String userId) throws NamingException, SQLException {
		List<PointLog> pl = new ArrayList<PointLog>();

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "select * from pointlog " + "where who = ?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, userId);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			pl.add(new PointLog(rs.getInt("id"), rs.getTimestamp("when"), rs.getString("why"), rs.getInt("howmuch"),
					rs.getString("who")));
		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return pl;
	}

	@Override
	public int modifyEmailInfo(String userId, String userEmailmodify) throws NamingException, SQLException { 
		int result = -1;

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "update member set userEmail = ? where userId = ? ";
		PreparedStatement pstmt = con.prepareStatement(query);

		pstmt.setString(1, userEmailmodify);
		pstmt.setString(2, userId);

		result = pstmt.executeUpdate();

		DBConnection.getInstance().dbClose(pstmt, con);

		return result;
		
	}

	@Override
	public int modifyImgnfo(String userId, String uploadFile) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "update member set userImg = ? where userId = ? ";
		PreparedStatement pstmt = con.prepareStatement(query);

		pstmt.setString(1, uploadFile);
		pstmt.setString(2, userId);

		result = pstmt.executeUpdate();

		DBConnection.getInstance().dbClose(pstmt, con);

		return result;
	}

	
	public List<PointLog> getPointLog(String userId, Paginginfo pi) throws NamingException, SQLException {
		List<PointLog> pl = new ArrayList<PointLog>();

		Connection con = DBConnection.getInstance().dbConnect();
		String query = "select * from pointlog where who = ? limit ?, ?";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, userId);
		pstmt.setInt(2, pi.getStartRowIndex());
		pstmt.setInt(3, pi.getViewPostCntPerPage());
		System.out.println(userId);
		System.out.println(pi.getStartRowIndex());
		System.out.println(pi.getViewPostCntPerPage());
		ResultSet rs = pstmt.executeQuery();
		
		
		while (rs.next()) {
			pl.add(new PointLog(rs.getInt("id"), rs.getTimestamp("when"), rs.getString("why"), rs.getInt("howmuch"),
					rs.getString("who")));
		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return pl;
	}

	@Override
	public int getTotalPostCnt(String userId) throws NamingException, SQLException {
		int result = -1;
		Connection con = DBConnection.getInstance().dbConnect();
		String query = "select count(*) as totalPostCnt from pointlog where who = ? ";
		
		PreparedStatement pstmt = con.prepareCall(query);
		pstmt.setString(1, userId);


		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			result =rs.getInt("totalPostCnt");

		}

		DBConnection.getInstance().dbClose(rs, pstmt, con);

		return result;
	}

}
