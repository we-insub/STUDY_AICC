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
 * @file ChannelController.java
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

import kr.co.aicc.infra.util.PaginationInfo;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.settings.dto.ChannelForm;
import kr.co.aicc.modules.settings.service.ChannelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "settings")
public class ChannelController {

    private final ChannelSettingService channelSettingService;	
	private final CodeCacheService codeCacheService;

    /**
     * 채널관리 화면
     * @return
     */
    @GetMapping("channel")
    public String channel(ChannelForm channelForm, Model model, HttpServletRequest request, @CurrentAccount Account account) {
        return "settings/channel";
    }

    /**
     * 채널 조회
     * @return
     */
    @GetMapping("channelList")
    public String channelList(ChannelForm channelForm, Model model) {
    	log.debug("채널 조회 channelForm ::::: " + channelForm);
    	
        List<ChannelForm> channelList = channelSettingService.getChannelList(channelForm);
		int channelListCnt =  channelSettingService.getChannelListCnt(channelForm);		

    	PaginationInfo pagination = new PaginationInfo();
    	pagination.setCurrentPageNo(channelForm.getPageNo());
    	pagination.setRecordCountPerPage(10);
    	pagination.setPageSize(5);
    	pagination.setTotalRecordCount(channelListCnt);

    	model.addAttribute("cnt", channelListCnt);
    	model.addAttribute("pagination", pagination);
        model.addAttribute("channelList", channelList);
        
        return "settings/channelTmp";
    }

    /**
     * 채널 상세
     * @return
     */
    @PostMapping("channelDtl")
    @ResponseBody
    public HashMap<String,Object> channelDtl(ChannelForm channelForm, Model model) {
    	log.debug("채널 상세 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	
        List<ChannelForm> channelList = channelSettingService.getChannelList(channelForm);
		log.debug("channelList.get(0) " + channelList.get(0));
		
		map.put("channel", channelList.get(0));
        
        return map;
    }

    /**
     * 채널 등록
     * @return
     */
    @PostMapping("createChannel")
    @ResponseBody
    public HashMap<String,Object> createChannel(ChannelForm channelForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("채널 등록 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "채널 등록");
    	String msg = "";
    	
    	if ("".equals(channelForm.getChnlNm()) || channelForm.getChnlNm() == null) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 입력해주세요.";
    	}
    	if (channelForm.getPicFile().isEmpty()) {
            errors.rejectValue("picFile", "");
    		msg += "<br/>채널로고를 등록해주세요.";
    	}
    	if ("".equals(channelForm.getLinkUrl()) || channelForm.getLinkUrl() == null) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsPort()) || channelForm.getTrnsPort() == null) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsIp()) || channelForm.getTrnsIp() == null) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를 입력해주세요.";
    	}

    	if (channelForm.getChnlNm().length() > 20) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 20자 내로 입력해주세요.";
    	}
    	if (channelForm.getChnlDesc().length() > 200) {
            errors.rejectValue("chnlDesc", "");
    		msg += "<br/>채널설명을 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getLinkUrl().length() > 200) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsIp().length() > 15) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를  15자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsPort().length() > 15) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를  15자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("채널 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		channelForm.setCretr(account.getMemNo());
    		channelForm.setChgr(account.getMemNo());
	    	if (channelSettingService.createChannel(channelForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 등록실패");
    	}
    	
        return map;
    }

    /**
     * 채널 수정
     * @return
     */
    @PostMapping("updateChannel")
    @ResponseBody
    public HashMap<String,Object> updateChannel(ChannelForm channelForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("채널 수정 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "채널 수정");
    	String msg = "";

    	if (channelForm.getChnlNo() == null) {
            errors.rejectValue("chnlNo", "");
    		msg += "<br/>수정할 채널을 선택해주세요.";
    	}
    	if ("".equals(channelForm.getChnlNm()) || channelForm.getChnlNm() == null) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 입력해주세요.";
    	}
    	if ("".equals(channelForm.getLinkUrl()) || channelForm.getLinkUrl() == null) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsPort()) || channelForm.getTrnsPort() == null) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsIp()) || channelForm.getTrnsIp() == null) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를 입력해주세요.";
    	}

    	if (channelForm.getChnlNm().length() > 20) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 20자 내로 입력해주세요.";
    	}
    	if (channelForm.getChnlDesc().length() > 200) {
            errors.rejectValue("chnlDesc", "");
    		msg += "<br/>채널설명을 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getLinkUrl().length() > 200) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsIp().length() > 15) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를  15자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsPort().length() > 15) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를  15자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("채널 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
            return map;
        }
    	try {
    		channelForm.setCretr(account.getMemNo());
    		channelForm.setChgr(account.getMemNo());
	    	if (channelSettingService.updateChannel(channelForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 수정실패");
    	}
    	
        return map;
    }

    /**
     * 채널 삭제
     * @return
     */
    @PostMapping("deleteChannel")
    @ResponseBody
    public HashMap<String,Object> deleteChannel(ChannelForm channelForm, Model model) {
    	log.debug("채널 삭제 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "채널 삭제");

    	if (channelForm.getChnlNo() == null) {
        	map.put("msg", "삭제할 채널을 선택해주세요.");
            return map;
    	}
    	
    	try {
	    	if (channelSettingService.deleteChannel(channelForm) > 0) {
	        	map.put("result", "success");
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 삭제실패");    		
    	}
    	
        return map;
    }
    
    @GetMapping("schedDetail")
    public String schedDetail(Schedule schedule, Model model, HttpServletRequest request) {
        Schedule resSchedule = channelSettingService.getMemberScheduleList(schedule);
        model.addAttribute("Schedule", resSchedule);
        return "settings/schedule_detail";
    }

}
