package com.ray.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.controller.BoardFactory;

public interface BoardService {
	BoardFactory doAction(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException;
}
