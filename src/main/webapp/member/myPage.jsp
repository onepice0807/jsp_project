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

<style>
.userPhoto {
	display: flex;
}

.userPhoto img {
	width: 150px;
	height: 150px;
}

.pagination {
	display: flex;
	justify-content: center;
}

.paging {
	width: auto;
	text-align: center;
	margin: auto;
	padding: auto;
}
}
</style>
<script type="text/javascript">
	let MailValid = false;

	function modifyEmail(userEmail) {
		console.log("!");
		$("#modifyEmail").html(userEmail);
		$('#modifyEmailModal').show(500);
	}

	function modifyImg(userImg) {
		console.log("!");
		$("#modifyImg").html(userImg);
		$('#modifyImgModal').show(500);
	}

	$(function() {
		// 아이디 작성을 마쳤을때
		$('#userId').blur(function() {
			validUserId();
		});

		$('#userPwd2').blur(function() {
			validUserPwd();
		});

		// 이메일 인증 버튼 클릭시
		$('.sendMail').click(function() {
			if ($('#userEmail').val() != '') {
				// 이메일을 보내고
				$.ajax({
					url : 'sendMail.mem', // 데이터를 수신받을 서버 주소
					type : 'get', // 통신방식(GET, POST, PUT, DELETE)
					data : {
						"tmpUserEmail" : $('#userEmailmodify').val()
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						console.log(data);
						if (data.status == "success") {
							alert('메일을 발송했습니다');
						} else if (data.status == "fail") {
							alert('메일 발송 실패');
						}
					}
				});
				$('.codeDiv').show();
			} else {
				alert('이메일 주소를 기입하고 인증 버튼을 눌러주세요');
				$('#userEmail').focus();
			}

		});

		// 코드 확인 버튼 클릭시
		$('.confirmCode').click(function() {

			$.ajax({
				url : 'confirmCode.mem', // 데이터를 수신받을 서버 주소
				type : 'get', // 통신방식(GET, POST, PUT, DELETE)
				data : {
					"tmpMailCode" : $('#mailcode').val()
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					console.log(data);
					if (data.activation == "success") {
						MailValid = true;
						alert('메일 인증 성공!');
					}
				}
			});

		});

		// 이메일 수정 모달창 닫기 버튼 클릭시
		$('.modifyEmailModalClose').click(function() {
			$('#modifyEmailModal').hide();
		});

		// 이미지 수정 모달창 닫기 버튼 클릭시
		$('.modifyImgModalClose').click(function() {
			$('#modifyImgModal').hide();
		});

	});
