package com.ray.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.ray.etc.Paginginfo;
import com.ray.etc.Paginginfo_pointLog;
import com.ray.etc.UploadedFile;
import com.ray.vo.Member;
import com.ray.vo.PointLog;
import com.ray.vo.searchCritrtia;

public interface MemberDAO {
	// 유저아이디가 중복되는지 검사하는 
	Member duplicateUserId(String tmpUserId) throws NamingException, SQLException;
	
	// 업로드된 파일이 있는 경우 회원가입
	int registerMemberWithFile(UploadedFile uf, Member newMemeber, String why, int howmuch) 
			throws NamingException, SQLException;
	
	// 업로드된 파일이 없는 경우 회원가입
	int registerMember(Member newMemeber, String why, int howmuch) 
			throws NamingException, SQLException;
	
	
	// 업로드된 파일의 정보를 uploadedFile 테이블에 insert
	int insertUploadedFileInfo(Connection con, UploadedFile uf) throws NamingException, SQLException;
	
	// 회원정보를 insert
	int insertMember(Member newMemeber, Connection con) throws NamingException, SQLException;
	
	// pointlog 테이블에 회원가입 포인트 로그를 남겨야 함
	int insertPointLog(String userId, String why, int howmuch, Connection con) throws NamingException, SQLException;
	
	// 로그인
	Member loginMember(String userId, String userPwd)  throws NamingException, SQLException;
	
	// member 테이블에 포인트 가감 + 로그 남기기
	int addPointToMember(String userId, String why, int howMuch)  throws NamingException, SQLException;
	
	// 나의 페이지에 정보 가져오기
	Member getMemberInfo(String userId)  throws NamingException, SQLException;

	// 회원의 멤버포인트 내역 가져오기
	List<PointLog> getPointLog(String userId) throws NamingException, SQLException;
	
	// 회원의 멤버포인트 내역 가져오기(페이징
		List<PointLog> getPointLog(String userId, Paginginfo pi) throws NamingException, SQLException;
	
	
	// 게시판 전체 글 수를 가져오는 (검색어가 없을때)
	int getTotalPostCnt(String userId)throws NamingException, SQLException;
	
	// 이메일 수정
	int modifyEmailInfo(String userId, String userEmailmodify) throws NamingException, SQLException;
	
	// 이미지 수정
	int modifyImgnfo(String userId, String uf)throws NamingException, SQLException;


	
}
