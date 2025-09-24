/**
 * 
 */
package kr.letech.study.cmmn.file.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.file.vo.FileVO;

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
public interface IFileService {

	/**
	 * @param boFiles
	 * @param fileGroupId
	 * @param fileDiv
	 * @return
	 */
	String insertFile(MultipartFile[] boFiles, String fileGroupId, String fileDiv);

	/**
	 * @param fileVO (fileGrpId, fileNo)
	 * @return
	 */
	FileVO selectFileOne(FileVO fileVO);

	/**
	 * @param fileGrpId 그룹아이디로 파일검색
	 * @return
	 */
	FileVO selectFileAll(String fileGrpId);

	/**
	 * @param fileGrpId (fileGrpId)
	 */
	void deleteFileAll(String fileGrpId);

	/**
	 * @param fileVO (fileGrpId, fileNo)
	 */
	void deleteFileOne(FileVO fileVO);

	/**
	 * @param fileVO
	 * @return
	 */
	ResponseEntity<byte[]> downloadFile(FileVO fileVO);

	/**
	 * @param fileVO
	 * @return
	 */
	ResponseEntity<byte[]> imagePreview(FileVO fileVO);

}
