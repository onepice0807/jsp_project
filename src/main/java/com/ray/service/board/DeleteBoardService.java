package com.ray.service.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.ray.controller.BoardFactory;
import com.ray.dao.BoardCRUD;
import com.ray.service.BoardService;

public class DeleteBoardService implements BoardService {

	@Override
	public BoardFactory doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));
		com.ray.dao.BoardDAO dao = BoardCRUD.getInstance();
		
		response.setContentType("application/json; charset=utf-8;");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try {
			if(dao.deleteBoard(boardNo))  {
				json.put("status", "success");
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			
			json.put("status", "fail");
			json.put("errMsg", e.getMessage());
		}
		
		out.print(json.toJSONString());
		
		out.flush();
		out.close();
		
		return null;
	}

}
