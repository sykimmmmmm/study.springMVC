<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
보드 리스트입니다
<div> 
	<form action="" method="get">
		<select name="searchType">
			<option value="title">제목</option>
			<option value="author">작성자</option>
		</select>
		<input type="text" name="searchValue" placeholder="검색어를 입력해주세요...">
		<input type="submit" value="검색"/>
	</form>
</div>
<table border="1">
	<tr>
		<th width="10%">게시글 번호</th>
		<th width="30%">제목</th>
		<th width="20%">작성자</th>
		<th width="30%">등록일시</th>
		<th width="10%"></th>
	</tr>
	<c:forEach items="${boardList}" var="board">
		<tr>
			<td>${board.boardId}</td>
			<td><a href="${CONTEXT_PATH}/cmmn/board/boardDetail.do?boardId=${board.boardId}">${board.boardTitle}</a></td>
			<td>${board.rgstId}</td>
			<td>${board.rgstDt}</td>
			<td><input/> </td>
		</tr>
	</c:forEach>
</table>
<button id="registBtn">등록</button>
</body>
<script type="text/javascript">
	const registBtn = document.getElementById("registBtn");
	
	registBtn.addEventListener("click",function(){
		window.location.href="${CONTEXT_PATH}/cmmn/board/boardRegisterForm.do";
	})
</script>
</html>