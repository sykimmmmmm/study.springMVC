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
	<div class="mt-4 mb-3">
		<form class="d-flex w-75" role="search">
			 <div class="input-group input-group-sm">
	           	<div class="input-group-text">아이디</div>
				<input class="form-control me-2" type="search" name="userId" placeholder="아이디를입력해주세요" aria-label="userId" />
			</div>
			 <div class="input-group input-group-sm">
	           	<div class="input-group-text">이름</div>
				<input class="form-control me-2" type="search" name="userNm" placeholder="이름을 입력해주세요" aria-label="userNm" />
			</div>
			<button class="btn btn-outline-success" type="submit">Search</button>
	    </form>
	</div>
	
	<table class="table table-striped table-hover">
		<tr class="table-dark">
			<th>번호</th>
			<th>아이디</th>
			<th>이름</th>
			<th>등록자</th>
			<th>등록일자</th>
		</tr>
		<c:forEach items="${userList}" varStatus="vs" var="user">
			<tr>
				<td>${vs.index + 1}</td>
				<td>${user.userId}</td>
				<td><a href="${ROOT_PATH}/cmmn/user/userDetail.do?userId=${user.userId}" class="link-info link-opacity-50-hover link-underline link-underline-opacity-0">${user.userNm}</a></td>
				<td>${user.rgstId}</td>
				<td>${user.rgstDt}</td>
			</tr>
		</c:forEach>
	</table>
</div>

</body>
<script type="text/javascript">
</script>
</html>