</script>
</head>
<body>
	<c:if test="${sessionScope.loginUser == null }">
		<c:redirect url="../member/login.jsp"></c:redirect>
	</c:if>

	<jsp:include page="./../header.jsp"></jsp:include>

	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<div class="container">
		<h1>마이 페이지</h1>

		<div class="userInfo">
			<div class="mb-3 mt-3 userPhoto">
				<img src="${contextPath }/${requestScope.memberInfo.memberImg }" />

			</div>
			<div class="imgFormat">
				<button type="button" class="btn btn-info"
					onclick='modifyImg("${requestScope.memberInfo.userId }");'>이미지
					변경</button>
				<a href=''>이미지 초기화</a>
			</div>

			<div class="mb-3 mt-3">
				<label for="userId" class="form-label">UserId:</label> <input
					type="text" class="form-control" id="userId" name="userId"
					value="${requestScope.memberInfo.userId }" readonly>
			</div>


			<div class="mb-3">
				<label for="userEmail" class="form-label">Email:</label> <input
					type="text" class="form-control" id="userEmail" name="userEmail"
					value="${requestScope.memberInfo.userEmail }" readonly>

				<button type="button" class="btn btn-info"
					onclick='modifyEmail("${requestScope.memberInfo.userId }");'>이메일
					변경</button>
			</div>

		</div>

		<div class="pointLog">
			<table class="table table-striped">
				<thead>
					<tr>
						<th>적립일시</th>
						<th>적립사유</th>
						<th>적립포인트</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="point" items="${requestScope.pointLog }">
						<tr>
							<td>${point.when }</td>
							<td>${point.why }</td>
							<td>${point.howmuch }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>


	<div class="paging">
		${requestScope.PagingInfo }

		<ul class="pagination">
			<c:if test="${param.pageNo > 1}">
				<li class="page-item"><a class="page-link"
					href="myPage.mem?userId=${requestScope.memberInfo.userId }&pageNo=${param.pageNo -1 }">Previous</a></li>
			</c:if>
			<c:forEach var="i"
				begin="${requestScope.PagingInfo.startNumCurrentPagingBlock}"
				end="${requestScope.PagingInfo.endNumOfCurrentBlock}" step="1">
				<li class="page-item"><a class="page-link"
					href="myPage.mem?userId=${requestScope.memberInfo.userId }&pageNo=${i }">${i }</a></li>
			</c:forEach>

			<c:if test="${param.pageNo < requestScope.PagingInfo.totalPageCnt }">
				<li class="page-item"><a class="page-link btn"
					href="myPage.mem?userId=${requestScope.memberInfo.userId }&pageNo=${param.pageNo +1 }">Next</a></li>
			</c:if>
		</ul>

	</div>

	<jsp:include page="./../footer.jsp"></jsp:include>


	<!-- The 이메일 수정 Modal -->
	<form action="modifyEmail.mem" method="post">
		<div class="modal" id="modifyEmailModal">
			<div class="modal-dialog">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">이메일 변경</h4>
						<button type="button" class="btn-close modifyEmailModalClose"
							data-bs-dismiss="modal"></button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">
						<span id="modifyEmail"></span>이메일을 변경할까요?
						<div class="mb-3">
							<div></div>
							<label for="userEmailmodify" class="form-label">변경하실 이메일을
								작성해주세요!</label> <input type="text" class="form-control"
								id="userEmailmodify" name="userEmailmodify">

							<button type="button" class="btn btn-info sendMail">이메일인증</button>
							<div class='codeDiv' style="display: none;">
								<input type="text" class="form-control" id="mailcode"
									placeholder="이메일로 전송된 코드 입력...">
								<button type="button" class="btn btn-warning confirmCode">코드
									확인</button>

								<input type="hidden" name="userId"
									value="${sessionScope.loginUser.userId}" />

							</div>
						</div>
					</div>

					<!-- Modal footer -->
					<div class="modal-footer">
						<button type="submit" class="btn btn-success modifyEmailBtn">수정</button>

						<button type="button" class="btn btn-danger modifyEmailModalClose"
							data-bs-dismiss="modal">취소</button>

					</div>

				</div>
			</div>
		</div>
	</form>

	<!-- The 이미지 수정 Modal -->
	<form action="modifyImg.mem" method="post"
		enctype="multipart/form-data">
		<div class="modal" id="modifyImgModal">
			<div class="modal-dialog">
				<div class="modal-content">

					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">이미지 변경</h4>
						<button type="button" class="btn-close modifyImgModalClose"
							data-bs-dismiss="modal"></button>
					</div>

					<!-- Modal body -->
					<div class="modal-body">
						<span id="modifyImg"></span>이미지를 변경할까요?
						<div class="mb-3">
							<label for="userImgmodify" class="form-label">변경하실 이미지를
								선택하세요!</label> <input type="file" class="form-control"
								id="userImgmodify" name="userImgmodify"> <input
								type="hidden" name="userId"
								value="${sessionScope.loginUser.userId}" />
						</div>
					</div>


					<!-- Modal footer -->
					<div class="modal-footer">
						<button type="submit" class="btn btn-success modifyImgBtn">수정</button>
						<button type="button" class="btn btn-danger modifyImgModalClose"
							data-bs-dismiss="modal">취소</button>
					</div>
				</div>
			</div>
		</div>
		</div>
	</form>

</body>
</html>