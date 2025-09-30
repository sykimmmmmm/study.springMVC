/**
 * 
 */
package kr.letech.study.cmmn.file.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.file.dao.FileDAO;
import kr.letech.study.cmmn.file.service.IFileService;
import kr.letech.study.cmmn.file.vo.FileVO;
import kr.letech.study.cmmn.utils.UserUtils;
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
@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements IFileService {
	
	@Value("${file.upload.path}")
	private String basePath;
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private final FileDAO fileDAO;
	
	
	/**
	 * 파일을 특정위치에 저장 후 DB에 등록
	 * @param boFiles 클라이언트에서 보내진 파일배열
	 * @param fileGroupId 파일그룹아이디(없을시 Null)
	 * @param fileDiv 해당 파일을 저장하는 폴더를 구분하기위한 폴더명 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String insertFile(MultipartFile[] boFiles,String fileGroupId, String fileDiv) {
		// 업로드경로 생성 및 폴더생성
		String uploadPath = this.makeUploadPathAndMkdirs(fileDiv);
		// null 여부에따른 신규 생성 or 재사용
		String fileGrpId = StringUtils.isBlank(fileGroupId) ? UUID.randomUUID().toString() : fileGroupId;
		// 현재 로그인중인 유저아이디가져오기
		String userId = UserUtils.getUserId();
		
		try {
			for(MultipartFile file : boFiles) {
				if(StringUtils.isEmpty(file.getOriginalFilename())) {
					continue;
				}
				
				String saveNm = UUID.randomUUID().toString();
				this.saveFileToDisk(file,uploadPath,saveNm);
				
				FileVO fileVO = new FileVO();
				String originNm = file.getOriginalFilename();
				fileVO.setFileGrpId(fileGrpId);
				fileVO.setFileOriginNm(originNm);
				fileVO.setFileSaveNm(saveNm);
				fileVO.setFileDiv(fileDiv);
				fileVO.setFileSize(file.getSize());
				fileVO.setRgstId(userId);
				fileVO.setUpdtId(userId);
				fileDAO.insertFile(fileVO);
			}
		} catch (IOException e) {
			throw new RuntimeException("파일 처리중 에러 발생");
		}
		return fileGrpId;
	}
	
	/**
	 * 파일 그룹번호에 해당하는 모든 파일 논리삭제
	 * @param fileGrpId
	 */
	@Transactional
	@Override
	public void deleteFileAll(String fileGrpId) {
		FileVO fileVO = new FileVO();
		fileVO.setFileGrpId(fileGrpId);
		fileVO.setUpdtId(UserUtils.getUserId());
		fileDAO.deleteFileAll(fileVO);
	}
	
	/**
	 * fileGrpId, fileNo을 이용 특정 파일 논리삭제
	 * @param fileVO (fileGrpId, fileNo)
	 */
	@Transactional
	@Override
	public void deleteFileOne(FileVO fileVO) {
		fileVO.setUpdtId(UserUtils.getUserId());
		fileDAO.deleteFileOne(fileVO);
	}

	/**
	 * 해당 파일그룹번호에 속한 모든 파일 가져오기
	 * @param fileGrpId 파일그룹번호
	 */
	@Override
	public FileVO selectFileAll(String fileGrpId) {
		return fileDAO.selectFileAll(fileGrpId);
	}
	
	/**
	 * fileGrpId, fileNo을 이용 특정 파일 가져오기
	 * @param fileVO (fileGrpId, fileNo)
	 */
	@Override
	public FileVO selectFileOne(FileVO fileVO) {
		return fileDAO.selectFileOne(fileVO);
	}

	/**
	 * fileGrpId, fileNo을 이용해서 특정 파일 다운로드
	 */
	@Override
	public ResponseEntity<byte[]> downloadFile(FileVO fileVO) {
		InputStream in = null;
		
		try {
			FileVO targetVO = this.selectFileOne(fileVO);
			// 해당 파일 db에 존재하지않음
			if(targetVO == null) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			
			String filePath = this.makeFilePath(targetVO);
			String originNm = targetVO.getFileOriginNm();
			
			File file = new File(filePath);
			// 해당 파일 물리적 존재 x
			if(!file.exists()) {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
			
			MediaType mType = this.getMediaType(originNm);
			
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
	
	/**
	 * fileGrpId, fileNo을 이용해서 이미지 파일 정보 불러오기
	 */
	@Override
	public ResponseEntity<byte[]> imagePreview(FileVO fileVO) {
		ResponseEntity<byte[]> entity = null;
		InputStream in = null;
		try {
			FileVO targetVO = this.selectFileOne(fileVO);
			if(targetVO != null) {
				String path = this.makeFilePath(targetVO);
				
				File file = new File(path);
				if(!file.exists()) {
					entity = new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
				}else {
					HttpHeaders headers = new HttpHeaders();
					MediaType mType = this.getMediaType(targetVO.getFileOriginNm()); 
					headers.setContentType(mType);
					
					in = new FileInputStream(file);
					
					entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),headers,HttpStatus.OK);
				}
			}
		} catch (IOException e) {
			log.debug(e.getMessage());
			entity = new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);		
		}
		return entity;
	}
	
	/**
	 * 특정 파일이 존재하고있는 경로 생성
	 * @param fileVO (fileDiv, rgstDt, fileSaveNm)
	 */
	private String makeFilePath(FileVO fileVO) {
		StringBuilder sb = new StringBuilder();
//		log.info("rgst_dt : {}",fileVO.getRgstDt());
		try {
			sb.append(this.basePath)
			  .append("/")
			  .append(fileVO.getFileDiv())
			  .append("/")
			  .append(dateFormat.format(dateFormat.parse(fileVO.getRgstDt().substring(0,10).replace("-", "/"))))
			  .append("/")
			  .append(fileVO.getFileSaveNm());
		} catch (ParseException e) {
			return null;
		}
//		log.info(sb.toString());
		
		return sb.toString();
	}
	/**
	 * 파일 업로드경로 생성 및 폴더생성
	 * @param fileDiv 폴더 구분자
	 * @return
	 */
	private String makeUploadPathAndMkdirs(String fileDiv) {
		String uploadPath = basePath + "/" + fileDiv + "/" +dateFormat.format(new Date());
		
		File directory = new File(uploadPath);
		if(!directory.exists()) {
			directory.mkdirs();
		}

		return uploadPath;
	}
	
	/**
	 * 실제 디스크에 파일 업로드
	 * @param file
	 * @param uploadPath
	 * @param saveNm
	 * @throws IOException 
	 */
	private void saveFileToDisk(MultipartFile file, String uploadPath, String saveNm) throws IOException {
		File saveFile = new File(uploadPath + "/" + saveNm);
		FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
	}

	/**
	 * 확장자에 따라 미디어타입 가져오기
	 * 모르면 MediaType.APPLICATION_OCTET_STREAM 반환
	 * @param formatName
	 * @return
	 */
	private MediaType getMediaType(String originNm) {
		// 해당 파일 mimetype 찾기
		int dotIndex = originNm.lastIndexOf('.');
		
		String formatName = "";
		
		if(dotIndex != -1) {
			formatName = originNm.substring(dotIndex+1).toLowerCase();
		}
		
		if(StringUtils.isNotBlank(formatName)) {
			formatName = formatName.toLowerCase();
			switch (formatName) {
				case "jpg":
				case "jpeg": return MediaType.IMAGE_JPEG;
				case "png":  return MediaType.IMAGE_PNG;
				case "gif":  return MediaType.IMAGE_GIF;
				case "svg":  return MediaType.valueOf("image/svg+xml");
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
