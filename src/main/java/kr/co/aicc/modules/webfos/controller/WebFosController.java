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
 * @package kr.co.aicc.modules.webfos.controller
 * @file WebFosController.java
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
package kr.co.aicc.modules.webfos.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.infra.exception.BizException;
import kr.co.aicc.infra.util.PaginationInfo;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.repository.DashboardDao;
import kr.co.aicc.modules.webfos.dto.AutoTxtInfo;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListReq;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListRes;
import kr.co.aicc.modules.webfos.dto.LineInfo;
import kr.co.aicc.modules.webfos.dto.RerunInfo;
import kr.co.aicc.modules.webfos.dto.ScheduleInfo;
import kr.co.aicc.modules.webfos.dto.SearchInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.dto.TransServerInfo;
import kr.co.aicc.modules.webfos.dto.UserInfo;
import kr.co.aicc.modules.webfos.dto.WebfosMessage;
import kr.co.aicc.modules.webfos.service.AutoTxtService;
import kr.co.aicc.modules.webfos.service.SocketManagementService;
import kr.co.aicc.modules.webfos.service.WebfosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "webfos")
public class WebFosController {
	
	private final AutoTxtService autoTxtService;
	private final WebfosService webfosService;
	private final SocketManagementService socketManagementService;
	private final DashboardDao dashboardDao;
	
