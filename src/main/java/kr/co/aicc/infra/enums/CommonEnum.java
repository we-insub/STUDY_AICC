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
 * @file CommonEnum.java
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

import org.apache.commons.lang3.StringUtils;

public class CommonEnum {

	/** Group Code */
	public enum GroupCode {
		MEM_STAT,MEM_STAT_DTL,SEX,WDL_RSN,TNC_GB,
		EDU,MODEL,EMAIL_ADDR,RES_GB,RES_METHOD,
		MEM_GRP,FILE_GB,MEM_SCHED_GB,TEL_NO
		;
	}

	/** MemStat */
	public enum MemStat {
		MEM_NORMAL("01", "정상"),
		MEM_STOP("02","정지"),
		MEM_WDL("03","탈퇴"),
		MEM_WAIT("04", "승인대기"),
		;
		final private String value;
		final private String desc;

		MemStat(String value, String desc) {
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

		public static MemStat findValue(final String v) {
			MemStat find = null;
			for(MemStat val: values()) {
				if(v.equals(val.value())){
					find = val;
					break;
				}
			}
			return find;
		}
	}

	/** default Role */
	public enum Role {
		ROLE_USER("ROLE_USER", "일반회원"),
		ROLE_ADMIN("ROLE_ADMIN","관리자"),
		ROLE_MANAGER("ROLE_MANAGER","매니저"),
		ROLE_WATCH("ROLE_WATCH","관전자")
		;
		final private String value;
		final private String desc;

		Role(String value, String desc) {
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

		public static Role findValue(final String v) {
			Role find = null;
			for(Role val: values()) {
				if(v.equals(val.value())){
					find = val;
					break;
				}
			}
			return find;
		}
	}

}
