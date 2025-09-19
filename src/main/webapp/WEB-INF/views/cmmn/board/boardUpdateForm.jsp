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

<c:if test="${not empty msg }">
	<div>
		<c:out value="${msg }"/>
	</div>
</c:if>
<form action="${ROOT_PATH}/cmmn/board/boardUpdate.do" method="POST" enctype="multipart/form-data" name="updateForm">
	<input type="hidden" name="boardId" value="${boardVO.boardId}">
	<input type="hidden" name="rgstId" value="${boardVO.rgstId}">
	<input type="hidden" name="fileGrpId" value="${boardVO.fileGrpId != null ? boardVO.fileGrpId : ''}">
<table>

	<tr>
		<td>제목</td>
		<td> <input type="text" name="boardTitle" value="${boardVO.boardTitle}"/></td>
	</tr>
	<tr>
		<td>작성자</td>
		<td>${boardVO.rgstId} </td>
	</tr>
	<tr>
		<td>내용</td>
		<td><textarea rows="20" cols="50" name="boardContent">${boardVO.boardContent}</textarea></td>
	</tr>
	<tr>
		<td>파일</td>
		<td>
			<c:if test="${not empty boardVO.fileList}">
				<c:forEach items="${boardVO.fileList}" var="fileVO">
					<div data-file-no="${fileVO.fileNo}">
						<span>${fileVO.fileOriginNm}</span>
						<span class="fileDeleteBtn">X</span>
					</div>
				</c:forEach>
			</c:if>
			<input type="file" name="boFiles" id="boFiles" multiple/>
		</td>
	</tr>
</table>
		
	<button type="button" id="updateBtn">수정</button>
	<button type="button" id="cancelBtn">취소</button>
	
</form>
</body>
<script type="text/javascript">
	const updateForm = document.updateForm;
	const updateBtn = document.getElementById("updateBtn");
	const cancelBtn = document.getElementById("cancelBtn");
	const boardTitleEl = document.updateForm.boardTitle;
	const boardContentEl = document.updateForm.boardContent;
	const fileDeleteBtns = document.querySelectorAll(".fileDeleteBtn");
	
	let index = 0;
	for(let fileDeleteBtn of fileDeleteBtns){
		fileDeleteBtn.addEventListener("click",function(e){
			let parentNode = e.target.parentElement;
			let fileNo = parentNode.dataset.fileNo;
			
			const inputDOM = document.createElement("input");
			inputDOM.type = 'hidden';
			inputDOM.name = 'deleteFileNoList['+ index +']';
			inputDOM.value = fileNo;
			index++;
			updateForm.append(inputDOM);
			parentNode.remove();
		})
	}
	
	updateBtn.addEventListener("click",function(){
		
		let boardTitle = boardTitleEl.value;
		let boardContent = boardContentEl.value;
		
		if(boardTitle == null || boardTitle.trim() == ''){
			alert("제목을 입력해주세요");
			return false;
		}
		if(boardContent == null || boardContent.trim() == ''){
			alert("내용을 입력해주세요");
			return false;
		}
		if(confirm("수정하시겠습니까?")){
			updateForm.submit();			
		}
	})
	
	cancelBtn.addEventListener("click",function(){
		window.location.href = "${ROOT_PATH}/cmmn/board/boardDetail.do?boardId=${boardVO.boardId}";
	});
</script>
</html>