	/**
     * websocket "/pub/webfos/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/webfos/message")
    public void message(WebfosMessage message, @Header("token") String token ) {
        // 로그인 회원 정보로 대화명 설정
        message.setSender(token);
        
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        webfosService.sendWebfosMessage(message);
    }
    
    @RequestMapping(value = "/program_list")
	 public String programList(@CurrentAccount Account account, Model model, HttpServletRequest request) {
    	
    	model.addAttribute("memId", account.getMemId());
    	model.addAttribute("memNm", account.getMemNm());
    	// 채널별 방송 프로그램및 작업자
        List<Channel> list = dashboardDao.selectChannel();
        model.addAttribute("workStatus", list);
		return "webfos/program_list";
	 }
    
    @GetMapping("/program/live/enter/{schedNo}")
    public String webfosLive(@CurrentAccount Account account, Model model, 
    		@PathVariable String schedNo)  {
    	
    	
    	if (!ObjectUtils.isEmpty(account)) {
    		// null check 필요함
    		ScheduleInfo schedInfo = webfosService.getScheduleInfo(Long.parseLong(schedNo));
    		
    		UserInfo userInfo = UserInfo.builder().memId(account.getMemId()).memNm(account.getMemNm()).build(); 
    		boolean participation = webfosService.checkMemberOfSchedule(Long.parseLong(schedNo), account.getMemNo());
    		
    		List<String> roleNmList = account.getMemRoles().stream().map(Role::getRoleNm).collect(Collectors.toList());
    		String topRole = CommonEnum.Role.ROLE_USER.value();
    		Iterator<String> itr = roleNmList.iterator();
    		
    		while (itr.hasNext()) {
    			String roleNm = itr.next();
    			// 최상위 롤부터 존재한다면 그 롤을 현재 롤로 본다.(동일한 레벨의 롤이라도 USER 보다는 WATCH가 우선순위로 본다.)
    			if (CommonEnum.Role.ROLE_ADMIN.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_MANAGER.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_WATCH.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} 
    		}
    		
    		// USER 롤이 아닐경우 스케줄에 배정된 인원이 아니여도 webfos에 진입 할 수 있다.
    		if (!CommonEnum.Role.ROLE_USER.value().equals(topRole)) participation = true;
    		
    		List<Integer> localPortList = new ArrayList<Integer>();
    		
    		if (participation) {
    			
    			log.debug("schedNo : {}, schedInfo : {}", schedNo, schedInfo.toString());
    			webfosService.createProgram(schedNo, schedInfo.getProgNm());
    			// 송출을 위한 목적지 list 조회
    			List<TransServerInfo> transServerInfoList = webfosService.getTrnsSvrInfo(schedInfo.getChnlNo());
    			// 송출을 위한 목적지 PC와의 socket 연결
    			if (!CollectionUtils.isEmpty(transServerInfoList)) {
    				for (TransServerInfo transServerInfo : transServerInfoList) {
    					int transLocalPort = 0;
    					transLocalPort = socketManagementService.createConnectionUDP(transServerInfo.getTrnsIp(), transServerInfo.getTrnsPort());
    					
    					localPortList.add(transLocalPort);
    				}
    			}
    		}
    		
    		model.addAttribute("schedNo", schedNo);
    		model.addAttribute("clientInfo", userInfo);
    		model.addAttribute("participation", participation);
    		model.addAttribute("topRole", topRole);
    		model.addAttribute("watcher", account.hasRole(CommonEnum.Role.ROLE_WATCH.value()));
    		model.addAttribute("memNo", account.getMemNo());
    		model.addAttribute("localPortList", localPortList);
    	}
    	
    	return "/webfos/webfos_live2";
    }
    
    @GetMapping("/program/live/enter/v1/{schedNo}")
    public String webfosLiveOfV1(@CurrentAccount Account account, Model model, 
    		@PathVariable String schedNo)  {
    	
    	
    	if (!ObjectUtils.isEmpty(account)) {
    		// null check 필요함
    		ScheduleInfo schedInfo = webfosService.getScheduleInfo(Long.parseLong(schedNo));
    		
    		UserInfo userInfo = UserInfo.builder().memId(account.getMemId()).memNm(account.getMemNm()).build(); 
    		boolean participation = webfosService.checkMemberOfSchedule(Long.parseLong(schedNo), account.getMemNo());
    		
    		List<String> roleNmList = account.getMemRoles().stream().map(Role::getRoleNm).collect(Collectors.toList());
    		String topRole = CommonEnum.Role.ROLE_USER.value();
    		Iterator<String> itr = roleNmList.iterator();
    		
    		while (itr.hasNext()) {
    			String roleNm = itr.next();
    			// 최상위 롤부터 존재한다면 그 롤을 현재 롤로 본다.(동일한 레벨의 롤이라도 USER 보다는 WATCH가 우선순위로 본다.)
    			if (CommonEnum.Role.ROLE_ADMIN.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_MANAGER.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_WATCH.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} 
    		}
    		
    		// USER 롤이 아닐경우 스케줄에 배정된 인원이 아니여도 webfos에 진입 할 수 있다.
    		if (!CommonEnum.Role.ROLE_USER.value().equals(topRole)) participation = true;
    		
    		int transLocalPort = 0;
    		
    		if (participation) {
    			log.debug("schedNo : {}, schedInfo : {}", schedNo, schedInfo.toString());
    			webfosService.createProgram(schedNo, schedInfo.getProgNm());
    			// 송출을 위한 목적지 PC와의 socket 연결
    			transLocalPort = socketManagementService.createConnectionUDP(schedInfo.getTrnsIp(), schedInfo.getTrnsPort());
    		}
    		
    		model.addAttribute("schedNo", schedNo);
    		model.addAttribute("clientInfo", userInfo);
    		model.addAttribute("participation", participation);
    		model.addAttribute("topRole", topRole);
    		model.addAttribute("watcher", account.hasRole(CommonEnum.Role.ROLE_WATCH.value()));
    		model.addAttribute("memNo", account.getMemNo());
    		model.addAttribute("transLocalPort", transLocalPort);
    	}
    	
    	return "/webfos/webfos_live";
    }
    
    @GetMapping("/program/rerun/enter/{schedNo}")
    public String webfosRerunEnter(@CurrentAccount Account account, @ModelAttribute RerunInfo rerunInfo, Model model, 
    		@PathVariable String schedNo)  {
    	if (!ObjectUtils.isEmpty(account)) {
    		// null check 필요함
    		ScheduleInfo schedInfo = webfosService.getScheduleInfo(Long.parseLong(schedNo));
    		
    		log.debug("schedInfo : {}", schedInfo.toString());
    		
    		// 해당 chennal의 저장된 자막 list를 가져온다.
//    		List<SubtitleDetailInfo> subDtlList = webfosService.getSubtitleDetailListOfChennal(schedInfo.getChnlNo(), null, null);
    		
    		List<LineInfo> lineInfoList = null;
    		if (rerunInfo.getTargetSchedNo() != null) {
    			lineInfoList = webfosService.getSubtitleDetailInfo(rerunInfo.getTargetSchedNo(), rerunInfo.getMaxByte());
    		}
    		
    		//UserInfo userInfo = UserInfo.builder().memId(account.getMemId()).memNm(account.getMemNm()).build(); 
    		boolean participation = webfosService.checkMemberOfSchedule(Long.parseLong(schedNo), account.getMemNo());
    		
    		List<String> roleNmList = account.getMemRoles().stream().map(Role::getRoleNm).collect(Collectors.toList());
    		String topRole = CommonEnum.Role.ROLE_USER.value();
    		Iterator<String> itr = roleNmList.iterator();
    		
    		while (itr.hasNext()) {
    			String roleNm = itr.next();
    			// 최상위 롤부터 존재한다면 그 롤을 현재 롤로 본다.(동일한 레벨의 롤이라도 USER 보다는 WATCH가 우선순위로 본다.)
    			if (CommonEnum.Role.ROLE_ADMIN.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_MANAGER.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_WATCH.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} 
    		}
    		
    		// USER 롤이 아닐경우 스케줄에 배정된 인원이 아니여도 webfos에 진입 할 수 있다.
    		if (!CommonEnum.Role.ROLE_USER.value().equals(topRole)) participation = true;
    		
    		int transLocalPort = 0;
    		
    		if (participation) {
    			webfosService.createProgram(schedNo, schedInfo.getProgNm());
    			// 송출을 위한 목적지 PC와의 socket 연결
    			transLocalPort = socketManagementService.createConnectionUDP(schedInfo.getTrnsIp(), schedInfo.getTrnsPort());
    		}
    		
    		model.addAttribute("schedNo", schedNo);
    		model.addAttribute("chnlNo", schedInfo.getChnlNo());
    		model.addAttribute("chnlNm", schedInfo.getChnlNm());
    		//model.addAttribute("clientInfo", userInfo);
//    		model.addAttribute("subDtlList", subDtlList);
    		model.addAttribute("participation", participation);
    		model.addAttribute("topRole", topRole);
    		model.addAttribute("watcher", account.hasRole(CommonEnum.Role.ROLE_WATCH.value()));
    		//model.addAttribute("memNo", account.getMemNo());
    		model.addAttribute("transLocalPort", transLocalPort);
    		model.addAttribute("disconnect", false);
    		model.addAttribute("lineInfoList", lineInfoList);
    		model.addAttribute("progNm", "");
//    		model.addAttribute("progNm", !ObjectUtils.isEmpty(targetDetailInfo) ? targetDetailInfo.getProgNm() : "");
    		
    	}
    	
    	return "/webfos/webfos_rerun2";
    }
    
    @GetMapping("/program/rerun/chnlSubDtlList")
    public String chnlSubDtlList(@CurrentAccount Account account, ChnlSubDtlListReq chnlSubDtlListReq, Model model) {
    	if (!ObjectUtils.isEmpty(account)) {
    		int subDtlCnt = webfosService.getChnlSubDtlListCnt(chnlSubDtlListReq);
    		
    		int page = chnlSubDtlListReq.getPage();
    		if (page < 1) {
    			page = 1;
    			chnlSubDtlListReq.setPage(page);
    		}
    		int recordCountPerPage = 50;
    		chnlSubDtlListReq.setPageLimit(recordCountPerPage);
    		chnlSubDtlListReq.setPageOffset((page - 1) * recordCountPerPage);
    		
    		List<ChnlSubDtlListRes> subDtlList = webfosService.getChnlSubDtlList(chnlSubDtlListReq);
    		
    		PaginationInfo subDtlPagination = new PaginationInfo();
    		subDtlPagination.setCurrentPageNo(page);
    		subDtlPagination.setRecordCountPerPage(recordCountPerPage);
    		subDtlPagination.setPageSize(5);
    		subDtlPagination.setTotalRecordCount(subDtlCnt);
    		
        	model.addAttribute("subDtlList", subDtlList);
        	model.addAttribute("subDtlCnt", subDtlCnt);
        	model.addAttribute("subDtlPagination", subDtlPagination);
        	model.addAttribute("chnlNm", chnlSubDtlListReq.getChnlNm());
    	}
    	return "/webfos/webfos_rerun_chnlSubDtlList";
    }
    
    @PostMapping("/program/rerun")
    public String webfosRerun(@CurrentAccount Account account, RerunInfo rerunInfo, Model model)  {
    	if (!ObjectUtils.isEmpty(account)) {
    		log.debug("RerunInfo : {}", rerunInfo.toString());
    		// 해당 chennal의 저장된 자막 list를 가져온다.
    		List<SubtitleDetailInfo> subDtlList = webfosService.getSubtitleDetailListOfChennal(rerunInfo.getChnlNo(), null, null, rerunInfo.getTargetSchedNo());
    		
    		// 재방송 사용 할 자막정보 조회
    		List<LineInfo> lineInfoList = webfosService.getSubtitleDetailInfo(rerunInfo.getTargetSchedNo(), rerunInfo.getMaxByte());
    		
    		//UserInfo userInfo = UserInfo.builder().memId(account.getMemId()).memNm(account.getMemNm()).build(); 
    		boolean participation = webfosService.checkMemberOfSchedule(rerunInfo.getSchedNo(), account.getMemNo());
    		
    		List<String> roleNmList = account.getMemRoles().stream().map(Role::getRoleNm).collect(Collectors.toList());
    		String topRole = CommonEnum.Role.ROLE_USER.value();
    		Iterator<String> itr = roleNmList.iterator();
    		
    		while (itr.hasNext()) {
    			String roleNm = itr.next();
    			// 최상위 롤부터 존재한다면 그 롤을 현재 롤로 본다.(동일한 레벨의 롤이라도 USER 보다는 WATCH가 우선순위로 본다.)
    			if (CommonEnum.Role.ROLE_ADMIN.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_MANAGER.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} else if (CommonEnum.Role.ROLE_WATCH.value().equals(roleNm)) {
    				topRole = roleNm;
    				break;
    			} 
    		}
    		
    		// USER 롤이 아닐경우 스케줄에 배정된 인원이 아니여도 webfos에 진입 할 수 있다.
    		if (!CommonEnum.Role.ROLE_USER.value().equals(topRole)) participation = true;
    		
//    		SubtitleDetailInfo targetDetailInfo = subDtlList.stream().filter(e -> e.getSchedNo().equals(rerunInfo.getTargetSchedNo())).findFirst().orElse(null);
    		SubtitleDetailInfo targetDetailInfo = subDtlList.get(0);
    			
    		model.addAttribute("schedNo", rerunInfo.getSchedNo());
    		model.addAttribute("chnlNo", rerunInfo.getChnlNo());
    		model.addAttribute("chnlNm", rerunInfo.getChnlNm());
    		model.addAttribute("transLocalPort", rerunInfo.getTransLocalPort());
//    		model.addAttribute("subDtlList", subDtlList);
    		model.addAttribute("lineInfoList", lineInfoList);
    		model.addAttribute("participation", participation);
    		model.addAttribute("topRole", topRole);
    		model.addAttribute("watcher", account.hasRole(CommonEnum.Role.ROLE_WATCH.value()));
    		model.addAttribute("disconnect", true);
    		model.addAttribute("progNm", !ObjectUtils.isEmpty(targetDetailInfo) ? targetDetailInfo.getProgNm() : "");
    	}
    	
    	return "/webfos/webfos_rerun2";
    }
    
    @GetMapping("/program/rerun/search/{chnlNo}")
    public String webfosRerunSearch(@CurrentAccount Account account, @PathVariable String chnlNo,
    		@RequestParam(value = "searchNm") String searchNm, @RequestParam(value = "searchDt") String searchDt,
    		@RequestParam(value = "chnlNm") String chnlNm, Model model)  {
    	List<SubtitleDetailInfo> subDtlList = null;
    	if (!ObjectUtils.isEmpty(account)) {
    		// 해당 chennal의 저장된 자막 list를 가져온다.
    		subDtlList = webfosService.getSubtitleDetailListOfChennal(Long.parseLong(chnlNo), searchNm, searchDt, null);
    		
    		model.addAttribute("chnlNm", chnlNm);
    		model.addAttribute("subDtlList", subDtlList);
    	}
    	
    	return "/webfos/subDtlTmp";
    }
    
    @GetMapping("/program/autoTxt")
    public String autoTxt(@CurrentAccount Account account, @ModelAttribute SearchInfo searchInfo, Model model) {
    	log.debug("account : {}", account.toString());
    	
    	model.addAttribute("memNm", account.getMemNm());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(account.getMemNo());
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	
    	return "/webfos/autotxt2";
    }
    
    @PostMapping("/program/autoTxt/search")
    public String searchAutoTxt(@CurrentAccount Account account, SearchInfo searchInfo, Model model) {
    	model.addAttribute("memNm", account.getMemNm());

    	searchInfo.setMemNo(account.getMemNo());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(searchInfo);
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	
    	return "/webfos/autotxt2";
    }
    
    @GetMapping("/program/rerun/autoTxt")
    public String rerunAutoTxt(@CurrentAccount Account account, @ModelAttribute SearchInfo searchInfo, Model model) {
    	log.debug("account : {}", account.toString());
    	
    	model.addAttribute("memNm", account.getMemNm());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(account.getMemNo());
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	model.addAttribute("autoTxtInfoCnt", autoTxtInfoList.size());
    	
    	return "/webfos/rerunAutotxt";
    }
    
    @PostMapping("/program/rerun/autoTxt/search")
    public String rerunSearchAutoTxt(@CurrentAccount Account account, SearchInfo searchInfo, Model model) {
    	model.addAttribute("memNm", account.getMemNm());

    	searchInfo.setMemNo(account.getMemNo());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(searchInfo);
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	model.addAttribute("autoTxtInfoCnt", autoTxtInfoList.size());
    	
    	return "/webfos/rerunAutotxt";
    }

    
    @GetMapping("/program/autoTxt/word")
    @ResponseBody
    public String getAutoTxt(@CurrentAccount Account account, @RequestParam(value = "key") String key) {
    	return autoTxtService.getWordOfAutoTxt(key, account.getMemNo());
    }
    
    @PostMapping("/program/autoTxt/word")
    @ResponseBody
    public String createAutoTxt(@CurrentAccount Account account, @RequestParam(value = "words") String words, @RequestParam(value = "overwrite") String overwrite) {
    	String[] array = words.split(":");
    	log.debug("overwrite : {}", overwrite);
    	return autoTxtService.createWordOfAutoTxt(account.getMemNo(), array[0].trim(), array[1], overwrite);
    }
    
    @PutMapping("/program/autoTxt/word")
    @ResponseBody
    public String updateAutoTxt(@CurrentAccount Account account, @RequestParam(value = "words") String words) {
    	String[] array = words.split(":");
    	
    	return autoTxtService.updateWordOfAutoTxt(account.getMemNo(), array[0].trim(), array[1]);
    }
    
    @DeleteMapping("/program/autoTxt/word")
    @ResponseBody
    public String deleteAutoTxt(@CurrentAccount Account account, @RequestParam(value = "keyword") String keyword) {
    	return autoTxtService.deleteWordOfAutoTxt(account.getMemNo(), keyword);
    }
    
    @PostMapping("/file/upload")
	public String autoTxtUpload(@CurrentAccount Account account, @RequestParam("filename") MultipartFile mfile,
			@ModelAttribute SearchInfo searchInfo, Model model) {
		
		if (!ObjectUtils.isEmpty(account)) {
			try {
				autoTxtService.autoTxtUpload(account, mfile);
			} catch (BizException e) {
				log.error(e.getMessage());
			}
		 }
		
		model.addAttribute("memNm", account.getMemNm());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(account.getMemNo());
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	model.addAttribute("upload", true);
    	
		return "/webfos/autotxt2";
	}
	
	@PostMapping("/file/rerun/upload")
	public String rerunAutoTxtUpload(@CurrentAccount Account account, @RequestParam("filename") MultipartFile mfile,
			@ModelAttribute SearchInfo searchInfo, Model model) {
		
		if (!ObjectUtils.isEmpty(account)) {
			try {
				autoTxtService.rerunAutoTxtUpload(account, mfile);
				model.addAttribute("upload", true);
			} catch (BizException e) {
				log.error(e.getMessage());
				model.addAttribute("upload", false);
			}
		 }
		
		model.addAttribute("memNm", account.getMemNm());
    	
    	List<AutoTxtInfo> autoTxtInfoList = autoTxtService.getAutoTxtInfoList(account.getMemNo());
    	
    	model.addAttribute("autoTxtInfoList", autoTxtInfoList);
    	model.addAttribute("autoTxtInfoCnt", autoTxtInfoList.size());
    	
    	
		return "/webfos/rerunAutotxt";
	}
	
	
	
	@GetMapping("/file/download")
	@ResponseBody
	public String autoTxtDwonload(@CurrentAccount Account account) {
		String result = null;
		if (!ObjectUtils.isEmpty(account)) {
			result = autoTxtService.autoTxtDownload(account);
		}
		
		return result;
	}

	@GetMapping("/program/socket/transmission")
    @ResponseBody
    public void transmissionSocket(@CurrentAccount Account account, @RequestParam(value = "schedNo") String schedNo,
    		@RequestParam(value = "transWords") String transWords, @RequestParam(value = "localPort") String localPort) {
		webfosService.sendSubtileDetailOfRerun(schedNo, transWords, account.getMemNo(), localPort);
    }
	
	@GetMapping("/program/socket/remove")
    @ResponseBody
    public void removeSocket(@RequestParam(value = "portList", required = false) List<Integer> portList) {
		if (!ObjectUtils.isEmpty(portList)) {
			for (int localPort : portList) {
				socketManagementService.removeTransSocketUDP(localPort);
			}
		}
    }
}


