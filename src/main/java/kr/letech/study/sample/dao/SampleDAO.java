/**
 * 
 */
package kr.letech.study.sample.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * <pre>
 * 
 * </pre>
 *  
 * << 개정이력 >>
 *   
 *  수정일      수정자		수정내용
 *  ------------------------------------------------
 *  2025-09-11  letech			최초 생성
 */
@Mapper
public interface SampleDAO {
	
	String selectNow();
}
