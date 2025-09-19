/**
 * 
 */
package kr.letech.study.cmmn.code.service;

import java.util.List;

import kr.letech.study.cmmn.code.vo.CodeVO;

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
public interface ICodeService {
	List<CodeVO> selectCodeList(String codeGrp);
}
