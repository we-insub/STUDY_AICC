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
 * @file AuthorityController.java
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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.settings.dto.RoleForm;
import kr.co.aicc.modules.settings.service.AuthorityService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class AuthorityController {
	private final AuthorityService authorityService;
	private final CodeCacheService codeCacheService;
	
    /**
     * Settings > 권한관리
     * @return
     */
    @GetMapping("authority")
    public String authority(RoleForm roleForm, Model model, HttpServletRequest request, @CurrentAccount Account account) {
    	log.debug("권한 관리 메뉴  roleForm ::: " + roleForm);
    	List<RoleForm> roleList = authorityService.findRoleList(roleForm);
    	model.addAttribute("roleList", roleList);
        return "settings/authority";
    }
	
    /**
     * 권한 상세
     * @return
     */
    @GetMapping("roleDtl")
    @ResponseBody
    public HashMap<String,Object> roleDtl(RoleForm roleForm, Model model) {
    	log.debug("권한 상세  roleForm ::: " + roleForm);
    	List<RoleForm> roleList = authorityService.findRoleList(roleForm);	
    	log.debug("roleList.get(0) ::: " + roleList.get(0));

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	
    	map.put("roleDtl", roleList.get(0));
    	
        return map;
    }

    /**
     * 권한 등록
     * @return
     */
    @PostMapping("createRole")
    @ResponseBody
    public HashMap<String,Object> createRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("권한 등록  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "권한 등록");
    	String msg = "";

    	if (roleForm.getPrntRoleNo() == null) {
            errors.rejectValue("prntRoleNo", "");
    		msg += "<br/>상위권한을 선택해주세요.";
		}
    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	} 
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("권한 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
        
    	try {
    		roleForm.setCretr(account.getMemNo());
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.createRole(roleForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 등록실패");    		
    	}
        return map;
    }   
    
    /**
     * 권한 수정
     * @return
     */
    @PostMapping("updateRole")
    @ResponseBody
    public HashMap<String,Object> updateRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("권한 수정  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "권한 수정");
    	String msg = "";

//    	if (roleForm.getPrntRoleNo() == null) {
//            errors.rejectValue("prntRoleNo", "");
//    		msg += "<br/>상위권한을 선택해주세요.";
//		}
    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	} 
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("권한 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {    		
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.updateRole(roleForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정실패");    		
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 수정실패");    		
    	}
        return map;
    }

    /**
     * 최상위 권한 등록
     * @return
     */
    @PostMapping("createTopRole")
    @ResponseBody
    public HashMap<String,Object> createTopRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("최상위 권한 등록  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "최상위 등록");
    	String msg = "";

    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	} 
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("최상위 권한 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		roleForm.setCretr(account.getMemNo());
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.createTopRole(roleForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "최상위 등록실패");    		
    	}
        return map;
    }    
    
    /**
     * 권한 삭제
     * @return
     */
    @PostMapping("/deleteRole")
    @ResponseBody
    public HashMap<String,Object> deleteRole(RoleForm roleForm) {
    	log.debug("권한 삭제 roleForm ::::: " + roleForm);

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "권한 삭제");
    	
    	if (roleForm.getRoleNo() == null) {
        	map.put("msg", "삭제할 권한을 선택해주세요.");
            return map;
    	}
    	
    	if (authorityService.chkDeleteRoleYn(roleForm) > 0) {
        	map.put("msg", "해당 권한은 시스템 지정 권한으로 삭제할 수 없습니다. <br/>해당 권한의 삭제가 필요한 경우 관리자에게 문의해 주세요.");
            return map;
    	}
    	
    	if (authorityService.chkChildRoleYn(roleForm) > 0) {
        	map.put("msg", "하위 권한이 있습니다. 먼저 하위 권한을 정리 후 다시 시도 하세요.");
            return map;
    	}
    	
    	try {
	    	if (authorityService.deleteRole(roleForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 삭제실패");    		
    	}
    	
        return map;
    }
        
}
