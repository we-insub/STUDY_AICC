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
package kr.co.aicc.modules.account.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

@Data
public class SignUpForm {
	private Long memNo;
    private String memId;

    @Pattern(regexp = "[a-zA-Z0-9]{4,20}", message = "{SignUpForm.memIdF.Pattern}")
//	@Email(message = "{SignUpForm.memId.Email}")
    private String memIdF;
    
    @Pattern(regexp = "[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}", message = "{SignUpForm.memIdL.Pattern}")
    private String memIdL;

    @Pattern(regexp = "[가-힣a-zA-Z]{2,20}", message = "{SignUpForm.memNm.Pattern}")
    private String memNm;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "{SignUpForm.passwd.Pattern}")
    private String passwd;

    @NotBlank(message = "{SignUpForm.passwd.NotBlank}")
    private String passwd2;

    @Pattern(regexp = "[0-9]{4,4}+\\-[0-9]{2,2}+\\-[0-9]{2,2}", message = "{SignUpForm.brthday.Pattern}")
    private String brthday;
    
    private String brthdayY;
    private String brthdayM;
    private String brthdayD;

    @NotBlank(message = "{SignUpForm.sex.NotBlank}")
    private String sex;

    @NotBlank(message = "{SignUpForm.ptblTelNoF.NotBlank}")
    private String ptblTelNoF;

    @Pattern(regexp = "[0-9]{4,4}", message = "{SignUpForm.ptblTelNoM.Pattern}")
    private String ptblTelNoM;

    @Pattern(regexp = "[0-9]{4,4}", message = "{SignUpForm.ptblTelNoL.Pattern}")
    private String ptblTelNoL;

    @Pattern(regexp = "[0-9]{2,6}", message = "{SignUpForm.zipNo.Pattern}")
    private String zipNo;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}", message = "{SignUpForm.baseAddr.Pattern}")
    private String baseAddr;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}", message = "{SignUpForm.dtlAddr.Pattern}")
    private String dtlAddr;

    private String lastEdu;

    @NotBlank
    private String useModel;

    @Size(min=0, max=500, message = "{SignUpForm.careerDesc.Pattern}")
    private String careerDesc;

    @NotBlank
    private String terms;
    
    private String reqTerms;
    private String optTerms;
    private String profImgData;
    
    private MultipartFile picFile;
    private String fileData; 
}
