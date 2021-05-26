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
 * @package kr.co.aicc.modules.schedule.controller
 * @file formFiltertroller.java
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
package kr.co.aicc.modules.schedule.controller;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.enums.ResponseEnum;
import kr.co.aicc.modules.account.dto.SignUpForm;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.schedule.dto.*;
import kr.co.aicc.modules.schedule.service.ScheduleService;
import kr.co.aicc.modules.schedule.validator.RegSchedFormValidator;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
	private final CodeCacheService codeCacheService;
    private final ReloadableResourceBundleMessageSource messageSource;
    private final RegSchedFormValidator regSchedFormValidator;
    private final AppProperties appProperties;
    /**
     * 방송스케쥴 > 타임테이블
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/timeline")
    public String home(@CurrentAccount Account account, Model model, HttpServletRequest request) {
        model.addAttribute("memId", account.getMemId());
        model.addAttribute("memNm", account.getMemNm());
        List<Channel> channelList = scheduleService.getTotalChnlList(account);
        model.addAttribute("channelList", channelList);
        return "/schedule/timeline";
    }

    /**
     * 더미 데이터 만들기
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/test_data_create")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
    public String testDataCreate(@CurrentAccount Account account, Model model) {
        scheduleService.createTestData(account);
        return "redirect:/dashboard/work_status";
    }

    @GetMapping("/history_excel_down")
    @PreAuthorize ("hasRole('ROLE_MANAGER')")
    @ResponseBody
    public ResponseEntity<Resource> history_excel_down (SchedExcelFile schedExcelFile, @CurrentAccount Account account) {
        return scheduleService.downloadHistoryExcel(schedExcelFile, account);
    }

    @GetMapping(path = "/timeline_excel_down", produces = "application/vnd.ms-excel")
    @PreAuthorize ("hasRole('ROLE_MANAGER')")
    public String downloadExcel () {
        return "TimelineXlsx";
    }

    @GetMapping(value = "/textDownload")
    public void textDownload(HttpServletResponse response, @RequestParam(value = "schedNo") long schedNo )
            throws Exception {
        scheduleService.textDownload(response, schedNo);
    }

}
