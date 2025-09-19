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
	
	
	@Override
	public String insertFile(MultipartFile[] boFiles,String fileGroupId, String fileDiv) {
		String uploadPath = basePath + "/" + fileDiv + "/" +dateFormat.format(new Date());
		String fileGrpId = null;
		
		File directory = new File(uploadPath);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
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
	
	
	@Override
	public void deleteFileAll(String fileGrpId) {
		FileVO fileVO = new FileVO();
		fileVO.setFileGrpId(fileGrpId);
		fileVO.setUpdtId(UserUtils.getUserId());
		fileDAO.deleteFileAll(fileVO);
	}
	
	/**
	 * @param fileVO
	 */
	@Override
	public void deleteFileOne(FileVO fileVO) {
		fileVO.setUpdtId(UserUtils.getUserId());
		fileDAO.deleteFileOne(fileVO);
	}

	@Override
	public FileVO selectFileAll(String fileGrpId) {
		return fileDAO.selectFileAll(fileGrpId);
	}
	
	@Override
	public FileVO selectFileOne(FileVO fileVO) {
		return fileDAO.selectFileOne(fileVO);
	}

	@Override
	public String makeFilePath(FileVO fileVO) {
		StringBuilder sb = new StringBuilder();
		log.info("rgst_dt : {}",fileVO.getRgstDt());
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
		log.info(sb.toString());
		
		return sb.toString();
	}
	
}
