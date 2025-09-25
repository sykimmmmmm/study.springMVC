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
</script>
</head>
<body class="login-page bg-body-secondary">
	<div class="login-box">
		<div class="card">
	        <div class="card-body login-card-body">
	          <p class="login-box-msg">Sign in to start your session</p>
	          <form action="${CONTEXT_PATH}/cmmn/sec/loginProc.do" method="post">
	            <div class="input-group mb-3">
	              <div class="input-group-text"><span class="bi bi-envelope"></span></div>
	              <input type="text" class="form-control" name="userId" placeholder="아이디" required/>
	            </div>
	            <div class="input-group mb-3">
	              <div class="input-group-text"><span class="bi bi-lock-fill"></span></div>
	              <input type="password" class="form-control" name="userPw" placeholder="비밀번호" required/>
	            </div>
	            <div class="col">
	              <div class="d-grid gap-2">
	                <button type="submit" class="btn btn-primary">로그인</button>
	              </div>
	            </div>
	          </form>
	        </div>
	        <!-- /.login-card-body -->
      	</div>
    </div>
</body>
</html>