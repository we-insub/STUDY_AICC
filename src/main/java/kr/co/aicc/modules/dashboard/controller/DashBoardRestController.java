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
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.dashboard.dto.ChannelForm;
import kr.co.aicc.modules.dashboard.dto.DashBoardForm;
import kr.co.aicc.modules.dashboard.service.DashboardService;
import kr.co.aicc.modules.dashboard.validator.ChannelFormValidator;
import kr.co.aicc.modules.dashboard.validator.DashBoardFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/dashboard")
public class DashBoardRestController {
    private final ChannelFormValidator channelFormValidator;
    private final DashBoardFormValidator dashBoardFormValidator;
    private final DashboardService dashboardService;
	private final CodeCacheService codeCacheService;
	private final AppProperties appProperties;

    @PostMapping("/work_detail")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
    @ResponseBody
    public HashMap<String, Object> workDetailPost(@RequestBody DashBoardForm dashBoardForm) {
        HashMap<String, Object> hashMap = dashboardService.getDashBoardData(dashBoardForm);
        return hashMap;
    }
}
