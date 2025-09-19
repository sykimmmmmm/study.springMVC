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
		<th width="5%">게시글 번호</th>
		<th width="35%">제목</th>
		<th width="20%">작성자</th>
		<th width="30%">등록일시</th>
		<th width="5%" style="text-align: center">삭제 <br/> All <input type="checkbox" name="allDeleteBoard">  </th>
	</tr>
	<c:forEach items="${boardList}" var="board">
		<tr>
			<td>${board.boardId}</td>
			<td><a href="${CONTEXT_PATH}/cmmn/board/boardDetail.do?boardId=${board.boardId}">${board.boardTitle}</a></td>
			<td>${board.rgstId}</td>
			<td>${board.rgstDt}</td>
			<td style="text-align: center"><input type="checkbox" name="deleteBoardNos" value="${board.boardId}"/></td>
		</tr>
	</c:forEach>
</table>
<button id="registBtn">등록</button>
<button id="deleteBtn">삭제</button>
<form action="${CONTEXT_PATH}/cmmn/board/boardDelete.do" method="post" name="delForm">
	
</form>
</body>
<script type="text/javascript">
	const registBtn = document.getElementById("registBtn");
	const deleteBtn = document.getElementById("deleteBtn");
	const delForm = document.delForm;
	const allDeleteBtn = document.querySelector("input[name='allDeleteBoard']");
	const deleteBoards = document.querySelectorAll("input[name='deleteBoardNos']");
	
	registBtn.addEventListener("click",function(){
		window.location.href="${CONTEXT_PATH}/cmmn/board/boardRegisterForm.do";
	})
	
	allDeleteBtn.addEventListener("click",function(e){
		let flag = false;
		if(e.target.checked){
			flag = true;
		}else{
			flag = false;
		}
		for(const deleteBoard of deleteBoards){
			deleteBoard.checked = flag;
		}
	})
	for(const deleteBoard of deleteBoards){
		deleteBoard.addEventListener("click",function(e){
			let totalLength = deleteBoards.length;
			let checkedLength = document.querySelectorAll("input[name='deleteBoardNos']:checked").length;
			
			if(e.target.checked && totalLength == checkedLength){
				allDeleteBtn.checked = allDeleteBtn.checked ? allDeleteBtn.checked : true;
			}else{
				allDeleteBtn.checked = allDeleteBtn.checked ? false : allDeleteBtn.checked;
			}
		})
	}
	
	deleteBtn.addEventListener("click",function(){
		for(const deleteBoard of deleteBoards){
			if(deleteBoard.checked){
				console.log(deleteBoard.value, "checked");
				const inputDOM = document.createElement("input");
				inputDOM.type = "hidden";
				inputDOM.name = "deleteBoardNos"
				inputDOM.value = deleteBoard.value;
				delForm.append(inputDOM);
			}else{
				console.log(deleteBoard.value, "no checked");
			}
		}
		
		if(confirm("정말로 삭제하시겠습니까?")){
			delForm.submit();
		}else{
			delForm.innerHTML = '';
		}
	})
</script>
</html>