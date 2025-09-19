/**
 * 
 */
package kr.letech.study.cmmn.sec.provider;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kr.letech.study.cmmn.sec.service.CustomUserDetailsService;
import kr.letech.study.cmmn.sec.vo.UserDetailsVO;
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
@Component
@RequiredArgsConstructor
public class UserLoginAuthenticationProvider implements AuthenticationProvider{
	private final CustomUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * 로그인 처리
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 사용자가 입력한 정보
		String userId = authentication.getName();
		String userPw = (String) authentication.getCredentials();
		
		UserDetailsVO userDetails = (UserDetailsVO) this.userDetailsService.loadUserByUsername(userId);
		
		// 인증 진행
		// DB에 정보가 없는 경우 예외 발생 (아이디/ 패스워드 잘못됐을 때와 동일한 것이 좋음)
		// ID 및 PW 체크해서 안맞을 경우 (match를 이용한 암호화 체크를 해야함)
		if(userDetails == null || !userId.equals(userDetails.getUsername())
				|| !this.passwordEncoder.matches(userPw, userDetails.getPassword())) {
			throw new BadCredentialsException(userId);
			
		//잠긴계정
		}else if (!userDetails.isAccountNonLocked()) {
			throw new LockedException(userId);
			
		//비활성화
		}else if (!userDetails.isEnabled()) {
			throw new DisabledException(userId);

		// 만료된 계정	
		}else if(!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException(userId);
			
		// 비밀번호가 만료된 계정	
		}else if (!userDetails.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(userId);
		}
		
		// 패스워드 정보는 지워줌 (객체를 계속 사용해야 하므로)
		userDetails.setPassword(null);
		
		Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
		return newAuth;
	}

	/**
	 * 로그인 처리 지원 여부
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		// security가 요구하는 UsernamePasswordAuthenticationToken 타입이 맞는지 확인
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
