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
 * @package kr.co.aicc.modules.account.dto
 * @file SignUpForm.java
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
package kr.co.aicc.modules.settings.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.*;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.settings.domain.MemGrp;

@Data
public class MemberForm extends Common {
    //검색조건
    private String pMemId;
    private String pMemNm;
    private String[] pStat;
    private String pPtblTelNo;
    private String pSrchRegDt;
    private Long[] pRole;
    private List<MemGrp> grpMembers = new ArrayList<>();
    
    @NotNull
    private Long memNo;
    @NotBlank
    private String memId;
    
    @Pattern(regexp = "[가-힣a-zA-Z]{2,20}")
    private String memNm;
    
    @Pattern(regexp = "[0-9]{4,4}+\\-[0-9]{2,2}+\\-[0-9]{2,2}")
    private String brthday;
    
    @NotBlank
    private String sex;
    private String sexNm;
    
    @Pattern(regexp = "(01[0-9])(\\d{4})(\\d{4})") 
    private String ptblTelNo;

    @Pattern(regexp = "[0-9]{2,6}")
    private String zipNo;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}")
    private String baseAddr;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}")
    private String dtlAddr;    
    
    private String memRolesString;
    
	@NotBlank
    private String role;

    @NotBlank
    private String lastEdu;
    
    @NotBlank
    private String useModel;
    
    @NotBlank
    private String stat;
    private String statNm;

    @Size(min=0, max=500)
    private String careerDesc;
    
    private int age;
    
    private String fullAddr;
    private String bgnRegDt;
    private String wdlRsnCd;
    private List<Role> memRoles = new ArrayList<>();

	private int pageNo = 1;
	private int pageRowCont = 10;
    @Getter(AccessLevel.NONE)
	private int startIdx;
	@Getter(AccessLevel.NONE)
	private int endIdx;
	
	public int getStartIdx() {
		return (pageNo-1)*pageRowCont +1;
	}

	public int getEndIdx() {
		return pageNo * pageRowCont;
	}
}
