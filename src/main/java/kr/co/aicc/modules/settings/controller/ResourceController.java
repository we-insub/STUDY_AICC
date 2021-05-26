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
 * @file ResourceController.java
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
import kr.co.aicc.modules.settings.dto.ResourceDto;
import kr.co.aicc.modules.settings.service.ResourceService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class ResourceController {
	private final ResourceService resourceService;
	private final CodeCacheService codeCacheService;
	
    /**
     * Settings > 메뉴관리
     * @return
     */
    @GetMapping("resource")
    public String authority(ResourceDto resourceDto, Model model, HttpServletRequest request, @CurrentAccount Account account) {
    	log.debug("메뉴 관리  resourceDto ::: " + resourceDto);
    	List<ResourceDto> resList = resourceService.findResList(resourceDto);
        model.addAttribute("roleList", codeCacheService.findRoleList());	//권한리스트    	
    	model.addAttribute("resList", resList);
        return "settings/resource";
    }
	
    /**
     * 메뉴 권한
     * @return
     */
    @GetMapping("resourceRole")
    public String resourceRole(ResourceDto resourceDto, Model model, HttpServletRequest request) {
    	log.debug("메뉴 권한  resourceDto ::: " + resourceDto);
    	List<ResourceDto> resList = resourceService.findResList(resourceDto);
    	log.debug("resList.get(0) ::: " + resList.get(0));

        model.addAttribute("resRoleList", codeCacheService.findRoleList());	//권한리스트
    	model.addAttribute("resInfo", resList.get(0));
        return "settings/resourceRoleTmp";
    }
    
    /**
     * 메뉴 순서 UP
     * @return
     */
    @PostMapping("resOrdUp")
    @ResponseBody
    public HashMap<String,Object> resOrdUp(ResourceDto resourceDto, @CurrentAccount Account account) {
    	log.debug("메뉴 순서 UP  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "메뉴 순서 변경");

    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.resOrdUp(resourceDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "순서 변경 완료");
	    		
	    	} else {
	        	map.put("msg", "상위메뉴로 변경이 불가합니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "순서 변경 ERROR");    		
    	}
        return map;
    }   
    
    /**
     * 메뉴 순서 DOWN
     * @return
     */
    @PostMapping("resOrdDown")
    @ResponseBody
    public HashMap<String,Object> resOrdDown(ResourceDto resourceDto, @CurrentAccount Account account) {
    	log.debug("메뉴 순서 DOWN  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "메뉴 순서 변경");

    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.resOrdDown(resourceDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "순서 변경 완료");
	    		
	    	} else {
	        	map.put("msg", "하위메뉴로 변경이 불가합니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "순서 변경 ERROR");    		
    	}
        return map;
    }
    
    /**
     * 메뉴 등록
     * @return
     */
    @PostMapping("createRes")
    @ResponseBody
    public HashMap<String,Object> createRes(ResourceDto resourceDto, Errors errors, @CurrentAccount Account account) {
    	log.debug("메뉴 등록  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "메뉴 등록");
    	String msg = "";
    	
    	if ("".equals(resourceDto.getResNm()) || resourceDto.getResNm() == null) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getResUrl()) || resourceDto.getResUrl() == null) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getRole()) || resourceDto.getRole() == null) {
            errors.rejectValue("role", "");
    		msg += "<br/>권한을 선택해주세요.";
    	}
    	if ("".equals(resourceDto.getOrd()) || resourceDto.getOrd() == 0) {
            errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getDispYn()) || resourceDto.getDispYn() == null) {
            errors.rejectValue("ord", "");
    		msg += "<br/>노출여부를 선택해주세요.";
    	}

    	if (resourceDto.getResNm().length() > 20) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 20자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResUrl().length() > 100) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 100자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResMeth().length() > 10) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>메소드 타입을 10자 내로 입력해주세요.";
    	}
    	if (resourceDto.getOrd() > 99) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>정렬번호를 100이하로 입력해주세요.";
    	}

        if (errors.hasErrors()) {
    		log.debug("메뉴 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.createRes(resourceDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "메뉴 등록실패");    		
    	}
        return map;
    }   
    
    /**
     * 메뉴 수정
     * @return
     */
    @PostMapping("updateRes")
    @ResponseBody
    public HashMap<String,Object> updateRes(ResourceDto resourceDto, Errors errors, @CurrentAccount Account account) {
    	log.debug("메뉴 수정  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "메뉴 수정");
    	String msg = "";
    	
    	if ("".equals(resourceDto.getResNm()) || resourceDto.getResNm() == null) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getResUrl()) || resourceDto.getResUrl() == null) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getRole()) || resourceDto.getRole() == null) {
            errors.rejectValue("role", "");
    		msg += "<br/>권한을 선택해주세요.";
    	}
    	if ("".equals(resourceDto.getOrd()) || resourceDto.getOrd() == 0) {
            errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getDispYn()) || resourceDto.getDispYn() == null) {
            errors.rejectValue("ord", "");
    		msg += "<br/>노출여부를 선택해주세요.";
    	}

    	if (resourceDto.getResNm().length() > 20) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 20자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResUrl().length() > 100) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 100자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResMeth().length() > 10) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>메소드 타입을 10자 내로 입력해주세요.";
    	}
    	if (resourceDto.getOrd() > 99) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>정렬번호를 100이하로 입력해주세요.";
    	}

        if (errors.hasErrors()) {
    		log.debug("메뉴 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {    		
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.updateRes(resourceDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정실패");    		
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "메뉴 수정실패");    		
    	}
        return map;
    }

    /**
     * 메뉴 삭제
     * @return
     */
    @PostMapping("/deleteRes")
    @ResponseBody
    public HashMap<String, Object> deleteRes(ResourceDto resourceDto) {
    	log.debug("메뉴 삭제  resourceDto ::: " + resourceDto);

		HashMap<String, Object> map = new HashMap<String, Object>();
    	map.put("title", "메뉴 삭제");
		
		if (resourceService.deleteRes(resourceDto) > 0 ) {
        	map.put("result", "success");
			map.put("msg", "삭제되었습니다.");
		} else {
			map.put("msg", "삭제할 정보가 없습니다.");
		}

        return map;
    }    
}
