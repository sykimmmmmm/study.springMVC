/**
 * 
 */
package kr.letech.study.cmmn.file.vo;

import kr.letech.study.cmmn.vo.BaseTableVO;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class FileVO extends BaseTableVO{
	private String fileGrpId; 	// 파일 그룹 ID
    private int fileNo;			// 파일 SEQ
    private String fileOriginNm;	// 원본 파일명
    private String fileSaveNm;		// 변환 파일명
    private long fileSize;	// 파일크기
    private String fileDiv;		// 파일저장폴더
    
}
