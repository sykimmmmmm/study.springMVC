<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:if test="${not empty loginFailMsg}"></c:if>
<script type="text/javascript">
	if ("${loginFailMsg}" != '') {
		alert("${loginFailMsg}");
	}

	document.addEventListener("DOMContentLoaded",function(){
		const loginForm = document.loginForm;
		const userIdDom = loginForm.userId;
		const userPwDom = loginForm.userPw;
		const loginBtn = document.getElementById("loginBtn");
		
		loginBtn.addEventListener("click",function(){
			let userId = userIdDom.value;
			let userPw = userPwDom.value;
			
			if(isEmpty(userId)){
				alert("아이디를 입력하세요");
				userIdDom.focus();
				return false;
			}
			if(isEmpty(userPw)){
				alert("비밀번호를 입력하세요");
				userPwDom.focus();
				return false;
			}
			
			loginForm.submit();
		})
		
		function isEmpty(str){
			return str == null || str.trim() == ''
		}
		
	})
</script>
</head>
<body>
	<form action="${CONTEXT_PATH}/cmmn/sec/loginProc.do" method="post" name="loginForm">
		<table>
			<tr>
				<td>아이디 : </td>
				<td> <input type="text" name="userId"/> </td>
			</tr>
			<tr>
				<td>비밀번호 : </td>
				<td> <input type="password" name="userPw"/> </td>
			</tr>
		</table>
		<button type="button" id="loginBtn">로그인</button>
	</form>
</body>
</html>