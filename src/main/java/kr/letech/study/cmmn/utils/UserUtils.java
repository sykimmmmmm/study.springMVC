/**
 * 
 */
package kr.letech.study.cmmn.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.letech.study.cmmn.sec.vo.UserDetailsVO;

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
public class UserUtils {
	
	public static String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			Object principal = authentication.getPrincipal();
			if(principal instanceof UserDetailsVO) {
				return ((UserDetailsVO) principal).getUsername();
			}
		}
		return null;
	}
}
