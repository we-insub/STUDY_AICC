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
 * @file TeamController.java
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.infra.util.PaginationInfo;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.settings.dto.ChnlTeamDto;
import kr.co.aicc.modules.settings.dto.MemGrpDto;
import kr.co.aicc.modules.settings.service.TeamService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class TeamController {
	
	private final CodeCacheService codeCacheService;
	private final TeamService teamService;
	
    /**
     * Settings > 팀(그룹관리)
     * @return
     */
    @GetMapping("/team")
    public String team(Model model, HttpServletRequest request, MemGrpDto memGrpDto, @CurrentAccount Account account) {
    	log.debug("팀(그룹관리) memGrpDto : " + memGrpDto);
    	
    	if ("".equals(memGrpDto.getPageType()) || memGrpDto.getPageType() == null) {
    		memGrpDto.setPageType("team");
    	}
    	
        model.addAttribute("team", codeCacheService.getCode(CommonEnum.GroupCode.MEM_GRP.name()));	//팀    	
		model.addAttribute("pageType", memGrpDto.getPageType());
        return "settings/team";
    }

    /**
     * 팀 배정 조회
     * @return
     */
    @GetMapping("teamList")
    public String teamList(MemGrpDto memGrpDto, Model model) {
    	log.debug("팀 배정 조회 memGrpDto ::::: " + memGrpDto);
    	
    	List<MemGrpDto> memGrpList = teamService.findMemGrpList(memGrpDto);
    	int memGrpListCnt =  teamService.findMemGrpListCnt(memGrpDto);		

    	log.debug("팀 배정 조회 memGrpList ::::: " + memGrpList);

    	PaginationInfo teamPagination = new PaginationInfo();
    	teamPagination.setCurrentPageNo(memGrpDto.getPageNo());
    	teamPagination.setRecordCountPerPage(10);
    	teamPagination.setPageSize(5);
    	teamPagination.setTotalRecordCount(memGrpListCnt);
    	
        model.addAttribute("teamCnt", memGrpListCnt);
    	model.addAttribute("teamPagination", teamPagination);
        model.addAttribute("teamList", memGrpList);
        
        return "settings/teamTmp";
    }

    /**
     * 맴버 조회
     * @return
     */
    @GetMapping("memberList")
    public String memberList(MemGrpDto memGrpDto, Model model) {
    	log.debug("맴버 조회 memGrpDto ::::: " + memGrpDto);	

    	
    	List<MemGrpDto> memList = teamService.findMemList(memGrpDto);
    	int memListCnt = teamService.findMemListCnt(memGrpDto);
    	
    	log.debug("맴버 조회 memList ::::: " + memList);

    	PaginationInfo memPagination = new PaginationInfo();
    	memPagination.setCurrentPageNo(memGrpDto.getPageNo());
    	memPagination.setRecordCountPerPage(10);
    	memPagination.setPageSize(5);
    	memPagination.setTotalRecordCount(memListCnt);

        model.addAttribute("memCnt", memListCnt);
    	model.addAttribute("memPagination", memPagination);
        model.addAttribute("memList", memList);
        
        return "settings/teamModalTmp";
    }

    /**
     * 특정 그룹 맴버 정보
     * @return
     */
    @GetMapping("getGrpMemInfo")
    @ResponseBody
    public HashMap<String,Object> getGrpMemInfo(MemGrpDto memGrpDto) {
    	log.debug("특정 그룹 맴버 정보 memGrpDto ::::: " + memGrpDto);	

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	
    	List<MemGrpDto> memList = teamService.getGrpMemInfo(memGrpDto);
    	
    	log.debug("맴버 조회 memList ::::: " + memList);

		map.put("memList", memList);
        
        return map;
    }

    /**
     * 특정 그룹 맴버 등록
     * @return
     */
    @PostMapping("createGrpMem")
    @ResponseBody
    public HashMap<String,Object> createGrpMem(MemGrpDto memGrpDto, Model model, @CurrentAccount Account account) {
    	log.debug("특정 그룹 맴버 등록 memGrpDto ::::: " + memGrpDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "맴버 등록");
    	
    	if (memGrpDto.getPMemNo().length == 0 || memGrpDto.getPMemNo() == null) {
        	map.put("msg", "맴버를 배정해주세요.");
            return map;
    	} else if ("".equals(memGrpDto.getGrpType()) || memGrpDto.getGrpType() == null) {
        	map.put("msg", "맴버그룹(팀)을 선택해주세요.");
            return map;
    	}

    	try {
    		memGrpDto.setCretr(account.getMemNo());
    		memGrpDto.setChgr(account.getMemNo());
	    	if (teamService.createGrpMem(memGrpDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "맴버 등록실패");
    	}
    	
        return map;
    }
    
/////////////////////////////////////////////////////////////////////////////////////////    
    
    /**
     * 채널 배정 조회
     * @return
     */
    @GetMapping("teamChnlList")
    public String teamChnlList(ChnlTeamDto chnlTeamDto, Model model) {
    	log.debug("채널 배정 조회 chnlTeamDto ::::: " + chnlTeamDto);
    	
        List<ChnlTeamDto> chnlTeamList = teamService.findChnlTeamList(chnlTeamDto);
		int chnlTeamListCnt =  teamService.findChnlTeamListCnt(chnlTeamDto);
		
    	log.debug("채널 배정 리스트 chnlTeamList ::::: " + chnlTeamList);

    	PaginationInfo pagination = new PaginationInfo();
    	pagination.setCurrentPageNo(chnlTeamDto.getPageNo());
    	pagination.setRecordCountPerPage(10);
    	pagination.setPageSize(5);
    	pagination.setTotalRecordCount(chnlTeamListCnt);
    	
    	model.addAttribute("chnlCnt", chnlTeamListCnt);
    	model.addAttribute("chnlPagination", pagination);
        model.addAttribute("chnlList", chnlTeamList);
        
        return "settings/teamChnlTmp";
    }
    
    /**
     * 편한 팀 관리 
     * @return
     */
//    @GetMapping("ctList")
//    public String mtmList(CtDTO ctDTO, Model model) {
//    	log.debug("채널 배정 조회 ctDTO ::::: " + ctDTO);
//    	
//        List<ChnlTeamDto> ctChnlTeamList = teamService.findCtChnlTeamList(ctDTO); // 여기 수정
//		int ctChnlTeamListCnt =  teamService.findCtChnlTeamListCnt(ctDTO); // 여기 수정 
//		
//    	log.debug("채널 배정 리스트 chnlTeamList ::::: " + ctChnlTeamList);
//
//    	PaginationInfo pagination = new PaginationInfo();
//    	pagination.setCurrentPageNo(ctDTO.getPageNo());
//    	pagination.setRecordCountPerPage(10);
//    	pagination.setPageSize(5);
//    	pagination.setTotalRecordCount(ctChnlTeamListCnt);
//    	
//    	model.addAttribute("ctChnlCnt", ctChnlTeamListCnt);
//    	model.addAttribute("ctChnlPagination", pagination);
//        model.addAttribute("ctChnlList", ctChnlTeamListCnt);
//        
//        return "settings/teamChnlTmp";
//    }
//    

    /**
     * 특정 채널 맴버 등록
     * @return
     */
    @PostMapping("createChnlTeam")
    @ResponseBody
    public HashMap<String,Object> createChnlTeam(ChnlTeamDto chnlTeamDto, Model model, @CurrentAccount Account account) {
    	log.debug("특정 채널 맴버 등록 chnlTeamDto ::::: " + chnlTeamDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "맴버 등록");
    	
//    	if (chnlTeamDto.getPMemNo().length == 0 || chnlTeamDto.getPMemNo() == null) {
//        	map.put("msg", "맴버를 배정해주세요.");
//            return map;
//    	} else 
    	if (chnlTeamDto.getChnlNo() == 0) {
        	map.put("msg", "맴버그룹(팀)을 선택해주세요.");
            return map;
    	}

    	try {
    		chnlTeamDto.setCretr(account.getMemNo());
    		chnlTeamDto.setChgr(account.getMemNo());
	    	if (teamService.createChnlTeam(chnlTeamDto) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "맴버 등록실패");
    	}
    	
        return map;
    }
  
}
