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
 * @package kr.co.aicc.modules.dashboard.controller
 * @file DashBoardController.java
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
package kr.co.aicc.modules.dashboard.controller;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.DashBoard;
import kr.co.aicc.modules.dashboard.domain.DashboardRow;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.dto.WorkStatusForm;
import kr.co.aicc.modules.dashboard.service.DashboardService;
import kr.co.aicc.modules.dashboard.validator.ChannelFormValidator;
import kr.co.aicc.modules.dashboard.validator.DashBoardFormValidator;
import kr.co.aicc.modules.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "dashboard")
public class DashBoardController {
    private final ChannelFormValidator channelFormValidator;
    private final DashBoardFormValidator dashBoardFormValidator;
    private final DashboardService dashboardService;
	private final CodeCacheService codeCacheService;
	private final AppProperties appProperties;
    private final ScheduleService scheduleService;
    /**
     * 대시보드 > 작업현황상세
     * @return
     */
    @GetMapping("/work_status")
    public String home(Model model, HttpServletRequest request, WorkStatusForm workStatusForm, @CurrentAccount Account account) {
        // 채널별 방송 프로그램및 작업자
        List<Channel> rowlist =  dashboardService.workStatus(workStatusForm, account);
        model.addAttribute("rowlist", rowlist);
        model.addAttribute("appProperties", appProperties);
        model.addAttribute("toDay", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")));
        List<Channel> channelList = scheduleService.getTotalChnlList(account);
        model.addAttribute("channelList", channelList);
        return "dashboard/work_status";
    }

    @PostMapping("createChannel")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
    @ResponseBody
    public HashMap<String, Object> createChannel(@Valid ChannelForm channelForm, Errors errors) throws Exception {
        if (errors.hasErrors()) {}
        log.debug("ChannelForm", channelForm);
        dashboardService.createChnl(channelForm);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("success", "ok");
        return hashMap;
    }

    @InitBinder("channelForm")
    public void initChannelBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(channelFormValidator);
    }

    /**
     * 대시보드 > 작업내역상세
     * @return
     */
    @GetMapping("/work_detail")
    public String workDetailGet(@Valid DashBoardForm dashBoardForm, Model model, HttpServletRequest request, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = dashboardService.getDashBoardData(dashBoardForm);
        model.addAllAttributes(hashMap);
        return "dashboard/work_detail";
    }

    @InitBinder("dashBoardForm")
    public void initDashBoardBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(dashBoardFormValidator);
    }

/*    @GetMapping("/work_detail_ajax")
    public String workDetailGetAjax(@Valid DashBoardForm dashBoardForm, Model model) {
        HashMap<String, Object> hashMap = dashboardService.getDashBoardData(dashBoardForm);
        model.addAllAttributes(hashMap);
        return "dashboard/work_detail_ajax";
    }*/

    @RequestMapping(path = "/dashboard_excel_down", produces = "application/vnd.ms-excel")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
    public String downloadExcel () {
        return "DashBoardXlsx";
    }

}
