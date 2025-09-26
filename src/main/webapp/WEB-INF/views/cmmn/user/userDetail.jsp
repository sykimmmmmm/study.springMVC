<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set scope="application" var="ROOT_PATH" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<form name="delForm" action="${ROOT_PATH}/cmmn/user/userDelete.do" method="POST">
		<input type="hidden" name="userId" value="${userVO.userId}"/>
		<input type="hidden" name="userNm" value="${userVO.userNm}"/>
		<input type="hidden" name="fileGrpId" value="${userVO.fileGrpId}"/>
	</form>
	<div class="card w-50">
		<c:set var="imgSrc" value="${RESOURCES_PATH}/dist/adminlte/dist/assets/img/avatar.png"/>
		<c:set var="imgAlt" value="이미지 없음"/>
		<c:if test="${not empty userVO.fileVO}">
			<c:set var="fileVO" value="${userVO.fileVO}"/>
			<c:set var="imgSrc" value="${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}"/>
			<c:set var="imgAlt" value="${fileVO.fileOriginNm}"/>
		</c:if>
		<div class="text-center">
			<a href="${CONTEXT_PATH}/cmmn/file/download.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}" style="cursor: pointer;">
				<img src="${imgSrc}" class="card-img-top" alt="${imgAlt}" style="max-width:250px; max-height: 250px; object-fit:cover;">
			</a>
		</div>
		<ul class="list-group list-group-flush">
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						아이디
					</div>
					<div class="col">
						${userVO.userId}
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						비밀번호
					</div>
					<div class="col">
						[protected]
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						이름
					</div>
					<div class="col">
						${userVO.userNm}
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						권한
					</div>
					<div class="col">
						<c:if test="${not empty userVO.authVOList}">
							<c:forEach items="${userVO.authVOList}" var="authVO">
								<span class="badge text-bg-info">${authVO.authNm}</span> 
							</c:forEach>
						</c:if>
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						등록자
					</div>
					<div class="col">
						${userVO.rgstId }
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						등록일시
					</div>
					<div class="col">
						${userVO.rgstDt }
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						수정자
					</div>
					<div class="col">
						${userVO.updtId }
					</div>
				</div>
			</li>
			<li class="list-group-item">
				<div class="row">
					<div class="col">
						수정일시
					</div>
					<div class="col">
						${userVO.updtDt}
					</div>
				</div>
			</li>
		</ul>
		<div class="card-body d-flex align-items-center justify-content-end gap-1">
			<button id="updateBtn" class="btn btn-sm btn-warning" >수정</button>
			<button id="deleteBtn" class="btn btn-sm btn-danger">삭제</button>
			<button id="listBtn" class="btn btn-sm btn-secondary">목록</button>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
	const delForm = document.delForm
	const updateBtn = document.getElementById("updateBtn");
	const deleteBtn = document.getElementById("deleteBtn");
	const listBtn = document.getElementById("listBtn");
	
	updateBtn.addEventListener("click",function(){
		window.location.href="${ROOT_PATH}/cmmn/user/userUpdateForm.do?userId=${userVO.userId}";
	})
	
	deleteBtn.addEventListener("click",function(){
		if(confirm("정말로 삭제하시겠습니까?")){
			delForm.submit();
		}
	})
	
	listBtn.addEventListener("click",function(){
		window.location.href="${ROOT_PATH}/cmmn/user/userList.do";
	})
</script>
</html>