package com.ray.controller;

import com.ray.service.BoardService;
import com.ray.service.board.DeleteBoardService;
import com.ray.service.board.GetBoardByNoService;
import com.ray.service.board.GetEntireBoardService;
import com.ray.service.board.ReplyBoardService;
import com.ray.service.board.WriteBoardService;

public class BoardFactory {
	private static BoardFactory instance = null;
	
	private boolean isRedirect;
	private String whereisGo;
	
	
	
	private BoardFactory () { }
	
	public static BoardFactory getInstance() {
		if (instance == null) {
			instance = new BoardFactory();
		}
		
		return instance;
	}
	
	
	
	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getWhereisGo() {
		return whereisGo;
	}

	public void setWhereisGo(String whereisGo) {
		this.whereisGo = whereisGo;
	}

	public BoardService getService(String command) {
		BoardService service = null;
		
		if (command.equals("/board/listAll.bo")) {
			service = new GetEntireBoardService();
		} else if (command.equals("/board/writeBoard.bo")) {
			service = new WriteBoardService();
		} else if (command.equals("/board/viewBoard.bo")) {
			service = new GetBoardByNoService();
		} else if (command.equals("/board/delBoard.bo")) {
			service = new DeleteBoardService();
		}else if (command.equals("/board/reply.bo")) {
			service = new ReplyBoardService();
		}
		
		return service;
	}
 
}
