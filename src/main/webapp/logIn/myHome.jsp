<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>myHome.jsp</h1>

	<a href="./Login.jsp">로그인</a>

		<%
			// 로그인 했을 경우에만 화면에 출력
			if(session.getAttribute("loginMember") != null) {
				out.print("<div>" + (String)session.getAttribute("loginMember") + "님 환영합니다</div>");
				out.print("<form action='./logout.do'><div><button type='submit'>로그아웃</button></div></form>");
			}
			%>



</body>
</html>