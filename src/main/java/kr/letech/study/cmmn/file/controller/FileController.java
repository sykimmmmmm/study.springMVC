/**
 * 
 */
package kr.letech.study.cmmn.file.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.letech.study.cmmn.file.service.IFileService;
import kr.letech.study.cmmn.file.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 파일 다운로드 및 img태그에 이미지 정보 가져오는 컨트롤러
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-17  KSY			최초 생성
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value="/cmmn/file")
public class FileController {
	
	private final IFileService fileService;
	
	@ResponseBody
	@RequestMapping(value = "/download.do" , method = RequestMethod.GET)
	public ResponseEntity<byte[]> fileDownload(FileVO fileVO){
		ResponseEntity<byte[]> entity = fileService.downloadFile(fileVO);
		return entity;
	}
	
	@ResponseBody
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	public ResponseEntity<byte[]> imagePreview(FileVO fileVO){
		ResponseEntity<byte[]> entity = fileService.imagePreview(fileVO);
		return entity;
	}
	
}
