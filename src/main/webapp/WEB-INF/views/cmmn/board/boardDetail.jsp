<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	textarea {
		width : 500px; 
		resize: none;
	}
</style>
</head>
<body>
<form name="delForm" action="${CONTEXT_PATH}/cmmn/board/boardDelete.do" method="POST">
	<input type="hidden" name="boardId" value="${boardVO.boardId}"/>
	<input type="hidden" name="fileGrpId" value="${boardVO.fileGrpId != null ? boardVO.fileGrpId : ''}"/>
</form>
<table border="1" >
	<tr>
		<td>제목</td>
		<td>${boardVO.boardTitle}</td>
	</tr>
	<tr>
		<td>작성자</td>
		<td>${boardVO.rgstId} </td>
	</tr>
	<tr>
		<td>작성일시</td>
		<td>${boardVO.rgstDt} </td>
	</tr>
	<tr>
		<td>내용</td>
		<td>${boardVO.boardContent}</td>
	</tr>
	<tr>
		<td>파일</td>
		<td>
			<c:if test="${not empty boardVO.fileList}">
				<c:forEach items="${boardVO.fileList}" var="fileVO">
					<div>
						<p>${fileVO.fileOriginNm}</p>
						<a href="${CONTEXT_PATH}/cmmn/file/download.do?fileGrpId=${fileVO.fileGrpId}&fileNo=${fileVO.fileNo}">[다운로드]</a>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${empty boardVO.fileList }">
				첨부파일 없습니다.
			</c:if>	
		</td>
	</tr>
</table>
<div>
	<button id="updateBtn">수정</button>
	<button id="deleteBtn">삭제</button>
	<button id="listBtn">목록</button>
</div>
<form action="${CONTEXT_PATH}/cmmn/board/insertReply.do" method="post" name="insertReplyForm">
	<input type="hidden" name="replyContent" />
	<input type="hidden" name="boardId" value="${boardVO.boardId}">
	<input type="hidden" name="replyDepth" value="1">
</form>
<div id="replyContent">
	<div>
		<p>댓글작성</p>
		<textarea rows="3" name="replyInput" id="replyInput" placeholder="댓글을 입력해주세요"></textarea>
		<button type="button" id="commentInsertBtn">댓글 등록</button>
	</div>
	<c:if test="${not empty boardVO.replyList}">
		<c:forEach items="${boardVO.replyList}" var="replyVO">
			<div style=" border-bottom: 1px solid #777; padding-left:${((replyVO.replyDepth -1)*20)}px;"
				 data-reply-id="${replyVO.replyId}"
				 data-reply-depth="${replyVO.replyDepth}">
				<div class="replyHeader">
					<c:if test="${replyVO.replyDepth != 1}"><span>└ </span></c:if>
					<span>${replyVO.rgstId}</span> <small>${replyVO.rgstDt}</small>
				</div>
				<div class="replyBody">
					<c:if test="${replyVO.delYn eq 'Y'}">
						<p>삭제된 댓글입니다.</p>
					</c:if>
					<c:if test="${replyVO.delYn eq 'N'}">
						<p>${replyVO.replyContent }</p>
						<div>
							<button type="button" class="replyInsertBtn">작성</button>
							<button type="button" class="replyUpdateBtn">수정</button>
							<button type="button" class="replyDeleteBtn" data-reply-id="${replyVO.replyId}">삭제</button>
						</div>
						<div class="subReply"></div>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</c:if>
	
</div>
<div style="display:none;" id="replyTemplate">
	<textarea rows="3" class="subReplyContent" placeholder="답글을 입력해주세요...."></textarea>
	<button type="button" class="confirmBtn">등록</button>
	<button type="button" class="cancleBtn">취소</button>
</div>

