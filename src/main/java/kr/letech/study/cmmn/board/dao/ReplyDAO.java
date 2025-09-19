/**
 * 
 */
package kr.letech.study.cmmn.board.dao;

import org.apache.ibatis.annotations.Mapper;

import kr.letech.study.cmmn.board.vo.BoardVO;
import kr.letech.study.cmmn.board.vo.ReplyVO;

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
@Mapper
public interface ReplyDAO {

	/**
	 * @param replyVO
	 */
	void insertReply(ReplyVO replyVO);

	/**
	 * @param boardVO
	 */
	void deleteReplyAll(BoardVO boardVO);

	/**
	 * @param replyVO
	 */
	void updateReply(ReplyVO replyVO);

	/**
	 * @param replyVO
	 */
	void deleteReplyOne(ReplyVO replyVO);
	
}
