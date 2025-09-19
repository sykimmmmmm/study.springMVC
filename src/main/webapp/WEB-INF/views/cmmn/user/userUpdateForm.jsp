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

<form action="${ROOT_PATH}/cmmn/user/userUpdate.do" method="POST" enctype="multipart/form-data" name="updateForm">
	<c:if test="${not empty msg }">
		<div>
			<c:out value="${msg }"/>
		</div>
	</c:if>
	<input type="hidden" name="userId" value="${userVO.userId }">
	<input type="hidden" name="fileGrpId" value="${userVO.fileGrpId}">
	<input type="hidden" name="deleteFileNo" value="${userVO.fileVO != null ? userVO.fileVO.fileNo : ''}">
	<table>
		<tr>
			<td>아이디 : </td>
			<td>${userVO.userId }</td>
		</tr>
		<tr>
			<td>비밀번호 : </td>
			<td><input type="password" name="userPw" id="userPw"/></td>
		</tr>
		<tr>
			<td>이름 : </td>
			<td><input type="text" name="userNm" id="userNm" value="${userVO.userNm }"/></td>
		</tr>
		<tr>
			<td>권한 : </td>
			<td> 
				<c:if test="${not empty codeList}">
					<c:forEach items="${codeList}" var="code">
						<c:set var="authCheck" value="false"/>
						<c:if test="${not empty userVO.authVOList}">
							<c:forEach items="${userVO.authVOList}" var="authVO">
								<c:if test="${authVO.userAuth eq code.cd}">
									<c:set var="authCheck" value="true"/>
								</c:if>
							</c:forEach>
						</c:if>
						
						<input type="checkbox" name="authList" value="${code.cd}" <c:if test="${authCheck}">checked</c:if> /> ${code.cdNm}
					</c:forEach>
				</c:if>
			</td>
		</tr>
		<tr>
			<td>사진:</td>
			<td>
				<div id="previewBox">
					<c:if test="${not empty userVO.fileVO}">
						<c:set var="fileVO" value="${userVO.fileVO}"/>
						<img alt="${fileVO.fileOriginNm}" src="${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}">
					</c:if>
				</div>
				<button type="button" id="imgUpdtBtn">수정</button>
				<button type="button" id="imgResetBtn">이미지초기화</button>
				<input type="file" name="boFiles" id="boFiles" style="display:none;" accept="image/*"/>
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
	const userPwEl = document.updateForm.userPw;
	const userNmEl = document.updateForm.userNm;
	
	updateBtn.addEventListener("click",function(){
		
		let userPw = userPwEl.value;
		let userNm = userNmEl.value;
		
		if(userNm == null || userNm.trim() == ''){
			alert("이름을 입력해주세요");
			return false;
		}
		if(userPw != null && userNm.trim() == ''){
			userPwEl.value='';
		}
		if(confirm("수정하시겠습니까?")){
			updateForm.submit();			
		}
	})
	
	cancelBtn.addEventListener("click",function(){
		window.location.href = "${ROOT_PATH}/cmmn/user/userDetail.do?userId=${userVO.userId}";
	});
	
	// 이미지 관련
	const fileEl = document.updateForm.boFiles;
	const previewBox = document.getElementById("previewBox");
	const imgUpdtBtn = document.getElementById("imgUpdtBtn");
	const imgResetBtn = document.getElementById("imgResetBtn");
	let imgSrcBak = "";
	if("${userVO.fileVO}" != null){
		imgSrcBak = "${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${userVO.fileVO.fileGrpId}&fileNo=${userVO.fileVO.fileNo}"
	}
	
	imgUpdtBtn.addEventListener("click",function(){
		fileEl.click();
	})
	imgResetBtn.addEventListener("click",function(){
		fileEl.value = "";
		if(imgSrcBak != ""){
			let html = `<img src="\${imgSrcBak}"/>`;
			previewBox.innerHTML = html;
		}else{
			previewBox.innerHTML = "";			
		}
	})
	fileEl.addEventListener("change",function(){
		let fileList = fileEl.files;
		let file = fileList[0];
		if(file == null && imgSrcBak != ""){
			let html = `<img src="\${imgSrcBak}"/>`;
			previewBox.innerHTML = html;
		}else{
			if(!file.type.startsWith("image/")){
				alert("이미지 파일만 올려주세요");
				fileEl.value = "";
				let html = `<img src="\${imgSrcBak}"/>`;
				previewBox.innerHTML = html;
				return false;
			}
			const fileReader = new FileReader();
			fileReader.onload = function(e){
				let html = `<img src="\${e.target.result}" title="\${file.name}" />`;
				previewBox.innerHTML = html;
				previewBox.style.display = 'block';
			}
			fileReader.readAsDataURL(file)
		}
	})
</script>
</html>