</body>
<script type="text/javascript">
	const delForm = document.delForm
	const updateBtn = document.getElementById("updateBtn");
	const deleteBtn = document.getElementById("deleteBtn");
	const listBtn = document.getElementById("listBtn");
	
	updateBtn.addEventListener("click",function(){
		window.location.href="${CONTEXT_PATH}/cmmn/board/boardUpdateForm.do?boardId=${boardVO.boardId}";
	})
	
	deleteBtn.addEventListener("click",function(){
		if(confirm("정말로 삭제하시겠습니까?")){
			delForm.submit();
		}
	})
	
	listBtn.addEventListener("click",function(){
		window.location.href="${Context_PATH}/cmmn/board/boardList.do";
	})
	
	// 댓글 관련 기능
	const insertReplyForm = document.insertReplyForm;
	const replyContentEl = insertReplyForm.replyContent;
	const replyDepthEl = insertReplyForm.replyDepth;
	const replyInputEl = document.getElementById("replyInput"); // 댓글 작성
	const replyBodys = document.querySelectorAll(".replyBody");
	const commentInsertBtn = document.getElementById("commentInsertBtn"); // 댓글 작성
	const replyTemplate = document.getElementById("replyTemplate"); // 댓글 작성
	
	commentInsertBtn.addEventListener("click",function(){
		let replyContent = replyInputEl.value;
		if(replyContent == null || replyContent.trim() == ''){
			alert("내용을 입력해주세요");
			return false;
		}
		if(confirm("댓글을 등록하시겠습니까?")){
			replyContentEl.value = replyContent;
			insertReplyForm.submit();
		}
	})
	
	let activeEl = null;
	for(let replyBody of replyBodys){
		replyBody.addEventListener("click",function(e){
			let target = e.target;
			
			if(target.classList.contains("replyInsertBtn")){
				let subReply = e.target.parentElement.nextElementSibling;
				subReply.dataset.name = '등록';
				subReply.dataset.content = '';
				openSubReply(subReply);
				console.dir(subReply);
				
			}
			if(target.classList.contains("replyUpdateBtn")){
				let subReply = e.target.parentElement.nextElementSibling;
				let content = e.target.parentElement.previousElementSibling.innerText;
				subReply.dataset.name = '수정';
				subReply.dataset.content = content;
				openSubReply(subReply)
			}
			if(target.classList.contains("replyDeleteBtn")){
				console.dir(target.dataset);
				let replyId = target.dataset.replyId;

				const inputDOM = document.createElement("input");
				inputDOM.type = 'hidden';
				inputDOM.name = 'replyId';
				inputDOM.value = replyId;
				insertReplyForm.append(inputDOM);
				insertReplyForm.action = "${CONTEXT_PATH}/cmmn/board/deleteReply.do";
				
				if(confirm("삭제하시겠습니까?")){
					insertReplyForm.submit();
				}
			}
			// 답글 작성 및 수정
			if(target.classList.contains("confirmBtn")){
				const replyWrapper = target.parentElement.parentElement.parentElement;
				console.dir(replyWrapper.dataset)
				let replyId = replyWrapper.dataset.replyId
				let replyDepth = replyWrapper.dataset.replyDepth
				const ta = activeEl.querySelector(".subReplyContent");
// 				console.log(ta.value)
				replyContentEl.value = ta.value;
				let name = activeEl.dataset.name;
				if(confirm(name+"하시겠습니까?")){
					const replyParentIdEl = insertReplyForm.replyParentId;
					if(name == '등록'){
						if(replyParentIdEl == null){
							const inputDOM = document.createElement("input");
							inputDOM.type = 'hidden';
							inputDOM.name = 'replyParentId';
							inputDOM.value = replyId;
							insertReplyForm.append(inputDOM);
						}
						replyDepthEl.value = +replyDepth + 1;
						insertReplyForm.action = "${CONTEXT_PATH}/cmmn/board/insertReply.do"
						insertReplyForm.submit();
						
					}else if(name == '수정'){
						if(replyParentIdEl != null){
							replyParentIdEl.remove();
						}
						const inputDOM = document.createElement("input");
						inputDOM.type = 'hidden';
						inputDOM.name = 'replyId';
						inputDOM.value = replyId;
						insertReplyForm.append(inputDOM);
						insertReplyForm.action = "${CONTEXT_PATH}/cmmn/board/updateReply.do";
					
						insertReplyForm.submit();
					}
				}
			}
			// 답글 작성 및 수정 취소
			if(target.classList.contains("cancleBtn")){
				const replyWrapper = target.parentElement.parentElement.parentElement;
				console.dir(replyWrapper.dataset)
				let replyId = replyWrapper.dataset.replyId
				let replyDepth = replyWrapper.dataset.replyDepth
				
				closeActiveSubReply();
				
				const replyParentIdEl = insertReplyForm.replyParentId;
				if(replyParentIdEl != null){
					replyParentIdEl.remove();
				}
				replyDepthEl.value = 1;
				replyContentEl.value = '';
			}
			
		})
	}
	
	function openSubReply(subReplyEl){
		if(activeEl && activeEl == subReplyEl){
			let name = activeEl.dataset.name;
			let content = activeEl.dataset.content;
			const confirmBtn = activeEl.querySelector(".confirmBtn");
			confirmBtn.innerText = name;
			
			const ta = activeEl.querySelector(".subReplyContent");
			if(content != null && content != ''){
				ta.value = content;
			}else{
				ta.value = '';
			}
			ta.focus();
			return;
		}
		closeActiveSubReply();
		
		subReplyEl.innerHTML = replyTemplate.innerHTML;
		let name = subReplyEl.dataset.name;
		let content = subReplyEl.dataset.content;
		
		const confirmBtn = subReplyEl.querySelector(".confirmBtn");
		confirmBtn.innerText = name;
		
		const ta = subReplyEl.querySelector(".subReplyContent");
		if(content != ''){
			ta.value = content;
		}else{
			ta.value = '';
		}
		ta.focus();
		
		activeEl = subReplyEl;
	}
	
	function closeActiveSubReply(){
		if(activeEl == null){
			return;
		}
		
		activeEl.innerHTML = '';
		activeEl = null;
	}
</script>
</html>