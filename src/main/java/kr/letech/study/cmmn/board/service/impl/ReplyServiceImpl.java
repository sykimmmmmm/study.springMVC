/**
 * 
 */
package kr.letech.study.cmmn.board.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.letech.study.cmmn.board.dao.ReplyDAO;
import kr.letech.study.cmmn.board.service.IReplyService;
import kr.letech.study.cmmn.board.vo.BoardVO;
import kr.letech.study.cmmn.board.vo.ReplyVO;
import kr.letech.study.cmmn.utils.UserUtils;
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
@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyServiceImpl implements IReplyService {
	
	private final ReplyDAO replyDAO;

	@Transactional
	@Override
	public void insertReply(ReplyVO replyVO) {
		replyVO.setRgstId(UserUtils.getUserId());
		replyDAO.insertReply(replyVO);
	}

	@Override
	public void deleteReplyAll(BoardVO boardVO) {
		replyDAO.deleteReplyAll(boardVO);
	}
	
	@Override
	public void updateReply(ReplyVO replyVO) {
		replyVO.setUpdtId(UserUtils.getUserId());
		replyDAO.updateReply(replyVO);
	}
	
	@Override
	public void deleteReplyOne(ReplyVO replyVO) {
		replyVO.setUpdtId(UserUtils.getUserId());
		replyDAO.deleteReplyOne(replyVO);
	}
}
