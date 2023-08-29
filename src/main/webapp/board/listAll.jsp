<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<style type="text/css">
</style>
<script>
	$(function (){
		$('.board').each(function(){
			let postDate = new Date($(this).children().eq(3).html());
			let curDate = new Date();
			
			let title = $(this).children().eq(1).html();
			
			let diff = (curDate - postDate) / 1000 / 60 / 60;
			if (diff < 4) {
				let output = "<span><img src='./../images/new.png' /></span>";
				$(this).children().eq(1).html(output + title);
			}
			
		});
	});
	
	
</script>
<style>
.delBoard td {
	color: #333;
	text-decoration: line-through;
}

.pagination {
	display: flex;
	justify-content: center;
}

.searchArea {
	display: flex;
	justify-content: flex-end;
}

.btns {
	display: flex;
	justify-content: flex-start;
}
</style>
</head>
<body>

	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<jsp:include page="../header.jsp"></jsp:include>

	<div class="container">
		<h1>게시판 전체 목록 조회</h1>


		<div class="boardList">
			<c:choose>
				<c:when test="${boardList != null}">

					<table class="table table-hover">
						<thead>
							<tr>
								<th>글 번호</th>
								<th>제 목</th>
								<th>작성자</th>
								<th>작성일</th>
								<th>조회수</th>
								<th>좋아요</th>
								<th>ref</th>
								<th>step</th>
								<th>reforder</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach var="board" items="${boardList }">
								<c:choose>
									<c:when test="${board.isDelete == 'N' }">
										<tr id=`board${board.no }` class="board"
											onclick='location.href="viewBoard.bo?no=${board.no}";'>
											<td>${board.no }</td>

											<td><c:if test="${board.step > 0 }">
													<c:forEach var="i" begin="1" end="${board.step}" step="1">
														<img alt="" src="${contextPath }/images/arrowright.png">

													</c:forEach>
												</c:if> ${board.title }</td>
											<td>${board.writer }</td>
											<td class='postDate'>${board.postDate }</td>
											<td>${board.readcount }</td>
											<td>${board.likecount }</td>
											<td>${board.ref }</td>
											<td>${board.step }</td>
											<td>${board.reforder }</td>
										</tr>
									</c:when>
									<c:otherwise>

										<tr id=`board${board.no }` class="board delBoard">
											<td>${board.no }</td>
											<td>${board.title }</td>
											<td>${board.writer }</td>
											<td class='postDate'>${board.postDate }</td>
											<td>${board.readcount }</td>
											<td>${board.likecount }</td>
											<td>${board.ref }</td>
											<td>${board.step }</td>
											<td>${board.reforder }</td>
										</tr>

									</c:otherwise>
								</c:choose>

							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
				텅~!
			</c:otherwise>
			</c:choose>
		</div>
		<div>

			<div class="btns">
				<button type="button" class="btn btn-primary"
					onclick="location.href='writeBoard.jsp';">글쓰기</button>
			</div>
			<div class="paging">
				${requestScope.PagingInfo }

				<ul class="pagination">
					<c:if
						test="${param.pageNo > 1}&searchType=${param.searchType}&searchWord=${param.searchWord}">
						<li class="page-item"><a class="page-link"
							href="listAll.bo?pageNo=${param.pageNo -1 }">Previous</a></li>
					</c:if>
					<c:forEach var="i"
						begin="${requestScope.PagingInfo.startNumCurrentPagingBlock}"
						end="${requestScope.PagingInfo.endNumOfCurrentBlock}" step="1">
						<li class="page-item"><a class="page-link"
							href="listAll.bo?pageNo=${i }&searchType=${param.searchType}&searchWord=${param.searchWord}"">${i }</a></li>
					</c:forEach>

					<c:if
						test="${param.pageNo < requestScope.PagingInfo.totalPageCnt }">
						<li class="page-item"><a class="page-link btn"
							href="listAll.bo?pageNo=${param.pageNo +1 }&searchType=${param.searchType}&searchWord=${param.searchWord}">Next</a></li>
					</c:if>
				</ul>

			</div>

			<form class="searchArea" action="listAll.bo" method="get">
				<select name="searchType">
					<option value="">검색조건을 선택하세요</option>
					<option value="writer">작성자</option>
					<option value="title">제목</option>
					<option value="content">본문</option>
				</select> <input type="text" name="searchWord" id="searchWord" />
				<button type="submit" class="btn btn-info">검색</button>
			</form>




		</div>
	</div>

	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>