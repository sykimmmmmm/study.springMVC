/**
 * 
 */
package kr.letech.study.cmmn.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * 
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
		InputStream in = null;
		
		try {
			FileVO targetVO = fileService.selectFileOne(fileVO);
			// 해당 파일 db에 존재하지않음
			if(targetVO == null) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			
			String filePath = fileService.makeFilePath(targetVO);
			String originNm = targetVO.getFileOriginNm();
			
			File file = new File(filePath);
			// 해당 파일 물리적 존재 x
			if(!file.exists()) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			// 해당 파일 mimetype 찾기
			int dotIndex = originNm.lastIndexOf('.');
			String formatName = "";
			if(dotIndex != -1) {
				formatName = originNm.substring(dotIndex+1).toLowerCase();
			}
			MediaType mType = getMediaType(formatName);
			
			// 파일 읽어오기
			in = new FileInputStream(file);
			
			// 글자깨짐 방지용 인코딩
			String encodedFileName = URLEncoder.encode(originNm, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
			
			// 헤더 정보 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mType);
			headers.add("Content-Disposition", "attachment; filename=\""+ encodedFileName + "\"");
			headers.setContentLength(file.length());
			
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(in),headers,HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			if(in != null) {
				IOUtils.closeQuietly(in);
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/view.do", method = RequestMethod.GET)
	public ResponseEntity<Object> imagePreview(FileVO fileVO){
		log.info("fileGrpId : {}",fileVO);
		ResponseEntity<Object> entity = null;
		FileVO targetVO = fileService.selectFileOne(fileVO);
		if(targetVO != null) {
			String path = fileService.makeFilePath(targetVO);
			File file = new File(path);
			if(!file.exists()) {
				entity = ResponseEntity.notFound().build();
			}else {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_PNG);
				Resource resource = new FileSystemResource(file);
				entity = new ResponseEntity<Object>(resource,headers,HttpStatus.OK);
			}
		}
		return entity;
	}
	

	/**
	 * @param formatName
	 * @return
	 */
	private MediaType getMediaType(String formatName) {
		if(formatName != null && !formatName.isEmpty()) {
			formatName = formatName.toLowerCase();
			switch (formatName) {
				case "jpg":
				case "jpeg": return MediaType.IMAGE_JPEG;
				case "png":  return MediaType.IMAGE_PNG;
				case "gif":  return MediaType.IMAGE_GIF;
				case "pdf":  return MediaType.APPLICATION_PDF;
				case "doc":
				case "docx": return MediaType.valueOf("application/msword"); // MS Word
				case "xls":
				case "xlsx": return MediaType.valueOf("application/vnd.ms-excel"); // MS Excel
				case "ppt":
				case "pptx": return MediaType.valueOf("application/vnd.ms-powerpoint"); // MS PowerPoint
				case "zip":  return MediaType.valueOf("application/zip"); // ZIP 압축 파일
				case "txt":  return MediaType.valueOf("text/plain"); // 텍스트 파일
				default : return MediaType.APPLICATION_OCTET_STREAM;
			}
		}
		return MediaType.APPLICATION_OCTET_STREAM;
	}

}
