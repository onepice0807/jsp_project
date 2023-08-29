package com.ray.service.board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ray.controller.BoardFactory;
import com.ray.dao.BoardCRUD;
import com.ray.dao.BoardDAO;
import com.ray.etc.Paginginfo;
import com.ray.service.BoardService;
import com.ray.vo.Board;
import com.ray.vo.searchCritrtia;

public class GetEntireBoardService implements BoardService {

	@Override
	public BoardFactory doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// System.out.println("게시판 전체 목록을 가져오자!");
		int pageNo = 1;
		if (request.getParameter("pageNo") != null && !request.getParameter("pageNo").equals("")) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}

		// 검색에 대한 처리
		String searchType = "";
		String searchWord = "";
		if (request.getParameter("searchType") != null && !request.getParameter("searchType").equals("")) {
			searchType = request.getParameter("searchType");
		}

		if (request.getParameter("searchWord") != null && !request.getParameter("searchWord").equals("")) {
			searchWord = request.getParameter("searchWord");
		}

		searchCritrtia sc = new searchCritrtia(searchWord, searchType);

		// System.out.println(pageNo + "페이지 글을 출력하자");
		try {
			Paginginfo pi = PagingProccess(pageNo, sc);
			System.out.println("페이지 처리 정보" + pi.toString());

			com.ray.dao.BoardDAO dao = BoardCRUD.getInstance();
			
			List<Board> lst = null;

			if (sc.getSearchType().equals("") && sc.getSearchWord().equals("")) {
				// 검색어가 없다
				 lst = dao.selectAllBoard(pi);
			} else if (!sc.getSearchType().equals("") && !sc.getSearchWord().equals("")) {
				// 검색어가 있다
				lst = dao.selectAllBoard(pi, sc);
			}

			if (lst.size() == 0) {
				request.setAttribute("boardList", null);
			} else {
				request.setAttribute("boardList", lst);
				request.setAttribute("PagingInfo", pi);
			}

			request.getRequestDispatcher("listAll.jsp").forward(request, response);

		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Paginginfo PagingProccess(int pageNo, searchCritrtia sc) throws NamingException, SQLException {
		Paginginfo pi = new Paginginfo();

		BoardDAO dao = BoardCRUD.getInstance();

		// 현재 페이지
		pi.setPageNo(pageNo);

		// 전체 게시판의 글 수 (검색어가 없을때)
		// 검색어가 있다면 검색된 글의 갯수
		if (sc.getSearchType().equals("") && sc.getSearchWord().equals("")) {
			// 검색어가 없다
			pi.setTotalPostCnt(dao.getTotalPostCnt());
		} else if (!sc.getSearchType().equals("") && !sc.getSearchWord().equals("")) {
			// 검색어가 있다
			pi.setTotalPostCnt(dao.getTotalPostCnt(sc));
		}

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
