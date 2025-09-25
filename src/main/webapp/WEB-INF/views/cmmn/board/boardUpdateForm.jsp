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
<form action="${ROOT_PATH}/cmmn/board/boardUpdate.do" method="POST" enctype="multipart/form-data" name="updateForm">
	<input type="hidden" name="boardId" value="${boardVO.boardId}">
	<input type="hidden" name="rgstId" value="${boardVO.rgstId}">
	<input type="hidden" name="fileGrpId" value="${boardVO.fileGrpId != null ? boardVO.fileGrpId : ''}">
	
	<div class="container mt-4">
	
		<table class="table table-bordered">
			<tr>
				<th style="width: 15%"><label for="boardTitle" class="col-form-label">제목</label></th>
				<td><input type="text" id="boardTitle" class="form-control" name="boardTitle" value="${boardVO.boardTitle}" required></td>
			</tr>
			<tr>
				<th style="width: 15%;">작성자</th>
				<td><c:out value="${boardVO.rgstId}"></c:out></td>
			</tr>
		</table>
	
	  	<!-- 내용 -->
		<div class="card my-3">
			<div class="card-body min-vh-50">
				<textarea class="form-control" rows="20" wrap="hard" name="boardContent" id="boardContent" required>${boardVO.boardContent}</textarea>
			</div>
		</div>
	
		<!-- 첨부파일 -->
		<div class="mb-3">
			<input type="file" class="form-control" name="boFiles" id="boFiles" multiple />
			<ul id="previewBox" class="mt-2 list-group list-group-horizontal">
				<c:if test="${not empty boardVO.fileList}">
					<c:forEach items="${boardVO.fileList}" var="fileVO">
						<li class="list-group-item" data-file-no="${fileVO.fileNo}">
							<span class="fileNm">${fileVO.fileOriginNm}</span>
							<span class="fileDeleteBtn badge text-bg-danger">X</span>
						</li>
					</c:forEach>
				</c:if>
			</ul>
		</div>
		<div class="mb-3 d-flex justify-content-end align-items-center gap-1">
			<button type="button" class="btn btn-primary" id="updateBtn">수정</button>
			<button type="button" class="btn btn-danger" id="cancelBtn">취소</button>
		</div>
	</div>
<!-- <table> -->

<!-- 	<tr> -->
<!-- 		<td>제목</td> -->
<%-- 		<td> <input type="text" name="boardTitle" value="${boardVO.boardTitle}"/></td> --%>
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td>작성자</td> -->
<%-- 		<td>${boardVO.rgstId} </td> --%>
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td>내용</td> -->
<%-- 		<td><textarea rows="20" cols="50" name="boardContent">${boardVO.boardContent}</textarea></td> --%>
<!-- 	</tr> -->
<!-- 	<tr> -->
<!-- 		<td>파일</td> -->
<!-- 		<td> -->
<%-- 			<c:if test="${not empty boardVO.fileList}"> --%>
<%-- 				<c:forEach items="${boardVO.fileList}" var="fileVO"> --%>
<%-- 					<div data-file-no="${fileVO.fileNo}"> --%>
<%-- 						<span>${fileVO.fileOriginNm}</span> --%>
<!-- 						<span class="fileDeleteBtn">X</span> -->
<!-- 					</div> -->
<%-- 				</c:forEach> --%>
<%-- 			</c:if> --%>
<!-- 			<input type="file" name="boFiles" id="boFiles" multiple/> -->
<!-- 		</td> -->
<!-- 	</tr> -->
<!-- </table> -->
		
<!-- 	<button type="button" id="updateBtn">수정</button> -->
<!-- 	<button type="button" id="cancelBtn">취소</button> -->
	
</form>
</body>
<script type="text/javascript">
	$(function(){
		const $updateForm = $("form[name='updateForm']");
		const $updateBtn = $("#updateBtn");
		const $cancelBtn = $("#cancelBtn");
		const $boardTitle = $updateForm.find("#boardTitle");
		const $boardContent = $updateForm.find("#boardContent");
		const $boFiles = $updateForm.find("#boFiles");
		const $previewBox = $("#previewBox");
		const $fileDeleteBtns = $(".fileDeleteBtn");
		
		$previewBox.on("click",".fileDeleteBtn",function(){
			const parentDOM = $(this).parent("li");
			const fileNo = parentDOM.data("fileNo") || 0;
			if(fileNo != 0){
				const inputDOM = $(`<input type="hidden" name="deleteFileNos" value="\${fileNo}" />`)
				$updateForm.append(inputDOM);
			}else{
				const fileName = $(this).prev(".fileNm").text();
				const fileList = $boFiles[0].files;
				let targetIndex = -1;
				Array.from(fileList).forEach((file,idx)=>{
					if(file.name == fileName ){
						targetIndex = idx;
					}
				})
				fileDelete(targetIndex);
			}
			parentDOM.remove();
		})
		
		$boFiles.on("change",function(){
			const fileList = this.files;
			if(fileList.length != 0){
				Array.from(fileList).forEach((file,idx)=>{
					const liDOM = $(`<li class="list-group-item"></li>`);
					const spanDOM1 = $(`<span class="fileNm">\${file.name}</span>`);
					const spanDOM2 = $(`<span class="fileDeleteBtn badge text-bg-danger">X</span>`);
					liDOM.append(spanDOM1);
					liDOM.append(spanDOM2);
					$previewBox.append(liDOM)
				})
			}else{
				const liDOM = $previewBox.children().not("li[data-file-no]");
				liDOM.remove();
			}
		})
		
		$updateBtn.on("click",function(){
			let boardTitle = $boardTitle.val();
			let boardContent = $boardContent.val();
			
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
		
		$cancelBtn.on("click",function(){
			location.href = "${ROOT_PATH}/cmmn/board/boardDetail.do?boardId=${boardVO.boardId}";
		});
		
		function fileDelete(index){
			if(index == -1){
				return;
			}
			const dataTransfer = new DataTransfer();
			const fileList = $boFiles[0].files;
			const fileArray = Array.from(fileList);
			
			fileArray.splice(index,1);
			fileArray.forEach(file => dataTransfer.items.add(file));
			$boFiles[0].files = dataTransfer.files;
		}
	})
</script>
</html>