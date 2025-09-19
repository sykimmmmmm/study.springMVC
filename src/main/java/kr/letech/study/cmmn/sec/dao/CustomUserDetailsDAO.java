/**
 * 
 */
package kr.letech.study.cmmn.sec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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
 *  2025-09-16  KSY			최초 생성
 */
@Mapper
public interface CustomUserDetailsDAO {

	/**
	 * 사용자 정보 조회
	 * @param userId 사용자ID
	 * @return 사용자 정보
	 */
	UserVO selectUser(String userId);
	
	/**
	 * 사용자 권한 목록 조회
	 * @param userId 사용자ID
	 * @return 사용자 권한 목록
	 */
	List<String> selectUserAuthList(String userId);
}
