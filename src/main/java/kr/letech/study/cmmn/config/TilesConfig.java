/**
 * 
 */
package kr.letech.study.cmmn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

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
public class TilesConfig {
	
	@Bean
	public UrlBasedViewResolver viewResolver() {
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		tilesViewResolver.setViewClass(TilesView.class);
		tilesViewResolver.setOrder(0);
		return tilesViewResolver;
	}
	
	@Bean
	public TilesConfigurer configurer() {
		final TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions("/WEB-INF/spring/tiles-define.xml");
		configurer.setCheckRefresh(true);
		return configurer;
	}
}
