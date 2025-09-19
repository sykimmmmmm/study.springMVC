/**
 * 
 */
package kr.letech.study.cmmn.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

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
@Mapper
public interface CodeDAO {

	/**
	 * @param codeGrp
	 * @return
	 */
	List<CodeVO> selectCodeList(String codeGrp);

}
