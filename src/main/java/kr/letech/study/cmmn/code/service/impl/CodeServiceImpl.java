/**
 * 
 */
package kr.letech.study.cmmn.code.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.letech.study.cmmn.code.dao.CodeDAO;
import kr.letech.study.cmmn.code.service.ICodeService;
import kr.letech.study.cmmn.code.vo.CodeVO;
import lombok.RequiredArgsConstructor;

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

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements ICodeService{
	private final CodeDAO codeDAO;
	
	@Override
	public List<CodeVO> selectCodeList(String codeGrp) {
		return codeDAO.selectCodeList(codeGrp);
	}
}
