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
 * @package kr.co.aicc.modules.account.domain
 * @file Resources.java
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
package kr.co.aicc.modules.account.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = {"roleSet"})
@EqualsAndHashCode(of = "resNo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resources extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long resNo;
    private Long prntResNo;
    private String useYn;
    private String dispYn;
    private String resGb;
    private String resNm;
    private String resDesc;
    private String resType;
    private String resMeth;
    private String resUrl;
    private String resLvl;
    private int ord;

    private Resources prntRes;
    private Set<Role> roleSet = new HashSet<>();

}
