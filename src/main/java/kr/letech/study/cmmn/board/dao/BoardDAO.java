/**
 * 
 */
package kr.letech.study.cmmn.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.letech.study.cmmn.board.vo.BoardVO;

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
@Mapper
public interface BoardDAO {

	/**
	 * @param boardVO
	 * @return
	 */
	List<BoardVO> selectBoardList(BoardVO boardVO);

	/**
	 * @param boardVO
	 * @return
	 */
	int insertBoard(BoardVO boardVO);

	/**
	 * @param boardId
	 * @return
	 */
	BoardVO selectBoardOne(int boardId);

	/**
	 * @param boardId
	 * @return
	 */
	BoardVO selectBoardOnly(int boardId);

	/**
	 * @param boardVO
	 * @return
	 */
	int updateBoard(BoardVO boardVO);

	/**
	 * @param boardVO
	 */
	void deleteReplyAll(BoardVO boardVO);

	/**
	 * @param boardVO
	 * @return
	 */
	int deleteBoard(BoardVO boardVO);

}
