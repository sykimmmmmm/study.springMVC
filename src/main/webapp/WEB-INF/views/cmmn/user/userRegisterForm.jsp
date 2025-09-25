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
	<div class="register-page">
	    <div class="register-box">
	      <!-- /.register-logo -->
	      <div class="card">
	        <div class="card-body register-card-body">
	          <p class="register-box-msg">가입하기</p>
	          <form action="${ROOT_PATH}/cmmn/user/userRegister.do" method="POST" enctype="multipart/form-data" name="regForm">
	            <div class="input-group mb-3">
	              	<div class="input-group-text"><span class="bi bi-person"></span></div>
	              	<input type="text" class="form-control" name="userId" id="userId" placeholder="아이디" required />
	            </div>
	            <div class="input-group mb-3">
	              	<div class="input-group-text"><span class="bi bi-lock-fill"></span></div>
	              	<input type="password" class="form-control" name="userPw" id="userPw" placeholder="비밀번호" required />
	            </div>
	            <div class="input-group mb-3">
	              	<div class="input-group-text"><span class="bi bi-lock-fill"></span></div>
	              	<input type="password" class="form-control" name="userPwConfirm" id="userPwConfirm" placeholder="비밀번호 확인" required />
	            </div>
	            <div class="input-group mb-3">
		            <div class="input-group-text"><span class="bi bi-card-text"></span></div>
		            <input type="text" class="form-control" name="userNm" id="userNm" placeholder="이름" required/>
	            </div>
	            <div class="input-group mb-3">
	              	<div class="input-group-text"><span class="bi bi-card-image"></span></div>
	              	<input type="file" class="form-control" name="boFiles" id="boFiles" accept="image/*" placeholder="파일" required />
	            </div>
	           	<div id="previewBox" class="d-none mb-3"><img src="" class="img-thumbnail"></div>
	            <div class="input-group mb-3">
	              	<div class="input-group-text"><span class="bi bi-key"></span> </div>
	            	<div class="form-control d-flex">
						<c:forEach items="${codeList}" var="code" varStatus="vs">
							<div class="form-check col">
								<input class="form-check-input" type="checkbox" name="authList" value="${code.cd}" id="authCheck-${vs.index+1}">
								<label class="form-check-label" for="authCheck-${vs.index+1}">
								  ${code.cdNm}
								</label>
							</div>
						</c:forEach>
	            	</div>
	            </div>
	            <!--begin::Row-->
	            <div class="row">
	              <div class="col-8">
	                <div class="form-check">
	                  <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" required />
	                  <label class="form-check-label" for="flexCheckDefault">
	                    I agree to the <a href="#">terms</a>
	                  </label>
	                </div>
	              </div>
	            </div>
	            <!--end::Row-->
	            <div class="col mt-2">
	                <div class="d-flex gap-2 align-items-center justify-content-end">
	                  <button type="submit" class="btn btn-primary" id="registBtn">가입하기</button>
	                  <a class="btn btn-secondary" href="${CONTEXT_PATH}/">취소</a>
	                </div>
	            </div>
	          </form>
	          <div class="social-auth-links text-center mb-3 d-grid gap-2">
	            <p>- OR -</p>
	            <a href="#" class="btn btn-primary">
	              <i class="bi bi-facebook me-2"></i> Sign in using Facebook
	            </a>
	            <a href="#" class="btn btn-danger">
	              <i class="bi bi-google me-2"></i> Sign in using Google+
	            </a>
	          </div>
	          <!-- /.social-auth-links -->
	          <p class="mb-0">
	            <a class="link-primary" href="javascript:openLoginForm()"> 로그인하러가기 </a>
	          </p>
	        </div>
	        <!-- /.register-card-body -->
	      </div>
	    </div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		const registForm = $("form[name='regForm']");
		const registBtn = $("#registBtn");
		const userIdEl = $("#userId");
		const userPwEl = $("#userPw");
		const userPwConfirmEl = $("#userPwConfirm");
		const userNmEl = $("#userNm");
		const fileEl = $("#boFiles");
		const previewBox = $("#previewBox");
		
		fileEl.on("change",function(){
			const fileList = this.files;
			console.log(fileList)
			if(fileList.length == 0){
				$(this).val("");
				previewBox.addClass("d-none");
				return false;
			}
			const file = fileList[0];
			if(!file.type.startsWith("image/")){
				alert("이미지 파일만 올려주세요");
				$(this).val("");
				return false;
			}
			const url = URL.createObjectURL(file);
			const img = $("#previewBox > img");
			img.attr("src",url);
			previewBox.removeClass("d-none")
		});
		
	})
	function openLoginForm(){
		let width = 600;
		let height = 600;
		let winL = (screen.width - width) / 2;
		let winH = (screen.height - height) / 2 - 29;
		
		// 로그인 창 화면 중앙에 배치하기
		const loginWindow = window.open("${CONTEXT_PATH}/cmmn/sec/loginForm.do"
										,"loginWindow"
										,"width="+width+"px,height="+height+"px,top="+winH+"px,left="+winL+"px",)
	}

</script>
</html>