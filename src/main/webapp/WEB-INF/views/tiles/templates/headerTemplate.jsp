<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="Header">
<!-- 	<a href="${CONTEXT_PATH}/cmmn/sec/loginForm.do">로그인</a> -->
<!-- 	<a href="${CONTEXT_PATH}/cmmn/user/userRegisterForm.do">회원가입</a> -->
	
	<sec:authorize access="isAnonymous()">
		<a href="${CONTEXT_PATH}/cmmn/user/userRegisterForm.do">회원가입</a> |
		<a href="${CONTEXT_PATH}/cmmn/sec/loginForm.do">로그인</a> 
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<sec:authentication property="principal.userVO.userNm"/>님 안녕하세요 |
		<a href="${CONTEXT_PATH}/cmmn/user/userList.do">사용자목록</a> |
		<a href="${CONTEXT_PATH}/cmmn/board/boardList.do">게시글목록</a> |
		<a href="${CONTEXT_PATH}/cmmn/sec/logout.do">로그아웃</a> 
	</sec:authorize>
</div>