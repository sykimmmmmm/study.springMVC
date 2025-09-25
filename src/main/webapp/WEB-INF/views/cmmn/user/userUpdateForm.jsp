<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body class="register-page bg-body-secondary">
    <div class="register-box mt-3">
      <div class="card">
        <div class="card-body register-card-body">
          <p class="register-box-msg">정보 수정</p>
          <form action="${ROOT_PATH}/cmmn/user/userUpdate.do" method="POST" enctype="multipart/form-data" name="updateForm">
			<input type="hidden" name="fileGrpId" value="${userVO.fileGrpId}">
			<input type="hidden" name="deleteFileNo" value="${userVO.fileVO != null ? userVO.fileVO.fileNo : ''}">
            <div class="input-group mb-3">
              	<div class="input-group-text"><span class="bi bi-person"></span></div>
              	<input type="text" class="form-control" name="userId" id="userId" placeholder="아이디" value="${userVO.userId}" readonly/>
            </div>
            <div class="input-group mb-3">
              	<div class="input-group-text"><span class="bi bi-lock-fill"></span></div>
              	<input type="password" class="form-control" name="userPw" id="userPw" placeholder="비밀번호" />
            </div>
            <div class="input-group mb-3">
	            <div class="input-group-text"><span class="bi bi-card-text"></span></div>
	            <input type="text" class="form-control" name="userNm" id="userNm" placeholder="이름" value="${userVO.userNm }" required/>
            </div>
            <div class="input-group mb-3">
              	<div class="input-group-text"><span class="bi bi-card-image"></span></div>
              	<input type="file" class="d-none" name="boFiles" id="boFiles" accept="image/*" placeholder="파일" />
	            <div id="previewBox" class="form-control">
					<c:if test="${not empty userVO.fileVO}">
						<c:set var="fileVO" value="${userVO.fileVO}"/>
						<img class="img-thumbnail" alt="${fileVO.fileOriginNm}" src="${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}">
					</c:if>
					<div class="btn-group">
						<button type="button" id="imgUpdtBtn" class="btn btn-sm btn-warning">수정</button>
						<button type="button" id="imgResetBtn" class="btn btn-sm btn-info">이미지초기화</button>
					</div>
				</div>
            </div>
            <div class="mb-3">
            </div>
            <div class="input-group mb-3">
              	<div class="input-group-text"><span class="bi bi-key"></span> </div>
            	<div class="form-control d-flex gap-2">
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
            	</div>
            </div>
            <div class="col mt-2">
                <div class="d-flex gap-2 align-items-center justify-content-end">
                  <button type="submit" class="btn btn-primary" id="updateBtn">저장</button>
                  <a class="btn btn-secondary" href="${CONTEXT_PATH}/cmmn/user/userDetail.do?userId=${userVO.userId}">취소</a>
                </div>
            </div>
          </form>
      </div>
    </div>
    </div>
</body>

<script type="text/javascript">
	$(function(){
		const $updateForm = $("form[name='updateForm']")
		const $updateBtn = $("#updateBtn");

		const $previewBox = $("#previewBox");
		const $previewImg = $previewBox.children("img");
		const $imgUpdtBtn = $("#imgUpdtBtn");
		const $imgResetBtn = $("#imgResetBtn");
		let imgSrcBak = "";
		if("${userVO.fileVO}" != ""){
			imgSrcBak = "${CONTEXT_PATH}/cmmn/file/view.do?fileGrpId=${userVO.fileVO.fileGrpId}&fileNo=${userVO.fileVO.fileNo}"
		}
		$imgUpdtBtn.on("click",function(){
			$("#boFiles").click();
		})
		$imgResetBtn.on("click",function(){
			$("#boFiles").val("");
			if(imgSrcBak != ''){
				$previewImg.attr("src",imgSrcBak);
				$previewImg.removeClass("d-none");
			}else{
				$previewImg.addClass("d-none");
			}
		})
		
		$("#boFiles").on("change",function(){
			const fileList = this.files;
			if(fileList.length == 0){
				if(imgSrcBak != ''){
					$previewImg.attr("src",imgSrcBak);
					$previewImg.removeClass("d-none");
				}else{
					$previewImg.addClass("d-none");
				}
				$(this).val("");
				return false;
			}
			const file = fileList[0];
			if(!file.type.startsWith("image/")){
				alert("이미지 파일만 올려주세요");
				$(this).val("");
				return false;
			}
			const url = URL.createObjectURL(file);
			$previewImg.attr("src",url);
			$previewBox.removeClass("d-none");
		})
		
	});
</script>
</html>