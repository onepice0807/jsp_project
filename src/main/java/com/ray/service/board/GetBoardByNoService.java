package com.ray.service.board;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.controller.BoardFactory;
import com.ray.dao.BoardCRUD;
import com.ray.etc.UploadedFile;
import com.ray.service.BoardService;
import com.ray.vo.Board;

public class GetBoardByNoService implements BoardService {

	@Override
	public BoardFactory doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int no = Integer.parseInt(request.getParameter("no"));

		// 클라이언트의 ip주소 얻어오기
		String userIp = getIp(request);

		com.ray.dao.BoardDAO dao = BoardCRUD.getInstance();

		int result = -1;
		try {
			if (dao.selectReadCountProcess(userIp, no)) {
				// 해당 아이피 주소와 글번호 같은 것이 있다면...
				if (dao.selectHourDiff(userIp, no) > 23) { // 시간이 24시간이 지난 경우

//					-> 아이피 주소와 글번호와 읽은시간을 readcountprocees테이블에 (update)
//					-> 해당 글번호의 readcount를 증가 (update)
//					 
					result = dao.readCountProcessWithReadCntInc(userIp, no, "update");
				}

			} else { // 최초 조회 (해당 아이피 주소와 글번호 같은 것이 없다면... )

//				-> 아이피 주소와 글번호와 읽은시간을 readcountprocees테이블에 (insert)
//				-> 해당 글번호의 readcount를 증가 (update)
				result = dao.readCountProcessWithReadCntInc(userIp, no, "insert");
			}

			Board board = dao.selectBoardByNo(no);
			UploadedFile attachFile = dao.getFile(no); 
			if (board != null) {
				request.setAttribute("board", board);
				if (attachFile != null) {
					request.setAttribute("uploadFile", attachFile);
				}
				
				request.getRequestDispatcher("viewBoard.jsp").forward(request, response);

			}

		} catch (NamingException | SQLException e) {
			e.printStackTrace();

			request.setAttribute("errorMsg", e.getMessage());
			request.setAttribute("errorStack", e.getStackTrace());

			RequestDispatcher rd = request.getRequestDispatcher("../commonError.jsp");
			rd.forward(request, response);

		}

		return null;
	}

	private String getIp(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");

		System.out.println(">>>> X-FORWARDED-FOR : " + ip);

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			System.out.println(">>>> Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
			System.out.println(">>>> WL-Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			System.out.println(">>>> HTTP_CLIENT_IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			System.out.println(">>>> HTTP_X_FORWARDED_FOR : " + ip);
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		System.out.println(">>>> Result : IP Address : " + ip);

		return ip;

	}

}
