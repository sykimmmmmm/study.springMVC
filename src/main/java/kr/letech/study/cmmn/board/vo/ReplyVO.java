/**
 * 
 */
package kr.letech.study.cmmn.board.vo;

import kr.letech.study.cmmn.vo.BaseTableVO;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 * 
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-16  KSY			최초 생성
 */
@Getter 
@Setter
public class ReplyVO extends BaseTableVO{
	private int replyId;
	private int boardId;
	private String replyContent;
	private int replyDepth;
	private int replyParentId;
	
}
