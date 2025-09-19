/**
 * 
 */
package kr.letech.study.sample.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import kr.letech.study.sample.dao.SampleDAO;
import kr.letech.study.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
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
 *  2025-09-11  letech			최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SampleServiceImpl implements SampleService{
	
	private final SampleDAO sampleDAO;
	
	private final MessageSource messageSource;
	
	@Override
	public String selectNow() {
		log.info("messageSource : {}", messageSource.getMessage("button.search", null, null));
		
		return sampleDAO.selectNow();
	}
}
