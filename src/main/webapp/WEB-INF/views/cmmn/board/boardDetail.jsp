<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<sec:authentication property="principal.userVO.userId" var="loginUserId"/>
<form name="delForm" action="${CONTEXT_PATH}/cmmn/board/boardDelete.do" method="POST">
	<input type="hidden" name="boardId" value="${boardVO.boardId}"/>
	<input type="hidden" name="fileGrpId" value="${boardVO.fileGrpId != null ? boardVO.fileGrpId : ''}"/>
</form>
<div class="container mt-4">

  	<!-- 제목 -->
  	<h2 class="mb-3">${boardVO.boardTitle}</h2>

	<!-- 메타정보 -->
	<table class="table table-bordered">
		<tr>
			<th style="width: 15%;">작성자</th>
			<td>${boardVO.rgstId}</td>
			<th style="width: 15%;">작성일시</th>
			<td>${boardVO.rgstDt}</td>
	</tr>
	</table>

  	<!-- 내용 -->
	<div class="card my-3">
		<div class="card-body min-vh-50" style="white-space:pre-line;">${boardVO.boardContent}</div>
	</div>

	<!-- 첨부파일 -->
	<div class="mb-3">
		<h6>첨부파일</h6>
		<ul class="list-group">
			<c:if test="${not empty boardVO.fileList}">
				<c:forEach items="${boardVO.fileList}" var="fileVO">
					<li class="list-group-item">
						<span>${fileVO.fileOriginNm}</span>
						<a href="${CONTEXT_PATH}/cmmn/file/download.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}" class="link-primary">[다운로드]</a>
					</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty boardVO.fileList }">
				<li class="list-group-item">첨부파일이 존재하지 않습니다.</li>
			</c:if>	
		</ul>
	</div>
	<div class="mb-3 d-flex justify-content-end align-items-center gap-1">
		<c:if test="${boardVO.rgstId eq loginUserId}">
			<button class="btn btn-warning" id="updateBtn">수정</button>
			<button class="btn btn-danger" id="deleteBtn">삭제</button>
		</c:if>
		<button class="btn btn-secondary" id="listBtn">목록</button>
	</div>
  	<!-- 댓글 -->
  	<div id="replyContent" class="mt-4">
    	<h6>댓글</h6>
    	<div class="mb-3">
    		<div class="input-group">
				<textarea class="form-control" rows="3" name="replyInput" id="replyInput" placeholder="댓글을 입력해주세요" wrap="hard"></textarea>
				<button type="button" class="btn btn-primary btn-sm" id="commentInsertBtn">댓글 등록</button>
    		</div>
		</div>
    	<ul class="list-group">
	    	<c:if test="${not empty boardVO.replyList}">
				<c:forEach items="${boardVO.replyList}" var="replyVO">
					<c:set var="margin" value="${replyVO.replyDepth % 5 - 1}"/>
					<li class="list-group-item card-body replyWrapper ms-${margin == 0 ? margin : margin + 2 }"
						 data-reply-id="${replyVO.replyId}"
						 data-reply-depth="${replyVO.replyDepth}">
						<div class="replyHeader">
							<span>${replyVO.rgstId}</span> <small class="text-muted">${replyVO.rgstDt}</small>
						</div>
						<div class="replyBody">
							<c:if test="${replyVO.delYn eq 'Y'}">
								<p class="card-text">삭제된 댓글입니다.</p>
							</c:if>
							<c:if test="${replyVO.delYn eq 'N'}">
								<p class="card-text" style="white-space: pre-wrap;">${replyVO.replyContent }</p>
								<div>
									<button type="button" class="replyInsertBtn btn btn-sm btn-primary">작성</button>
									<c:if test="${replyVO.rgstId eq loginUserId}">
										<button type="button" class="replyUpdateBtn btn btn-sm btn-warning">수정</button>
										<button type="button" class="replyDeleteBtn btn btn-sm btn-danger" data-reply-id="${replyVO.replyId}">삭제</button>
									</c:if>
								</div>
								<div class="subReply"></div>
							</c:if>
						</div>
					</li>
				</c:forEach>
			</c:if>
    	</ul>
  	</div>
</div>

<form action="${CONTEXT_PATH}/cmmn/board/insertReply.do" method="post" name="insertReplyForm">
	<input type="hidden" name="replyContent" required/>
	<input type="hidden" name="boardId" value="${boardVO.boardId}" required>
	<input type="hidden" name="replyDepth" value="1" required>
</form>

<div style="display:none;" id="replyTemplate">
	<textarea rows="3" class="subReplyContent form-control" placeholder="답글을 입력해주세요...." wrap="hard"></textarea>
	<button type="button" class="confirmBtn btn btn-sm btn-primary">등록</button>
	<button type="button" class="cancleBtn btn btn-sm btn-secondary">취소</button>
</div>

