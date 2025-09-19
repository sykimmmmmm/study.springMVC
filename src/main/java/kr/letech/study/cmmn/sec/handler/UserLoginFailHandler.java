/**
 * 
 */
package kr.letech.study.cmmn.sec.handler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
@Component
public class UserLoginFailHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		if(exception instanceof AuthenticationServiceException) {
			request.setAttribute("loginFailMsg", "존재하지 않은 사용자입니다.");
		
		}else if(exception instanceof BadCredentialsException) {
			request.setAttribute("loginFailMsg", "아이디 혹은 비밀번호가 틀립니다.");
			
		}else if(exception instanceof LockedException) {
			request.setAttribute("loginFailMsg", "잠긴계정입니다.");
			
		}else if(exception instanceof DisabledException) {
			request.setAttribute("loginFailMsg", "비활성화된 계정입니다.");
			
		}else if(exception instanceof AccountExpiredException) {
			request.setAttribute("loginFailMsg", "만료된 계정입니다.");
			
		}else if(exception instanceof CredentialsExpiredException) {
			request.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
			
		}
		
		//로그인 페이지로 다시 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/cmmn/sec/loginForm.do");
		dispatcher.forward(request, response);
		
	}

}
