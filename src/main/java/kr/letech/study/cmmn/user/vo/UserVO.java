/**
 * 
 */
package kr.letech.study.cmmn.user.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
 *  2025-09-12  KSY			최초 생성
 */
@Getter
@Setter
public class UserVO extends BaseTableVO{

	private String userId; 					// 유저 아이디
	private String userPw; 					// 유저 비밀번호
	private String userNm; 					// 유저 이름
	private String fileGrpId;				// 파일 그룹
	private List<String> authList; 			// 등록할 권한 목록
	private List<UserAuthVO> authVOList; 	// 가지고있는 권한 목록
	@JsonIgnore
	private MultipartFile[] boFiles; 		// 등록할 파일 리스트
	private FileVO fileVO; 					// 가지고있는 사진 하나
	private String deleteFileNo; 			// 하나의 사진만 관리하고있기때문에 삭제할 파일넘버
	
}
