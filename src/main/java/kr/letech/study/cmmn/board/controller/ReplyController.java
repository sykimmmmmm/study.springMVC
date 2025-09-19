/**
 * 
 */
package kr.letech.study.cmmn.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.letech.study.cmmn.board.service.IReplyService;
import kr.letech.study.cmmn.board.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-18  KSY			최초 생성
 */
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value="/cmmn/board")
public class ReplyController {
	
	private final IReplyService replyService;
	
	@RequestMapping(value = "/insertReply.do", method = RequestMethod.POST)
	public String insertReply(ReplyVO replyVO) {
		log.info("댓글 작성 -> {}", replyVO);
		replyService.insertReply(replyVO);
		
		
		return "redirect:/cmmn/board/boardDetail.do?boardId="+replyVO.getBoardId();
	}
	
	@RequestMapping(value = "/updateReply.do", method = RequestMethod.POST)
	public String updateReply(ReplyVO replyVO) {
		log.info("댓글 수정 -> {}", replyVO);
		
		replyService.updateReply(replyVO);
		
		return "redirect:/cmmn/board/boardDetail.do?boardId="+replyVO.getBoardId();
	}
	
	@RequestMapping(value = "/deleteReply.do", method = RequestMethod.POST)
	public String deleteReply(ReplyVO replyVO) {
		log.info("댓글 삭제 -> {}", replyVO);
		
		replyService.deleteReplyOne(replyVO);
		
		return "redirect:/cmmn/board/boardDetail.do?boardId="+replyVO.getBoardId();
	}
}
