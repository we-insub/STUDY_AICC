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
 * @package kr.co.aicc.modules.common.domain
 * @file Code.java
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
package kr.co.aicc.modules.settings.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "grpCd")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code extends Common implements Serializable {
//	private static final long serialVersionUID = 1L;
	private static final long serialVersionUID = 435940847476869630L;

	private String grpCd;
	private String grpCdNm;
	private String grpDesc;
	private String cd;
	private String cdNm;
	private String cdVal1;
	private String cdVal2;
	private String cdVal3;
	private String ord;
	private String useYn;
	private String cdDesc;
}
