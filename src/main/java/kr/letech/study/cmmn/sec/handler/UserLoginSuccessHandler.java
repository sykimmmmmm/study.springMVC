/**
 * 
 */
package kr.letech.study.cmmn.sec.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import kr.letech.study.cmmn.sec.vo.UserDetailsVO;
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
@Component
@Slf4j
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		HttpSession session = request.getSession(true);
		UserDetailsVO detailsVO = (UserDetailsVO) authentication.getPrincipal();
		session.setAttribute("userVO", detailsVO.getUserVO());
		
		//IP, 세션 ID
		WebAuthenticationDetails web = (WebAuthenticationDetails) authentication.getDetails();
		log.debug("IP : " + web.getRemoteAddress());
		log.debug("session Id : " + web.getSessionId());
		
		// 인증 ID
		log.debug("name : " + authentication.getName());
		
		// 권한 리스트
		List<GrantedAuthority> authList = (List<GrantedAuthority>) authentication.getAuthorities();
		StringBuffer sb = new StringBuffer();
		sb.append("권한 : ");
		for(int i = 0; i < authList.size(); i++) {
			sb.append(authList.get(i).getAuthority() + " ");
		}
		log.debug(sb.toString());
		
		// 디폴트 URI
		String uri = "/";
		
		// 강제 인터셉트 당했을 경우의 데이터 get
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		// 로그인 버튼 눌러 접속햇을 경우 의 데이터 get
		String prevPage = (String) request.getSession().getAttribute("prevPage");
		
		if(prevPage != null) {
			request.getSession().removeAttribute("prevPage");
		}
		
		// null이아니라면 강제 인터셉트
		if(savedRequest != null) {
			uri = savedRequest.getRedirectUrl();
		
		
		// "" 가아니라면 직접 로그인 페이지로 접속한것
		} else if( StringUtils.isNotBlank(prevPage)) {
			uri = prevPage;
		}
		
		// 세가지 케이스에 따른 uri 리다이렉트
		response.sendRedirect(uri);
		
	}
}
