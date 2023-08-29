package com.ray.service.member;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ray.controller.MemberFactory;
import com.ray.dao.MemberCRUD;
import com.ray.service.MemberService;
import com.ray.vo.Member;

public class LoginMemberService implements MemberService {

	@Override
	public MemberFactory executeService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		MemberFactory mf = MemberFactory.getInstance();
		
		com.ray.dao.MemberDAO dao = MemberCRUD.getInstance();
		
		try {
			Member loginMember = dao.loginMember(userId, userPwd);
			
			if (loginMember != null) {  // 로그인 성공
				// member 테이블 update + 포인트 로그 남기기
				dao.addPointToMember(userId, "로그인", 5);
				loginMember.setUserPoint(loginMember.getUserPoint() + 5);
				
				HttpSession ses = request.getSession();
				ses.setAttribute("loginUser", loginMember); // 세션에 바인딩
							
				request.getRequestDispatcher("../index.jsp").forward(request, response);
				
			} else { // 로그인 실패
				mf.setRedirect(true);
				mf.setWhereIsGo("./login.jsp?status=fail");
				
			}
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			request.setAttribute("errorMsg", e.getMessage());
			request.setAttribute("errorStack", e.getStackTrace());
			
			RequestDispatcher rd = request.getRequestDispatcher("../commonError.jsp");
					rd.forward(request, response);
		}
		
		
		return mf;
	}

}
