/**
 * 
 */
package kr.letech.study.cmmn.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.letech.study.cmmn.sec.handler.UserLoginFailHandler;
import kr.letech.study.cmmn.sec.handler.UserLoginSuccessHandler;
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
 *  2025-09-30  KSY			최초 생성
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserLoginSuccessHandler loginSuccessHandler;
	private final UserLoginFailHandler loginFailHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// csrf 비활성화
		http.csrf(csrf -> csrf.disable());
		
		// auth 기반 로그인창 설정 비활성화
		http.httpBasic(basic -> basic.disable());
		
		// 요청 처리
		http.authorizeRequests(authorize -> 
							//서버 내부에서 포워드 혹은 비동기 방식은 바로 통과
//				authorize.dispatcherTypeMatchers(DispatcherType.FORWARD,DispatcherType.ASYNC).permitAll()
							// resources안에 /js, /css 등에 있는 기본적으로 설정된 static요청들 허용
				authorize.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						 // /resources 폴더 안에있는 모든 자원 허용
						 .antMatchers("/resources/**").permitAll()
						 	// 로그인폼, 회원가입 폼 비로그인유저만 가능
						 .antMatchers("/cmmn/sec/loginForm.do","/cmmn/user/userRegisterForm.do").anonymous()
						 .antMatchers("/admin/**").hasRole("admin")
		);
		
		// 로그인 방식 처리
		http.formLogin(form -> 
				form.usernameParameter("userId")
					.passwordParameter("userPw")
					.loginProcessingUrl("/cmmn/sec/loginProc.do")
					.loginPage("/cmmn/sec/loginForm.do")
					.successHandler(loginSuccessHandler)
					.failureHandler(loginFailHandler)
		);
		
		// 로그아웃 처리
		http.logout(logout ->
				logout.logoutUrl("/cmmn/sec/logout.do")
					  .logoutSuccessUrl("/")
					  .invalidateHttpSession(true)
					  .deleteCookies("JSESSIONID")
		);
		
		// 권한에 위배되는 페이지 접속시 메인페이지로 이동
		http.exceptionHandling(exception ->
				exception.accessDeniedPage("/")
		);
		
		http.sessionManagement(session -> 
				// 세션만료시 메인페이지로 이동
				session.sessionConcurrency(concurreny -> concurreny.expiredUrl("/"))
		);
		
		return http.build();
	}
	
	/**
	 * 데이터베이스기반 사용자 인증정보 비교
	 * @param http
	 * @param bCryptPasswordEncoder
	 * @param detailsService
	 * @return
	 */
	@Bean
	protected AuthenticationManager authenticationManager(HttpSecurity http
			, BCryptPasswordEncoder bCryptPasswordEncoder
			, UserDetailsService detailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(detailsService);
		authProvider.setPasswordEncoder(bCryptPasswordEncoder);
		return new ProviderManager(authProvider);
	}
}
