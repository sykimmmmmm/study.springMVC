/**
 * 
 */
package kr.letech.study.cmmn.file.dao;

import org.apache.ibatis.annotations.Mapper;

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
@Mapper
public interface FileDAO {

	/**
	 * @param fileVO
	 */
	void insertFile(FileVO fileVO);

	/**
	 * @param fileVO
	 * @return
	 */
	FileVO selectFileOne(FileVO fileVO);

	/**
	 * @param fileGrpId
	 * @return
	 */
	FileVO selectFileAll(String fileGrpId);

	/**
	 * @param fileVO
	 */
	void deleteFileOne(FileVO fileVO);

	/**
	 * @param fileVO
	 */
	void deleteFileAll(FileVO fileVO);

	/**
	 * @param fileVO
	 */
	void mergeFile(FileVO fileVO);

	/**
	 * @param fileGrpId
	 * @return
	 */
	int selectNextFileNo(String fileGrpId);

}
