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
 * @package kr.co.aicc.modules.settings.controller
 * @file MemberController.java
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
package kr.co.aicc.modules.settings.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.settings.dto.MemberForm;
import kr.co.aicc.modules.settings.service.MemberService;
import kr.co.aicc.modules.settings.validator.MemberFormValidator;
import kr.co.aicc.infra.util.PaginationInfo;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class MemberController {
    private final MemberFormValidator memberFormValidator;
    private final MemberService memberService;
    private final CodeCacheService codeCacheService;
    
    /**
     * 회원관리 화면
     * @return
     */
    @GetMapping("/member")
    public String memberView(MemberForm memberForm, Model model, HttpServletRequest request, @CurrentAccount Account account) {
    	log.debug("회원관리 조회 조건 " + memberForm);
		
        model.addAttribute("cmnMemStat", codeCacheService.getCode(CommonEnum.GroupCode.MEM_STAT.name()));	//회원상태
        model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));	//성별
        model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
        model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
        model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종
        model.addAttribute("roleList", codeCacheService.findRoleList());	//권한리스트
        return "settings/member";
    }

    /**
     * 회원관리 조회
     * @return
     */
    @PostMapping("/memberList")
    public String memberList(MemberForm memberForm, Model model) {

    	log.debug("회원관리 조회 조건 000 " + memberForm);
    	
		List<MemberForm> memberList =  memberService.selectMemList(memberForm);
		int memberListCnt =  memberService.selectMemListCnt(memberForm);

    	log.debug("회원관리 조회 조건 " + memberForm);
    	log.debug("memberListCnt " + memberListCnt);
    	log.debug("회원관리 조회 List " + memberList);

    	PaginationInfo pagination = new PaginationInfo();
    	pagination.setCurrentPageNo(memberForm.getPageNo());
    	pagination.setRecordCountPerPage(10);
    	pagination.setPageSize(5);
    	pagination.setTotalRecordCount(memberListCnt);

    	model.addAttribute("cnt", memberListCnt);
    	model.addAttribute("pagination", pagination);
    	model.addAttribute("memberList", memberList);
        model.addAttribute("cmnMemStat", codeCacheService.getCode(CommonEnum.GroupCode.MEM_STAT.name()));	//회원상태
        model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));	//성별
        model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
        model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
        model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종
        model.addAttribute("roleList", codeCacheService.findRoleList());	//권한리스트
        return "settings/memberTmp";
    }

    /**
     * 회원정보 상세
     * @return
     */
    @GetMapping("/memberDtl")
    @ResponseBody
    public HashMap<String, Object> memberDtl(MemberForm memberForm, Model model) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		List<MemberForm> memberList = memberService.selectMemList(memberForm);
		log.debug("memberList.get(0) " + memberList.get(0));
		
		map.put("member", memberList.get(0));
		
        return map;
    }

    /**
     * 회원정보 수정
     * @return
     */
    @PostMapping("/memberUpdate")
    @ResponseBody
    public HashMap<String, Object> memberUpdate(@Valid MemberForm memberForm, Errors errors, Model model, @CurrentAccount Account account) {
		log.debug("memberForm :::: " + memberForm);

		HashMap<String, Object> map = new HashMap<String, Object>();
    	map.put("title", "회원정보 수정");
    	String msg = "";
    	
        if (errors.hasErrors()) {        	
    		log.debug("회원정보 수정 : 에러발생 :: " + errors);
    		if (errors.hasFieldErrors("memId")) {
        		msg += "<br/>회원을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("memNm")) {
        		msg += "<br/>성명은 한글,영문을 이용한 2자~20자로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("brthday")) {
        		msg += "<br/>생년월일을 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("sex")) {
        		msg += "<br/>성별을 체크해주세요.";
    		}
    		if (errors.hasFieldErrors("role")) {
        		msg += "<br/>권한을 체크해주세요.";
    		}
    		if (errors.hasFieldErrors("ptblTelNo")) {
        		msg += "<br/>휴대폰번호 11자리를 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("zipNo")) {
        		msg += "<br/>우편번호를 6자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("baseAddr")) {
        		msg += "<br/>주소를 100자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("dtlAddr")) {
        		msg += "<br/>나머지주소를 100자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("lastEdu")) {
        		msg += "<br/>최종학력을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("useModel")) {
        		msg += "<br/>사용기종을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("stat")) {
        		msg += "<br/>승인여부를 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("careerDesc")) {
        		msg += "<br/>경력사항을 500자내로 입력해주세요.";
    		}
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	//수정
        memberForm.setCretr(account.getMemNo());
        memberForm.setChgr(account.getMemNo());
		if (memberService.updateMember(memberForm) > 0 ) {
        	map.put("result", "success");
			map.put("msg", "수정되었습니다.");
		} else {
			map.put("msg", "수정할 정보가 없습니다.");
		}
    	
        return map;
    }

    /**
     * 회원정보 삭제
     * @return
     */
    @PostMapping("/memberDelete")
    @ResponseBody
    public HashMap<String, Object> memberDelete(MemberForm memberForm, Model model, @CurrentAccount Account account) {

		HashMap<String, Object> map = new HashMap<String, Object>();
    	map.put("title", "회원정보 삭제");

        memberForm.setChgr(account.getMemNo());
		if (memberService.deleteMember(memberForm) > 0 ) {
        	map.put("result", "success");
			map.put("msg", "탈퇴되었습니다.");
		} else {
			map.put("msg", "탈퇴할 정보가 없습니다.");
		}

        return map;
    }


    /**
     * memberForm validation
     * @param webDataBinder
     */
    @InitBinder("memberForm")
    public void initBinderMemberForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberFormValidator);
    } 
}
