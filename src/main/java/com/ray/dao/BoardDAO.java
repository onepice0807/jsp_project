package com.ray.dao;

import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.ray.etc.Paginginfo;
import com.ray.etc.UploadedFile;
import com.ray.vo.Board;
import com.ray.vo.ReadCountProcess;
import com.ray.vo.searchCritrtia;

public interface BoardDAO {

	// 게시판 전체 글 목록
	List<Board> selectAllBoard() throws NamingException, SQLException;
	
	// 게시판 전체 글 목록(페이징 처리 검색어가 없을떄)
	List<Board> selectAllBoard(Paginginfo pi) throws NamingException, SQLException;
	
	// 게시판 글 저장(업로드가 있을 경우)
	int insertBoardWithUploadFileTransaction(Board tmpBoard, UploadedFile uf) 
			throws NamingException, SQLException;
	
	// 게시판 글 저장(업로드가 없을 경우)
	int insertBoardTransaction(Board tmpBoard) throws NamingException, SQLException;
	
	
	// readCountProcess 테이블에 ip주소와 글번호가 있는지 없는지
	boolean selectReadCountProcess(String userIp, int no) throws NamingException, SQLException;
	
	// 24시간이 지났는지 지나지 않았는지 
	int selectHourDiff(String userIp, int no) throws NamingException, SQLException;
	
	// 아이피 주소와 글번호와 읽은시간을 readcountprocees테이블에 insert하거나 update함. 그리고 조회수증가
	int readCountProcessWithReadCntInc(String userIp, int no, String how) throws NamingException, SQLException;
	
	// 글번호로 해당 글 가져오기
	Board selectBoardByNo(int no) throws NamingException, SQLException;

	// 글번호로 해당 글에 저장된 파일 가져오기
	UploadedFile getFile(int no) throws NamingException, SQLException;

	// 글 번호 해당 글을 삭제 처리
	boolean deleteBoard(int boardNo) throws NamingException, SQLException;


	boolean inserReply(Board board) throws NamingException, SQLException;

	// 게시판 전체 글 수를 가져오는 (검색어가 없을때)
	int getTotalPostCnt()throws NamingException, SQLException;

	// 게시판 전체 글 수를 가져오는 (검색어가 있을때)
	int getTotalPostCnt(searchCritrtia sc)throws NamingException, SQLException;

	// 게시판 전체 글 목록(페이징 처리 검색어가 있을떄)
	List<Board> selectAllBoard(Paginginfo pi, searchCritrtia sc)throws NamingException, SQLException;
}



