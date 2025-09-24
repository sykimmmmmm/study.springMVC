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
	<form action="" method="get" name="searchForm">
		<select name="searchType">
			<option value="title">제목</option>
			<option value="author">작성자</option>
		</select>
		<input type="text" name="searchValue" placeholder="검색어를 입력해주세요...">
		<input type="submit" value="검색"/>
	</form>
</div>
<table>
	<tr>
		<th>삭제 <br/> All <input type="checkbox" name="allDeleteBoard">  </th>
		<th>게시글 번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>등록일시</th>
	</tr>
	<c:forEach items="${boardList}" var="board">
		<tr>
			<td style="text-align: center"><input type="checkbox" name="deleteBoardNos" value="${board.boardId}"/></td>
			<td>${board.boardId}</td>
			<td><a href="${CONTEXT_PATH}/cmmn/board/boardDetail.do?boardId=${board.boardId}">${board.boardTitle}</a></td>
			<td>${board.rgstId}</td>
			<td>${board.rgstDt}</td>
		</tr>
	</c:forEach>
</table>
<button id="registBtn">등록</button>
<button id="deleteBtn">삭제</button>
<form action="${CONTEXT_PATH}/cmmn/board/boardDelete.do" method="post" name="delForm">
</form>
</body>
<script type="text/javascript">
	$(function(){
		const registBtn = $("#registBtn");
		const deleteBtn = $("#deleteBtn");
		const searchForm = $("form[name='searchForm']");
		const delForm = $("form[name='delForm']");
		const allDeleteBtn = $("input[name='allDeleteBoard']");
		const deleteBoards = $("input[name='deleteBoardNos']");
		const table = $("table");
		const trs = $("table tr");
		
		/* css */
		table.css("width","600px").css("border","1px solid black").css("borderCollapse","collapse");
		trs.filter(":odd").css("background","#e7f790");
		trs.filter(":even").css("background","#68bda6");
		trs.eq(0).css("background","#333").css("color","white");
		$("tr td").filter(function(i){
			return i % 5 == 1 || i % 5 == 0
		}).css("text-align","center");
		$("table a").css("color","black").css("textDecoration","none");
		$("table a").hover(function(){
			$(this).css("color","skyblue");
		},function(){
			$(this).css("color","black");
		})
		/* css */
		
		
		registBtn.on("click",function(){
			location.href = "${CONTEXT_PATH}/cmmn/board/boardRegisterForm.do";
		})
		
		allDeleteBtn.on("click",function(e){
			let flag = false;
			if($(this).prop("checked")){
				flag = true;
			}else{
				flag = false;
			}
			deleteBoards.prop("checked",flag);
		})
		deleteBoards.on("click",function(e){
			let totalLength = deleteBoards.length;
			let checkedLength = deleteBoards.filter(":checked").length;

			if($(this).prop("checked") && totalLength == checkedLength){
				allDeleteBtn.prop("checked",allDeleteBtn.prop("checked") ? allDeleteBtn.prop("checked") : true)
			}else{
				allDeleteBtn.prop("checked",allDeleteBtn.prop("checked") ? false : allDeleteBtn.prop("checked"));
			}
		})
		
		deleteBtn.on("click",function(){
			deleteBoards.each(function(i,v){
				if($(v).prop("checked")){
					const value = $(v).val();
					const inputDOM = $(`<input type='hidden' name='deleteBoardNos' value='\${value}'/>`)
					delForm.append(inputDOM);
				}else{
					console.log($(v).val(),"no checked");
				}
			})
		
		if(confirm("정말로 삭제하시겠습니까?")){
			delForm.submit();
		}else{
			delForm.innerHTML = '';
		}
	})
		
	})
// 	const registBtn = document.getElementById("registBtn");
// 	const deleteBtn = document.getElementById("deleteBtn");
// 	const delForm = document.delForm;
// 	const allDeleteBtn = document.querySelector("input[name='allDeleteBoard']");
// 	const deleteBoards = document.querySelectorAll("input[name='deleteBoardNos']");
	
// 	registBtn.addEventListener("click",function(){
// 		window.location.href="${CONTEXT_PATH}/cmmn/board/boardRegisterForm.do";
// 	})
	
// 	allDeleteBtn.addEventListener("click",function(e){
// 		let flag = false;
// 		if(e.target.checked){
// 			flag = true;
// 		}else{
// 			flag = false;
// 		}
// 		for(const deleteBoard of deleteBoards){
// 			deleteBoard.checked = flag;
// 		}
// 	})
// 	for(const deleteBoard of deleteBoards){
// 		deleteBoard.addEventListener("click",function(e){
// 			let totalLength = deleteBoards.length;
// 			let checkedLength = document.querySelectorAll("input[name='deleteBoardNos']:checked").length;
			
// 			if(e.target.checked && totalLength == checkedLength){
// 				allDeleteBtn.checked = allDeleteBtn.checked ? allDeleteBtn.checked : true;
// 			}else{
// 				allDeleteBtn.checked = allDeleteBtn.checked ? false : allDeleteBtn.checked;
// 			}
// 		})
// 	}
	
// 	deleteBtn.addEventListener("click",function(){
// 		for(const deleteBoard of deleteBoards){
// 			if(deleteBoard.checked){
// 				console.log(deleteBoard.value, "checked");
// 				const inputDOM = document.createElement("input");
// 				inputDOM.type = "hidden";
// 				inputDOM.name = "deleteBoardNos"
// 				inputDOM.value = deleteBoard.value;
// 				delForm.append(inputDOM);
// 			}else{
// 				console.log(deleteBoard.value, "no checked");
// 			}
// 		}
		
// 		if(confirm("정말로 삭제하시겠습니까?")){
// 			delForm.submit();
// 		}else{
// 			delForm.innerHTML = '';
// 		}
// 	})
</script>
</html>