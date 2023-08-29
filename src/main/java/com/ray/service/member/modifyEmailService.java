package com.ray.service.member;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ray.controller.MemberFactory;
import com.ray.dao.MemberCRUD;
import com.ray.service.MemberService;
import com.ray.vo.Member;
import com.ray.vo.PointLog;

public class modifyEmailService implements MemberService {

	@Override
	public MemberFactory executeService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String userId = request.getParameter("userId");
		String userEmailmodify = request.getParameter("userEmailmodify");

		System.out.println(userId);
		System.out.println(userEmailmodify);
		
		
		com.ray.dao.MemberDAO dao = MemberCRUD.getInstance();
		
		try {
			
			dao.modifyEmailInfo(userId, userEmailmodify);

			Member memberInfo = dao.getMemberInfo(userId);
			List<PointLog> lst = dao.getPointLog(userId);
			
			
			request.setAttribute("pointLog", lst);
			request.setAttribute("memberInfo", memberInfo);
			
			request.getRequestDispatcher("myPage.jsp").forward(request, response);
			
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
