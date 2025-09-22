/**
 * 
 */
package kr.letech.study.cmmn.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
	@Override
	public String insertFile(MultipartFile[] boFiles,String fileGroupId, String fileDiv) {
		String uploadPath = basePath + "/" + fileDiv + "/" +dateFormat.format(new Date());
		String fileGrpId = null;
		
		File directory = new File(uploadPath);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		// fileGroupId 가 NULL로 들어오면 가지고있는 fileGroupId 가 존재하지 않음
		// fileGroupId 신규 생성
		if(StringUtils.isBlank(fileGroupId)) {
			fileGrpId = UUID.randomUUID().toString();
			
		}else {
			fileGrpId = fileGroupId;
		}
		
		FileVO fileVO = new FileVO();
		try {
			for(MultipartFile file : boFiles) {
				if(StringUtils.isEmpty(file.getOriginalFilename())) {
					continue;
				}
				String saveNm = UUID.randomUUID().toString();
				File saveFile = new File(uploadPath+"/"+saveNm);
				FileUtils.copyInputStreamToFile(file.getInputStream(), saveFile);
				
				String originNm = file.getOriginalFilename();
				fileVO.setFileGrpId(fileGrpId);
				fileVO.setFileOriginNm(originNm);
				fileVO.setFileSaveNm(saveNm);
				fileVO.setFileDiv(fileDiv);
				fileVO.setFileSize(file.getSize());
				fileVO.setRgstId(UserUtils.getUserId());
				fileVO.setUpdtId(UserUtils.getUserId());
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
	 * 특정 파일이 존재하고있는 경로 생성
	 * @param fileVO (fileDiv, rgstDt, fileSaveNm)
	 */
	@Override
	public String makeFilePath(FileVO fileVO) {
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
	
}
