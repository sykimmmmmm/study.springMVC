/**
 * 
 */
package kr.letech.study.cmmn.board.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.letech.study.cmmn.file.vo.FileVO;
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
 *  2025-09-16  KSY			최초 생성
 */
@Getter
@Setter
public class BoardVO extends BaseTableVO{
	private int boardId;
	private String boardTitle;
	private String boardContent;
	private String fileGrpId;
	
	private int[] deleteBoardNos;
	private MultipartFile[] boFiles;
	private int[] deleteFileNos; 
	private List<FileVO> fileList;
	private List<ReplyVO> replyList;

	
}
