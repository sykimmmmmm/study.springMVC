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
<div class="mt-4 mb-3">
	<form class="w-75" role="search">
		<div class="input-group">
			<select class="form-select" style="max-width:100px;" name="searchType">
				<option value="title" selected>제목</option>
				<option value="author">작성자</option>
			</select>
			<input class="form-control w-50" type="search" name="searchValue" placeholder="검색어를 입력해주세요" aria-label="searchValue" />
			<button class="btn btn-outline-success" type="submit">Search</button>
		</div>
	</form>
</div>
<table class="table table-hover table-striped w-100">
	<tr class="table-dark">
		<th class="text-center" style="width:5%">삭제 <br/> All <input type="checkbox" name="allDeleteBoard"></th>
		<th style="width:5%">번호</th>
		<th style="width:30%">제목</th>
		<th style="width:30%">작성자</th>
		<th>등록일시</th>
	</tr>
	<c:forEach items="${boardList}" var="board">
		<tr>
			<td class="text-center"><input type="checkbox" name="deleteBoardNos" value="${board.boardId}"/></td>
			<td>${board.boardId}</td>
			<td><a class="link-info link-opacity-50-hover link-underline link-underline-opacity-0" href="${CONTEXT_PATH}/cmmn/board/boardDetail.do?boardId=${board.boardId}">${board.boardTitle}</a></td>
			<td>${board.rgstId}</td>
			<td>${board.rgstDt}</td>
		</tr>
	</c:forEach>
</table>
<div class="mb-3 d-flex justify-content-end align-items-center gap-1">
	<button id="registBtn" class="btn btn-primary">등록</button>
	<button id="deleteBtn" class="btn btn-danger">삭제</button>
</div>
<form action="${CONTEXT_PATH}/cmmn/board/boardDelete.do" method="post" name="delForm">
</form>
</body>
<script type="text/javascript">
	$(function(){
		const $registBtn = $("#registBtn");
		const $deleteBtn = $("#deleteBtn");
		const $searchForm = $("form[name='searchForm']");
		const $delForm = $("form[name='delForm']");
		const $allDeleteBtn = $("input[name='allDeleteBoard']");
		const $deleteBoards = $("input[name='deleteBoardNos']");
		
		$registBtn.on("click",function(){
			location.href = "${CONTEXT_PATH}/cmmn/board/boardRegisterForm.do";
		})
		
		$allDeleteBtn.on("click",function(e){
			let flag = false;
			if($(this).prop("checked")){
				flag = true;
			}else{
				flag = false;
			}
			$deleteBoards.prop("checked",flag);
		})
		
		$deleteBoards.on("click",function(e){
			let totalLength = $deleteBoards.length;
			let checkedLength = $deleteBoards.filter(":checked").length;
			let checkedFlag = $allDeleteBtn.prop("checked");
			if($(this).prop("checked") && totalLength == checkedLength){
				$allDeleteBtn.prop("checked", checkedFlag ? checkedFlag : true )
			}else{
				$allDeleteBtn.prop("checked", checkedFlag ? false : checkedFlag );
			}
		})
		
		$deleteBtn.on("click",function(){
			$deleteBoards.each(function(i,v){
				if($(v).prop("checked")){
					const value = $(v).val();
					const inputDOM = $(`<input type='hidden' name='deleteBoardNos' value='\${value}'/>`)
					$delForm.append(inputDOM);
				}
			})
		
		if(confirm("정말로 삭제하시겠습니까?")){
			$delForm.submit();
		}else{
			$delForm.innerHTML = '';
		}
	})
		
	})

</script>
</html>