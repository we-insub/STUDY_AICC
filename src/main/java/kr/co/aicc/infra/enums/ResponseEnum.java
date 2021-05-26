/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.infra.enums
 * @file ResponseEnum.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 08	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.infra.enums;

import org.thymeleaf.util.StringUtils;

public class ResponseEnum {
	/** result status code */
	public enum Status {		
		SUCCESS("success", "성공"), ERROR("error", "실패");
		final private String value;
		final private String desc;
		
		private Status(String value, String desc) {
			this.value = value;
			this.desc = desc;
		}
		
		public String value() {
			return value;
		}
		
		public String desc() {
			return desc;
		}
		
		public boolean equals(String param) {
			return StringUtils.equalsIgnoreCase(this.value, param);
		}
		
		public static Status findValue(final String v) {
			Status find = null;
			for(Status val: values()) {
				if(v.equals(val.value())){
					find = val;
					break;
				}
			}
			return find;
		}	
	}

	/** Error code */
	public enum ErrorCode {
		AICC_0000_200("AICC.0000.200", "정상"),
		AICC_1000_400("AICC.1000.400", "BAD REQUEST"),
		AICC_1001_400("AICC.1001.400", "{0} - 데이터 포맷 또는 유효성 검증 오류"),
		AICC_1002_400("AICC.1002.400", "{0} - 필수 항목 오류"),
		AICC_1003_400("AICC.1003.400", "{0} - 숫자 타입 오류"),
		AICC_1004_400("AICC.1004.400", "{0} - 날짜 패턴 오류 ({1})"),
		AICC_1005_400("AICC.1005.400", "{0} - 최소길이 오류 (more than {1})"),
		AICC_1006_400("AICC.1006.400", "{0} - 최대길이 오류 (less than {1})"),
		AICC_1007_400("AICC.1007.400", "{0} - 유효값 오류 (more than {1},  less than {2})"),
		AICC_1008_400("AICC.1008.400", "{0} - 패턴 오류"),
		AICC_1009_400("AICC.1009.400", "{0} - 최소 Byte 길이 오류 (more than {1})"),
		AICC_1010_400("AICC.1010.400", "{0} - 최대 Byte 길이 오류 (less than {1})"),
		AICC_1011_400("AICC.1011.400", "{0} - 입력제한 오류 (input limit) {1}"),
		AICC_1012_400("AICC.1012.400", "{0} - 최소값 오류 (more than {1})"),
		AICC_1013_400("AICC.1013.400", "{0} - 최대값 오류 (less than {1})"),
		AICC_1014_400("AICC.1014.400", "{0} - 유효성조건 오류 (API 참조)"),
		AICC_1015_400("AICC.1015.400", "{0} - 정수 변환 오류"),
		AICC_0000_401("AICC.0000.401", "FORBIDDEN"),
		AICC_0000_403("AICC.0000.403", "UNAUTHORIZED"),
		AICC_0000_404("AICC.0000.404", "NOT FOUND"),
		AICC_9001_500("AICC.9001.500", "내부 서버 오류"),
		AICC_8001_500("AICC.8001.500", "세션 저장소 오류")

		;
		final private String value;
		final private String dtlNm;
		
		private ErrorCode(String value, String dtlNm) {
			this.value = value;			
			this.dtlNm = dtlNm;
		}
		
		public String value() {
			return value;
		}
		public String dtlNm() {
			return dtlNm;
		}
		
		public static ErrorCode findValue(final String v) {
			ErrorCode find = null;
			for(ErrorCode val: values()) {
				if(v.equals(val.value())){
					find = val;
					break;
				}
			}
			return find;
		}		
	}
}