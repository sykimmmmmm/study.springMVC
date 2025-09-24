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

<div>
 	아이디와 이름을 통한 검색기능

	<form action="">
		ID <input name="userId"/>
		이름 <input name="userNm"/>
		<input type="submit" value="검색" />
	</form>
</div>

<table>
	<tr>
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
			<td><a href="${ROOT_PATH}/cmmn/user/userDetail.do?userId=${user.userId}">${user.userNm}</a></td>
			<td>${user.rgstId}</td>
			<td>${user.rgstDt}</td>
		</tr>
	</c:forEach>
</table>

</body>
<script type="text/javascript">
$(function(){
	$("table").css("border","1px solid black").css("borderCollapse","collapse").css("width","500px");
	$("td").filter(function(i){
		return i % 5 == 0
	}).css("text-align","center");
	$("tr:odd td").css("borderLeft","1px solid black")
	$("tr:even td").css("borderLeft","1px solid black")
	$("tr").filter(":odd").css("background","skyblue");
	$("tr").filter(":even").css("background","pink");
	$("tr").eq(0).css("background","#555").css("color","white");
})
</script>
</html>