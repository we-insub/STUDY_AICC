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
 * @file CodeController.java
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

import kr.co.aicc.infra.util.PaginationInfo;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.settings.domain.Code;
import kr.co.aicc.modules.settings.dto.DtlCode;
import kr.co.aicc.modules.settings.dto.GrpCode;
import kr.co.aicc.modules.settings.service.CodeService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class CodeController {
	private final CodeService codeService;
	private final CodeCacheService codeCacheService;
	
    /**
     * Settings > 공통코드관리
     * @return
     */
    @GetMapping("/code")
    public String code(GrpCode grpCode, DtlCode dtlCode, Model model, HttpServletRequest request, @CurrentAccount Account account) {
        return "settings/code";
    }

    /**
     * 그룹코드 리스트
     * @return
     */
    @GetMapping("/grpCodeList")
    public String grpCodeList(GrpCode grpCode, Model model) {
    	
    	List<GrpCode> grpList = codeService.findCodeGrpList(grpCode);
		int grpListCnt =  codeService.findCodeGrpListCnt(grpCode);		
    	log.debug("grpCode ::: " + grpCode);
    	log.debug("grpList ::: " + grpList);

    	PaginationInfo paginationGrp = new PaginationInfo();
    	paginationGrp.setCurrentPageNo(grpCode.getPageNo());
    	paginationGrp.setRecordCountPerPage(10);
    	paginationGrp.setPageSize(5);
    	paginationGrp.setTotalRecordCount(grpListCnt);

    	model.addAttribute("grpListCnt", grpListCnt);
    	model.addAttribute("paginationGrp", paginationGrp);
    	model.addAttribute("grpList", grpList);
    	
        return "settings/grpCodeTmp";
    }

    /**
     * 그룹코드 리스트
     * @return
     */
    @GetMapping("/dtlCodeList")
    public String dtlCodeList(DtlCode dtlCode, Model model) {
    	
    	List<DtlCode> dtlList = codeService.findCodeDtlList(dtlCode);
		int dtlListCnt =  codeService.findCodeDtlListCnt(dtlCode);
    	log.debug("dtlCode ::: " + dtlCode);
    	log.debug("dtlList ::: " + dtlList);
    	
    	PaginationInfo paginationDtl = new PaginationInfo();
    	paginationDtl.setCurrentPageNo(dtlCode.getPageNo());
    	paginationDtl.setRecordCountPerPage(10);
    	paginationDtl.setPageSize(5);
    	paginationDtl.setTotalRecordCount(dtlListCnt);
    	
    	model.addAttribute("dtlListCnt", dtlListCnt);
    	model.addAttribute("paginationDtl", paginationDtl);
    	model.addAttribute("dtlList", dtlList);
    	
        return "settings/dtlCodeTmp";
    }
    
    /**
     * 그룹코드 상세
     * @return
     */
    @GetMapping("/grpCodeDtl")
    @ResponseBody
    public HashMap<String,Object> grpCodeDtl(GrpCode grpCode) {
    	log.debug("그룹코드 상세 code ::::: " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	GrpCode grpCodeDtl = codeService.findCodeGrpList(grpCode).get(0);
    	
    	log.debug("grpCodeDtl ::::: " + grpCodeDtl);
    	map.put("grpCodeDtl", grpCodeDtl);
        return map;
    }
    
    /**
     * 코드 상세
     * @return
     */
    @GetMapping("/codeDtl")
    @ResponseBody
    public HashMap<String,Object> codeDtl(DtlCode dtlcode) {
    	log.debug("코드 상세 code ::::: " + dtlcode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	DtlCode codeDtl = codeService.findCodeDtlList(dtlcode).get(0);
    	
    	log.debug("codeDtl ::::: " + codeDtl);
    	map.put("codeDtl", codeDtl);
        return map;
    }
    
    /**
     * 그룹코드 등록
     * @return
     */
    @PostMapping("/createCodeGrp")
    @ResponseBody
    public HashMap<String,Object> createCodeGrp(GrpCode grpCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("그룹코드 등록 grpCode ::::: " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "그룹코드 등록");
    	String msg = "";
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
            errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>그룹코드를 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpGrpCdNm()) || grpCode.getGrpGrpCdNm() == null) {
            errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpUseYn()) || grpCode.getGrpUseYn() == null) {
            errors.rejectValue("grpUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (grpCode.getGrpGrpCd().length() > 20) {
        	errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>그룹코드를 20자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpGrpCdNm().length() > 50) {
        	errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 50자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpDesc().length() > 200) {
        	errors.rejectValue("grpGrpDesc", "");
    		msg += "<br/>그룹코드 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("그룹코드 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		grpCode.setCretr(account.getMemNo());
    		grpCode.setChgr(account.getMemNo());
	    	if (codeService.createCodeGrp(grpCode) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 등록실패");    		
    	}
    	
        return map;
    }
    
    /**
     * 상세코드 등록
     * @return
     */
    @PostMapping("/createCodeDtl")
    @ResponseBody
    public HashMap<String,Object> createCodeDtl(DtlCode dtlCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("상세코드 등록 dtlCode ::::: " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "상세코드 등록");
    	String msg = "";
    	
    	if ("".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>그룹코드를 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getCd()) || dtlCode.getCd() == null) {
        	errors.rejectValue("cd", "");
    		msg += "<br/>상세코드를 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getCdNm()) || dtlCode.getCdNm() == null) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getDtlUseYn()) || dtlCode.getDtlUseYn() == null) {
        	errors.rejectValue("dtlUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (dtlCode.getDtlGrpCd().length() > 20) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>그룹코드를 20자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCd().length() > 10) {
        	errors.rejectValue("cd", "");
    		msg += "<br/>상세코드를 10자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdNm().length() > 200) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 200자 내로 입력해주세요.";
    	}
    	if (dtlCode.getOrd() > 99) {
        	errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 100 미만으로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal1().length() > 50) {
        	errors.rejectValue("cdVal1", "");
    		msg += "<br/>기타1을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal2().length() > 50) {
        	errors.rejectValue("cdVal2", "");
    		msg += "<br/>기타2을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal3().length() > 50) {
        	errors.rejectValue("cdVal3", "");
    		msg += "<br/>기타3을 50자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("상세코드 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		dtlCode.setCretr(account.getMemNo());
    		dtlCode.setChgr(account.getMemNo());
	    	if (codeService.createCodeDtl(dtlCode) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    	}    		
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "상세코드 등록실패");    		
    	}

        return map;
    }    
    
    
    /**
     * 그룹코드 수정
     * @return
     */
    @PostMapping("/updateCodeGrp")
    @ResponseBody
    public HashMap<String,Object> updateCodeGrp(GrpCode grpCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("그룹코드 수정 grpCode ::::: " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "그룹코드 수정");
    	String msg = "";
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
            errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>수정할 그룹코드를 선택해주세요.";
    	}
    	if ("".equals(grpCode.getGrpGrpCdNm()) || grpCode.getGrpGrpCdNm() == null) {
            errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpUseYn()) || grpCode.getGrpUseYn() == null) {
            errors.rejectValue("grpUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (grpCode.getGrpGrpCdNm().length() > 50) {
        	errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 50자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpDesc().length() > 200) {
        	errors.rejectValue("grpGrpDesc", "");
    		msg += "<br/>그룹코드 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("그룹코드 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		grpCode.setChgr(account.getMemNo());
	    	if (codeService.updateCodeGrp(grpCode) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 수정실패");
    	}
    	
        return map;
    }
    
    /**
     * 상세코드 수정
     * @return
     */
    @PostMapping("/updateCodeDtl")
    @ResponseBody
    public HashMap<String,Object> updateCodeDtl(DtlCode dtlCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("상세코드 수정 dtlCode ::::: " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "상세코드 수정");
    	String msg = "";
    	
    	if ("".equals(dtlCode.getCd()) || dtlCode.getCd() == null || "".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>수정할 상세코드를 선택해주세요.";
    	}
    	if ("".equals(dtlCode.getCdNm()) || dtlCode.getCdNm() == null) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getDtlUseYn()) || dtlCode.getDtlUseYn() == null) {
        	errors.rejectValue("dtlUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}
    	
    	if (dtlCode.getCdNm().length() > 200) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 200자 내로 입력해주세요.";
    	}
    	if (dtlCode.getOrd() > 99) {
        	errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 100 미만으로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal1().length() > 50) {
        	errors.rejectValue("cdVal1", "");
    		msg += "<br/>기타1을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal2().length() > 50) {
        	errors.rejectValue("cdVal2", "");
    		msg += "<br/>기타2을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal3().length() > 50) {
        	errors.rejectValue("cdVal3", "");
    		msg += "<br/>기타3을 50자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("상세코드 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		dtlCode.setChgr(account.getMemNo());
	    	if (codeService.updateCodeDtl(dtlCode) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    	}    		
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "상세코드 수정실패");    		
    	}

        return map;
    }
    
    /**
     * 그룹코드 삭제
     * @return
     */
    @PostMapping("/deleteCodeGrp")
    @ResponseBody
    public HashMap<String,Object> deleteCodeGrp(GrpCode grpCode) {
    	log.debug("그룹코드 삭제 grpCode ::::: " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "그룹코드 삭제");
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
        	map.put("msg", "삭제할 그룹코드를 선택해주세요.");
            return map;
    	}

    	try {
	    	if (codeService.deleteCodeGrp(grpCode) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 삭제실패");    		
    	}
    	
        return map;
    }
    
    /**
     * 상세코드 삭제
     * @return
     */
    @PostMapping("/deleteCodeDtl")
    @ResponseBody
    public HashMap<String,Object> deleteCodeDtl(DtlCode dtlCode) {
    	log.debug("상세코드 삭제 dtlCode ::::: " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "상세코드 삭제");

    	if ("".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null || "".equals(dtlCode.getCd()) || dtlCode.getCd() == null) {
        	map.put("msg", "삭제할 상세코드를 선택해주세요.");
            return map;
    	}
    	
    	try {
	    	if (codeService.deleteCodeDtl(dtlCode) > 0) {
	        	map.put("result", "success");
		    	map.put("msg", "삭제되었습니다.");
			} else {
		    	map.put("msg", "삭제할 데이터가 없습니다.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "상세코드 삭제실패");    		
		}    	

        return map;
    }
}
