/**
 * 
 */
package kr.letech.study.cmmn.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.letech.study.cmmn.user.vo.UserAuthVO;
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
@Mapper
public interface UserDAO {

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
	 * @param userVO
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
	 * 로그인 기능 없어서 만든 임시 기능
	 * 유저 목록 가져오기 이름, 아이디
	 * @return
	 */
	List<UserVO> selectUserListTemp();

	/**
	 * @param authVO
	 */
	void insertUserAuth(UserAuthVO authVO);

	/**
	 * @param authVO
	 */
	void mergeUserAuth(UserAuthVO authVO);

	/**
	 * @param userVO
	 */
	void mergeUserAuth2(UserVO userVO);

	/**
	 * @param userVO
	 */
	void deleteUserAuth(UserVO userVO);

}
