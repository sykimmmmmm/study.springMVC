/**
 * 
 */
package kr.letech.study.cmmn.sec.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.letech.study.cmmn.rest.RestTemplateService;
import kr.letech.study.cmmn.sec.dao.CustomUserDetailsDAO;
import kr.letech.study.cmmn.sec.vo.UserDetailsVO;
import kr.letech.study.cmmn.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

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
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final CustomUserDetailsDAO userDetailsDAO;
	private final RestTemplateService restTemplateService;
	
	@Override
	public UserDetails loadUserByUsername(String inputUserId) throws UsernameNotFoundException {
		//최종적으로 리턴해야할 객체
		UserDetailsVO userDetails = new UserDetailsVO();
		
		// 사용자 정보 select
//		UserVO userVO = this.userDetailsDAO.selectUser(inputUserId);
		UserVO userVO = this.restTemplateService.findUser(inputUserId);
		
		// 사용자 정보 없으면 null 처리
		if(userVO == null) {
			
 			return null;
			
			// 사용자 정보 있을 경우 로직 전개 (userDetails 에 데이터 넣기)
		}else {
			userDetails.setUsername(userVO.getUserId());
			userDetails.setPassword(userVO.getUserPw());
			userDetails.setUserVO(userVO);
			userDetails.getUserVO().setUserPw(null);
			
			// 사용자 권한 select해서 받아온 List<String> 객체 주입
			List<String> authList = this.restTemplateService.findUserAuthByUserId(inputUserId);
//			userDetails.setAuthorities(this.userDetailsDAO.selectUserAuthList(inputUserId));
			userDetails.setAuthorities(authList);
		}
		
		return userDetails;
	}

}