</body>
<script type="text/javascript">
	$(function(){
		const $delForm = $("form[name='delForm']");
		const $updateBtn = $("#updateBtn");
		const $deleteBtn = $("#deleteBtn");
		const $listBtn = $("#listBtn");
		
		$updateBtn.on("click",function(){
			location.href="${CONTEXT_PATH}/cmmn/board/boardUpdateForm.do?boardId=${boardVO.boardId}";
		});
		
		$deleteBtn.on("click",function(){
			if(confirm("정말로 삭제하시겠습니까?")){
				$delForm.submit();
			}
		})
		
		$listBtn.on("click",function(){
			location.href="${Context_PATH}/cmmn/board/boardList.do";
		})
		
		// 댓글 관련 기능
		const $insertReplyForm = $("form[name='insertReplyForm']");
		const $replyContent = $("input[name='replyContent']");
		const $replyDepth = $("input[name='replyDepth']");
		const $replyInput = $("#replyInput")// 댓글 작성
		const $replyBodys = $(".replyBody");
		const $commentInsertBtn = $("#commentInsertBtn"); // 댓글 작성
		const $replyTemplate = $("#replyTemplate"); // 댓글 작성
		
		$commentInsertBtn.on("click",function(){
			let replyContent = $replyInput.val();
			if(replyContent == null || replyContent.trim() == ''){
				alert("내용을 입력해주세요");
				return false;
			}
			if(confirm("댓글을 등록하시겠습니까?")){
				$replyContent.val(replyContent);
				$insertReplyForm.submit();
			}
		})
		
		let activeEl = null;
		$replyBodys.on("click",function(e){
			let target = e.target;
			if($(target).hasClass("replyInsertBtn")){
				const $subReply = $(target).parent().next(".subReply");
				$subReply.data("name","등록");
				$subReply.data("content","");
				openSubReply($subReply);
				
			}
			if($(target).hasClass("replyUpdateBtn")){
				const $subReply = $(target).parent().next(".subReply");
				let content = $(target).parent().prev("p").text();
				$subReply.data("name","수정");
				$subReply.data("content",content);
				openSubReply($subReply)
			}
			if($(target).hasClass("replyDeleteBtn")){
				let replyId = $(target).data("replyId");
				
				const inputDOM = $(`<input type="hidden" name="replyId" value="\${replyId}">`);
				$insertReplyForm.append(inputDOM);
				$insertReplyForm.attr("action","${CONTEXT_PATH}/cmmn/board/deleteReply.do");
				
				if(confirm("삭제하시겠습니까?")){
					$insertReplyForm.submit();
				}
			}
			// 답글 작성 및 수정
			if($(target).hasClass("confirmBtn")){
				const $replyWrapper = $(target).closest(".replyWrapper");
				
				let replyId = $replyWrapper.data("replyId");
				let replyDepth = $replyWrapper.data("replyDepth");
				
				const ta = activeEl.children(".subReplyContent");
				
				$replyContent.val(ta.val());
				let name = activeEl.data("name");
				if(confirm(name+"하시겠습니까?")){
					const $replyParentId = $insertReplyForm.children("input[name='replyParentId']");
					if(name == '등록'){
						if($replyParentId.length == 0){
							const inputDOM = $(`<input type='hidden' name='replyParentId' value='\${replyId}'>`);
							$insertReplyForm.append(inputDOM);
						}
						$replyDepth.val(+replyDepth + 1);
						$insertReplyForm.attr("action","${CONTEXT_PATH}/cmmn/board/insertReply.do");
						$insertReplyForm.submit();
						
					}else if(name == '수정'){
						if($replyParentId != null){
							$replyParentId.remove();
						}
						const inputDOM = $(`<input type='hidden' name='replyId' value='\${replyId}'>`);
						$insertReplyForm.append(inputDOM);
						$insertReplyForm.attr("action","${CONTEXT_PATH}/cmmn/board/updateReply.do");
					
						$insertReplyForm.submit();
					}
				}
			}
			// 답글 작성 및 수정 취소
			if($(target).hasClass("cancleBtn")){
				const $replyWrapper = $(target).closest(".replyWrapper");
				let replyId = $replyWrapper.data("replyId");
				let replyDepth = $replyWrapper.data("replyDepth");
				
				closeActiveSubReply();
				
				const $replyParentId = $insertReplyForm.children("input[name='replyParentId']");
				if($replyParentId.length > 0){
					$replyParentId.remove();
				}
				$replyDepth.val("1");
				$replyContent.val('');
			}
			
		})
		
		function openSubReply(subReplyEl){
			if(activeEl && activeEl == subReplyEl){
				let name = activeEl.data("name");
				let content = activeEl.data("content");
				const confirmBtn = activeEl.children(".confirmBtn");
				confirmBtn.text(name);
				
				const ta = activeEl.children(".subReplyContent");
				if(content != null && content != ''){
					ta.val(content);
				}else{
					ta.val("");
				}
				ta.focus();
				return;
			}
			closeActiveSubReply();
			
			subReplyEl.html($replyTemplate.html())
			let name = subReplyEl.data("name");
			let content = subReplyEl.data("content");
			
			const confirmBtn = subReplyEl.children(".confirmBtn");
			confirmBtn.text(name);
			
			const ta = subReplyEl.children(".subReplyContent");
			if(content != ''){
				ta.val(content);
			}else{
				ta.val('');
			}
			ta.focus();
			
			activeEl = subReplyEl;
		}
		
		function closeActiveSubReply(){
			if(activeEl == null){
				return;
			}
			
			activeEl.html("");
			activeEl = null;
		}
	})
		
</script>
</html>