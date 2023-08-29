package com.ray.service.member;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.controller.MemberFactory;
import com.ray.dao.MemberCRUD;
import com.ray.etc.Paginginfo;
import com.ray.service.MemberService;
import com.ray.vo.Member;
import com.ray.vo.PointLog;

public class MyPageService implements MemberService {

	@Override
	public MemberFactory executeService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int pageNo = 1;
		if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("")) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		
		String userId = request.getParameter("userId");
		System.out.println(userId);
		
		com.ray.dao.MemberDAO dao = MemberCRUD.getInstance();
		
		try {
			Paginginfo pi = PagingProccess(pageNo, userId);
			System.out.println("페이지 처리 정보" + pi.toString());
			Member memberInfo = dao.getMemberInfo(userId);
			
			List<PointLog> lst = dao.getPointLog(userId, pi);
			
			request.setAttribute("pointLog", lst);
			request.setAttribute("memberInfo", memberInfo);
			request.setAttribute("PagingInfo", pi);
			request.setAttribute("userId", userId);
			
			request.getRequestDispatcher("myPage.jsp").forward(request, response);
			
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}



	private Paginginfo PagingProccess(int pageNo, String userId)  throws NamingException, SQLException {
		Paginginfo pi = new Paginginfo();
		com.ray.dao.MemberDAO dao = MemberCRUD.getInstance();
		
		pi.setPageNo(pageNo);
		
		pi.setTotalPostCnt(dao.getTotalPostCnt(userId));

		// 총페이지수
		pi.setTotalPostCnt(pi.getTotalPostCnt(), pi.getViewPostCntPerPage());

		// 보여주기 시작할 글 Index 번호
		pi.setStartRowIndex();

		// -------------------------- 페이징 블럭 처리를 위한 코드 --------------------------

		// 전체 페이징 갯수
		pi.setTotalPagingBlockCnt();

		// 현재 페이지가 속한 페이징 블럭 번호
		pi.setPageBlockOfCurrentPage();

		// 현재 페이징 블럭 시작페이지 번호
		pi.setEndNumOfCurrentBlock();

		// 현재 페이징 블럭 끝 페이지 번호
		pi.setStartNumCurrentPagingBlock();

		return pi;
	}

}
