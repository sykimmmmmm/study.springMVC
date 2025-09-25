/**
 * 
 */
package kr.letech.study.cmmn.sec.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
public class LoginController {

	@RequestMapping(value = "/cmmn/sec/loginForm.do")
	public String loginForm(HttpServletRequest request) {
		
		// 요청 시점의 사용자 URI 정보를 Session의 Attribute에 담아서 전달 (잘 지워줘야함)
		// 로그인이 틀려서 다시 하면 요청 시점의 URI가 로그인 페이지가 되므로 조건문 설정
		String uri = request.getHeader("Referer");
		if(StringUtils.isNotBlank(uri) && !uri.contains("/cmmn/sec/loginForm.do") && !uri.contains("/cmmn/sec/loginProc.do")) {
			request.getSession().setAttribute("prevPage", request.getHeader("Referer"));
		}
		return "cmmn/sec/loginForm.popup";
	}
}
