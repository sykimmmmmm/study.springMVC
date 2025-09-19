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
<form name="delForm" action="${ROOT_PATH}/cmmn/user/userDelete.do" method="POST">
	<input type="hidden" name="userId" value="${userVO.userId}"/>
	<input type="hidden" name="userNm" value="${userVO.userNm}"/>
	<input type="hidden" name="fileGrpId" value="${userVO.fileGrpId}"/>
</form>
<c:if test="${not empty msg }">
	<div>
		<c:out value="${msg }"/>
	</div>
</c:if>
<table>
	<tr>
		<td>아이디</td>
		<td>${userVO.userId}</td>
	</tr>
	<tr>
		<td>비밀번호</td>
		<td>[protected]</td>
	</tr>
	<tr>
		<td>이름</td>
		<td>${userVO.userNm}</td>
	</tr>
	<tr>
		<td>권한</td>
		<td>
			<c:if test="${not empty userVO.authVOList}">
				<c:forEach items="${userVO.authVOList}" var="authVO">
					${authVO.authNm } 
				</c:forEach>
			</c:if>
		</td>
	</tr>
	<tr>
		<td>사진</td>
		<td>
			<c:if test="${not empty userVO.fileVO}">
				<c:set var="fileVO" value="${userVO.fileVO}"/>
				<div>
					<img alt="${fileVO.fileOriginNm}" src="${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}">
					<a href="${CONTEXT_PATH}/cmmn/file/download.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}">[다운로드]</a>
				</div>
			</c:if>
		</td>
	</tr>
</table>
<button id="updateBtn">수정</button>
<button id="deleteBtn">삭제</button>
<button id="listBtn">목록</button>

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