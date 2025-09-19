package kr.letech.study.cmmn.sec.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import kr.letech.study.cmmn.sec.vo.UserDetailsVO;
import kr.letech.study.cmmn.user.vo.UserVO;
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
 *  2025-09-16  KSY			최초 생성
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {
	
	@Around("@annotation(kr.letech.study.cmmn.sec.aop.AuthCheck)")
	public Object checkAuth(ProceedingJoinPoint pjp) throws Throwable {
		String methodName = pjp.getSignature().getName();
        log.info("Before method: " + methodName);
        
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserVO userVO = null;
		if(authentication != null) {
			Object userDetails = authentication.getPrincipal();
			if(userDetails instanceof UserDetailsVO) {
				userVO =  ((UserDetailsVO) userDetails).getUserVO();				
			}
//			log.info("userVO : -> {}",userVO);
		}
		
		if(userVO == null) {
			return "redirect:/";
			
		}
		return pjp.proceed();
	}
}
