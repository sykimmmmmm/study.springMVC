<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<sec:authentication property="principal.userVO" var="loginUser"/>
<form action="${CONTEXT_PATH}/cmmn/board/boardRegister.do" method="POST" enctype="multipart/form-data" name="regForm">
	<div class="container mt-4">
	
		<table class="table table-bordered">
			<tr>
				<th style="width: 15%"><label for="boardTitle" class="col-form-label">제목</label></th>
				<td><input type="text" id="boardTitle" class="form-control" name="boardTitle" required></td>
			</tr>
			<tr>
				<th style="width: 15%;">작성자</th>
				<td><input type="hidden" name="rgstId" id="rgstId" value="${loginUser.userId}"/> ${loginUser.userNm }</td>
			</tr>
		</table>
	
	  	<!-- 내용 -->
		<div class="card my-3">
			<div class="card-body min-vh-50">
				<textarea class="form-control" rows="20" wrap="hard" name="boardContent" id="boardContent" required></textarea>
			</div>
		</div>
	
		<!-- 첨부파일 -->
		<div class="mb-3">
			<input type="file" class="form-control" name="boFiles" id="boFiles" multiple />
			<ul id="previewBox" class="mt-2 list-group list-group-horizontal">
			</ul>
		</div>
		<div class="mb-3 d-flex justify-content-end align-items-center gap-1">
			<button type="button" class="btn btn-primary" id="registBtn">등록</button>
			<button type="button" class="btn btn-secondary" id="listBtn">목록</button>
		</div>
	</div>
</form>
</body>
<script type="text/javascript">
	const $registForm = $("form[name='regForm']");
	const $registBtn = $("#registBtn");
	const $listBtn = $("#listBtn");
	const $boardTitle = $registForm.find("#boardTitle");
	const $boardContent = $registForm.find("#boardContent");
	const $boFiles = $registForm.find("#boFiles");
	const $previewBox = $("#previewBox");
	
	$registBtn.on("click",function(){
		
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
		
		if(confirm("등록하시겠습니까?")){
			$registForm.submit();			
		}
		
	})
	$boFiles.on("change", function(){
		let fileList = this.files;
		const maxSize = 3 * 1024 * 1024;
		
		if(fileList.length != 0){
			$previewBox.html("");
			$previewBox.removeClass("d-none");
			Array.from(fileList).forEach((file,idx)=>{
				const liDOM = $(`<li class="list-group-item" data-index="\${idx}"></li>`);
				if(file.size > maxSize){
					alert("최대 3MB로 지정해주세요");
					$file.val('');
					$previewBox.addClass("d-none");
				}
				if(file.type.startsWith("image/")){
					let url = URL.createObjectURL(file);
					const imgDOM = $(`<img class='img-thumbnail' src="\${url}" >`);
					liDOM.append(imgDOM);
				}
				const spanDOM1 = $(`<span class="fileNm">\${file.name}</span>`);
				const spanDOM2 = $(`<span class="fileDeleteBtn badge text-bg-danger">X</span>`);
				liDOM.append("<div/>")
				liDOM.append(spanDOM1);
				liDOM.append(spanDOM2);
				$previewBox.append(liDOM);
			})
		}else{
			$previewBox.addClass("d-none");
			$previewBox.html('');
		}
	})
	$previewBox.on("click",".fileDeleteBtn",function(){
		const parentDOM = $(this).parent("li");
		const fileName = $(this).prev(".fileNm").text();
		const fileList = $boFiles[0].files;
		let targetIndex = -1;
		Array.from(fileList).forEach((file,idx)=>{
			if(file.name == fileName ){
				targetIndex = idx;
			}
		})
		fileDelete(targetIndex);
		parentDOM.remove();
	})
	
	$listBtn.on("click",function(){
		location.href = "${CONTEXT_PATH}/cmmn/board/boardList.do";
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
		console.dir(dataTransfer.files)
		$boFiles[0].files = dataTransfer.files;
	}
</script>
</html>