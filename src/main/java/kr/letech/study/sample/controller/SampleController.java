/**
 * 
 */
package kr.letech.study.sample.controller;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
@Controller
@Slf4j
@RequiredArgsConstructor
public class SampleController {
	
	private final SampleService sampleService;
	
	private final MessageSource messageSource;
	
	@Value("${sample.value}")
	private String sample;

	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public String home(Model model) {
		log.info("TestController sample.value: {}",sample);
		log.info("messageSource : {}", messageSource.getMessage("button.search", null,null));
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate);
		model.addAttribute("sampleNow", sampleService.selectNow());
		
		return "sample/sample.tiles";
	}
}
