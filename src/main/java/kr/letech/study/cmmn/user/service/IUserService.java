/**
 * 
 */
package kr.letech.study.cmmn.user.service;

import java.util.List;

import kr.letech.study.cmmn.user.vo.UserVO;

/**
 * <pre>
 * 
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-12  KSY			최초 생성
 *  						리스트, 생성, 상세페이지 완성
 *  2025-09-15  KSY			수정, 삭제 완성
 */
public interface IUserService {

	/**
	 * @param userVO 
	 * @return
	 */
	List<UserVO> selectUserList(UserVO userVO);

	/**
	 * @param userVO
	 * @return
	 */
	int insertUser(UserVO userVO);

	/**
	 * @param targetVO
	 * @return
	 */
	UserVO selectUserOne(UserVO userVO);

	/**
	 * @param userVO
	 * @return
	 */
	int updateUser(UserVO userVO);

	/**
	 * @param userVO
	 * @return
	 */
	int deleteUser(UserVO userVO);

	/**
	 * 로그인기능이없어서 만드는 임시기능
	 * 유저 목록가져와서 선택하기
	 * @return
	 */
	List<UserVO> selectUserListTemp();

}
