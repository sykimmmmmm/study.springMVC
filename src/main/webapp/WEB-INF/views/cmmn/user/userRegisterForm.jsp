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

<form action="${ROOT_PATH}/cmmn/user/userRegister.do" method="POST" enctype="multipart/form-data" name="regForm">
	<c:if test="${not empty msg }">
		<div>
			<c:out value="${msg }"/>
		</div>
	</c:if>
	<table>
		<tr>
			<td>아이디 : </td>
			<td><input type="text" name="userId" id="userId"/></td>
		</tr>
		<tr>
			<td>비밀번호 : </td>
			<td><input type="password" name="userPw" id="userPw"/></td>
		</tr>
		<tr>
			<td>비밀번호 확인 : </td>
			<td><input type="password" name="userPwConfirm" id="userPwConfirm"/></td>
		</tr>
		<tr>
			<td>이름 : </td>
			<td><input type="text" name="userNm" id="userNm"/></td>
		</tr>
		<tr>
			<td>파일 :</td>
			<td><div id="previewBox" style="display: none; "></div> <input type="file" name="boFiles" id="boFiles" accept="image/*" /></td>
		</tr>
		<tr>
			<td>권한 : </td>
			<td>
				<c:forEach items="${codeList}" var="code">
					<input type="checkbox" name="authList" value="${code.cd}"/> ${code.cdNm}
				</c:forEach>
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
	const userIdEl = document.regForm.userId;
	const userPwEl = document.regForm.userPw;
	const userPwConfirmEl = document.regForm.userPwConfirm;
	const userNmEl = document.regForm.userNm;
	const fileEl = document.regForm.boFiles;
	const previewBox = document.getElementById("previewBox");
	
	fileEl.addEventListener("change",function(){
		let fileList = fileEl.files;
		if(fileList.length == 0){
			fileEl.value = '';
			previewBox.innerHTML = '';
			previewBox.style.display = 'none';
			return false;
		}
		let file = fileList[0];
		if(!file.type.startsWith("image/")){
			alert("이미지 파일만 올려주세요");
			fileEl.value = '';
			return false;
		}
		const url = URL.createObjectURL(file);
		const imgDOM = document.createElement("img");
		imgDOM.src = url;
		previewBox.append(imgDOM);
		previewBox.style.display = 'block';
	})
	
	let pwCheckFlag = false;
	userPwEl.addEventListener("change",function(){
		pwCheckFlag = false;		
	})
	userPwConfirmEl.addEventListener("change",function(){
		pwCheckFlag = false;		
	})
	
	registBtn.addEventListener("click",function(){
		
		let userId = userIdEl.value;
		let userPw = userPwEl.value;
		let userPwConfirm = userPwConfirmEl.value;
		let userNm = userNmEl.value;
		let authFlag = document.querySelector("input[type='checkbox']:checked");
		
		if(userId == null || userId.trim() == ''){
			alert("아이디를 입력해주세요");
			return false;
		}
		if(userPw == null || userPw.trim() == ''){
			alert("비밀번호를 입력해주세요");
			return false;
		}
		if(userNm == null || userNm.trim() == ''){
			alert("이름을 입력해주세요");
			return false;
		}
		if(authFlag == null){
			alert("권한을 체크해주세요");
			return false;
		}
		if(userPw == userPwConfirm){
			pwCheckFlag = true;	
		}else{
			pwCheckFlag = false;
		}
		
		if(pwCheckFlag){
			if(confirm("등록하시겠습니까?")){
				registForm.submit();			
			}
		}else{
			alert("비밀번호가 일치하지 않습니다");
		}
	})
	
	listBtn.addEventListener("click",function(){
		window.location.href = "${ROOT_PATH}/cmmn/user/userList.do";
	});
</script>
</html>