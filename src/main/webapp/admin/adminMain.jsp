<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



	<c:if
		test="${sessionScope.loginUser.isAdmin != 'Y' or sessionScope.loginUser == null}">
		<c:redirect url="../member/login.jsp"></c:redirect>
	</c:if>

	<h1>게시판 관리자 페이지</h1>

	<p>관리자님 환영합니다.</p>

	<a href="boardList.jsp">게시판 목록</a>
	<a href="boardWrite.jsp">게시글 작성</a>
	<a href="boardMember.jsp">회원 관리</a>

	<hr>

	<h2>게시판 목록</h2>

	<table border="1">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회수</th>
			<th>작성일</th>
		</tr>

		<c:forEach var="board" items="${boards}">
			<tr>
				<td>${board.no}</td>
				<td><a href="boardView.jsp?no=${board.no}">${board.title}</a></td>
				<td>${board.writer}</td>
				<td>${board.viewCount}</td>
				<td>${board.regDate}</td>
			</tr>
		</c:forEach>

	</table>

</body>
</html>