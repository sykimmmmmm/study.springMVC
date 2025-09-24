/**
 * 
 */
package kr.letech.study.cmmn.board.service;

import java.util.List;

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
public interface IBoardService {

	/**
	 * @param searchType
	 * @param searchValue
	 * @return
	 */
	List<BoardVO> selectBoardList(String searchType, String searchValue);

	/**
	 * @param boardVO
	 * @return
	 */
	void insertBoard(BoardVO boardVO);

	/**
	 * 해당 게시글정보 가져오기 댓글 o
	 * @param boardId
	 * @return
	 */
	BoardVO selectBoardOne(int boardId);

	/**
	 * 해당 게시글정보만 가져오기 댓글 x
	 * @param boardId
	 * @return
	 */
	BoardVO selectBoardOnly(int boardId);

	/**
	 * @param boardVO
	 * @return
	 */
	void updateBoard(BoardVO boardVO);

	/**
	 * @param boardVO
	 * @return
	 */
	void deleteBoard(BoardVO boardVO);

}
