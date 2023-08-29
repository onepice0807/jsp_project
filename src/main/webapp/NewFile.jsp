<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- include libraries(jQuery, bootstrap) -->
    <script type="text/javascript" src="//code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" />
    <script type="text/javascript" src="cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>

    <link href="../css/summernote-bs5.css" rel="stylesheet">
    <script src="../js/summernote-bs5.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('.summernote').summernote();
	});
</script>
</head>
<body>
	<div class="summernote">summernote 1</div>
	<div class="summernote">summernote 2</div>
	<img alt="" src="">
</body>
</html>