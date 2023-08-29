package com.ray.service.board;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.controller.BoardFactory;
import com.ray.dao.BoardCRUD;
import com.ray.service.BoardService;
import com.ray.vo.Board;

public class ReplyBoardService implements BoardService {

	@Override
	public BoardFactory doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BoardFactory bf = BoardFactory.getInstance();
		
		int no = Integer.parseInt(request.getParameter("no"));
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		int ref = Integer.parseInt(request.getParameter("ref"));
		int step = Integer.parseInt(request.getParameter("step"));
		int reforder = Integer.parseInt(request.getParameter("reforder"));

		content = content.replace("\r\n", "<br />");
		
		com.ray.dao.BoardDAO dao = BoardCRUD.getInstance();
		Board tmp = new Board(no, writer, title, null, new StringBuilder(content), -1, -1, ref, step, reforder, null);
		
		System.out.println("저장되어야 할 답글" + tmp.toString());
		
		try {
			if(dao.inserReply(tmp)) {
				bf.setRedirect(true);
				bf.setWhereisGo("listAll.bo");
			}
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return bf;
	}

}
