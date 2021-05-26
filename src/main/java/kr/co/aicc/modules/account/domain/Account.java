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
 * @file Account.java
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "memId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memNo;
    private String memId;
    private String memNm;
    private String passwd;
    private String stat;
    private String statDtl;
    private String sex;
    private String brthday;
    private String gnrlTelNo;
    private String ptblTelNo;
    private String zipNo;
    private String baseAddr;
    private String dtlAddr;
    private String lastEdu;
    private String useModel;
    private String careerDesc;
    private LocalDateTime bgnRegDt;
    private LocalDateTime lastLgnDt;
    private LocalDateTime lastPasswdChgDt;
    private String wdlRsnCd;
    private String wdlRsnDtl;
    private String wdlDt;
    private String reRegYn;
    private String reRegDt;
    private String emailAuthYn;
    private LocalDateTime emailAuthDt;
    private String emailAuthTkn;
    private LocalDateTime emailAuthTknIssueDt;
    private String profImgData;
    private String fileNm;
    private String sysFileNm;
    private String thumbSysFileNm;
    private String filePath;
    private String reFilePath;
    private String fileSize;
    private Set<Role> memRoles = new HashSet<>();
    private String grpNm;

    public void generateEmailAuthToken() {
        this.emailAuthTkn = UUID.randomUUID().toString();
        this.emailAuthTknIssueDt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailAuthTkn.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailAuthTknIssueDt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public void completeSignUp() {
        this.emailAuthYn = "Y";
        this.emailAuthDt = LocalDateTime.now();
    }

    public boolean hasRole(String roleNm) {
        return memRoles.stream().filter(ro -> ro.getRoleNm().equals(roleNm)).findAny().isPresent();
    }


}
