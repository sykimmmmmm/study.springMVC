/**
 * 
 */
package kr.letech.study.cmmn.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

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
public class BaseTableVO {
	private String rgstId;
	private String rgstDt;
	private String updtId;
	private String updtDt;
	private String delYn;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
