<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	div {
	border-collapse: collapse;
}
</style>
</head>
<body>
<sec:authentication property="principal.userVO" var="loginUser"/>
<form action="${CONTEXT_PATH}/cmmn/board/boardRegister.do" method="POST" enctype="multipart/form-data" name="regForm">
	<table>
		<tr>
			<td>제목 : </td>
			<td><input type="text" name="boardTitle" id="boardTitle" maxlength="30" style="width: 500px;"/></td>
		</tr>
		
		<tr>
			<td>등록자 : </td>
			<td><input type="hidden" name="rgstId" id="rgstId" value="${loginUser.userId}"/>${loginUser.userNm }</td>
		</tr>
		
		<tr>
			<td>내용 : </td>
			<td>
				<textarea rows="20" cols="50" name="boardContent"></textarea>
			</td>
		</tr>
		<tr>
			<td>파일 :</td>
			<td> 
				<input type="file" name="boFiles" id="boFiles" multiple />
				<div id="previewBox" style="display:none;"></div>
			</td>
		</tr>
	</table>
		
	<button type="button" id="registBtn">등록</button>
	<button type="button" id="listBtn">목록</button>
	
</form>
</body>
<script type="text/javascript">
	const registForm = document.regForm;
	const registBtn = document.getElementById("registBtn");
	const listBtn = document.getElementById("listBtn");
	const boardTitleEl = document.regForm.boardTitle;
	const boardContentEl = document.regForm.boardContent;
	const fileEl = document.regForm.boFiles;
	const previewBox = document.getElementById("previewBox");
	
	registBtn.addEventListener("click",function(){
		
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
		
		if(confirm("등록하시겠습니까?")){
			registForm.submit();			
		}
		
	})
	fileEl.addEventListener("change", function(){
		let fileList = fileEl.files;
		const maxSize = 3 * 1024 * 1024;
		
		console.dir(fileList);
		if(fileList.length != 0){
			previewBox.innerHTML = '';
			previewBox.style.display = "block";
			for(let file of fileList){
				const divWrapDOM = document.createElement("div");
				divWrapDOM.style.border = "1px solid #777";
				console.dir(file)
				if(file.size > maxSize){
					alert("최대 3MB로 지정해주세요");
					fileEl.value = '';
					previewBox.style.display="none";
				}
				if(file.type.startsWith("image/")){
					let url = URL.createObjectURL(file);
						const imgDOM = document.createElement("img");
						imgDOM.src = url;
						imgDOM.style.width = "100px";
						imgDOM.style.height = "100px";
						divWrapDOM.append(imgDOM);
				}
				const divDOM = document.createElement("div");
				divDOM.innerText = file.name;
				divWrapDOM.append(divDOM);
				previewBox.append(divWrapDOM)
			}
		}else{
			previewBox.style.display = "none";
			previewBox.innerHtml = '';
		}
	})
	
	listBtn.addEventListener("click",function(){
		window.location.href = "${CONTEXT_PATH}/cmmn/board/boardList.do";
	});
</script>
</html>