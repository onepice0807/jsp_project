<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="./js/commonjs.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<title>로그인창</title>
</head>
<body>
	<jsp:include page="./../header.jsp"></jsp:include>

	<div class="container">
		<h1>로그인</h1>

		<div>아이디 : abcd123, 비밀번호 : abcd123!</div>
		<form action="./../loginprcEx2.do" method="post">

			<div>
				아이디 : <input type="text" name="userId" />
			</div>
			<div>
				비밀번호 : <input type="password" name="userPwd" />
			</div>
			<div>
				<button type="reset">취소</button>
				<button type="submit">로그인</button>
			</div>

		</form>


	</div>

	<jsp:include page="./../footer.jsp"></jsp:include>

</body>
</html>