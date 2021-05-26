package kr.co.aicc.modules.schedule.controller;


import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.infra.enums.ResponseEnum;
import kr.co.aicc.infra.exception.BizException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.schedule.dto.*;
import kr.co.aicc.modules.schedule.service.ScheduleService;
import kr.co.aicc.modules.schedule.validator.RegSchedFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/schedule")
public class ScheduleRestController {

    private final ScheduleService scheduleService;
    private final CodeCacheService codeCacheService;
    private final ReloadableResourceBundleMessageSource messageSource;
    private final RegSchedFormValidator regSchedFormValidator;
    private final AppProperties appProperties;

    @PostMapping("/timeline")
    @ResponseBody
    public Response workDetailPost(@RequestBody TimeLineForm timeLineForm, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.getTimeLineData(timeLineForm, account);

        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/isWebposButtonExposure")
    @ResponseBody
    public Response isWebposButtonExposure(@RequestBody TimeLineForm timeLineForm, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.isWebposButtonExposure(timeLineForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/regSchedule")
    @ResponseBody
    public Response regSchedule(@RequestBody @Valid SchedForm schedForm, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        scheduleService.scheduleCheck(schedForm, account);

        HashMap<String, Object> hashMap = null;
        hashMap = scheduleService.regSchedule(schedForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /* validator binder */
    /*@InitBinder("schedForm")
    public void initSchedFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(regSchedFormValidator);
    }*/

    @PostMapping("/deleteSchedule")
    @ResponseBody
    public Response deleteProgramSchedule(@RequestBody SchedForm schedForm, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.deleteSchedule(schedForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/scheduleExcelUpload")
    @ResponseBody
    public Response scheduleExcelUpload (@Valid SchedExcelUpload schedExcelUpload, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.scheduleExcelUpload(schedExcelUpload, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }


    @PostMapping("/getUploadHistoryData")
    @ResponseBody
    public Response getUploadHistoryData(@RequestBody @Valid SchedExcelUploadHistory schedExcelUploadHistory, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.getUploadHistoryData(schedExcelUploadHistory, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/copySchedule")
    @ResponseBody
    public Response copySchedule(@RequestBody @Valid CopySchedule copySchedule, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.copySchedule(copySchedule, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/copyNoWorkSchedule")
    @ResponseBody
    public Response copyNoWorkSchedule(@RequestBody @Valid CopyNoWorkSchedule copyNoWorkSchedule, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.copyNoWorkSchedule(copyNoWorkSchedule, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/getGroupTeam")
    @ResponseBody
    public Response getGroupTeam(@CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.getGroupTeam(account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/getGroupTeamMem")
    @ResponseBody
    public Response getGroupTeamMem(@RequestBody GroupTeamMemForm groupTeamMemForm, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.getGroupTeamMem(groupTeamMemForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/getChnlTeamMem")
    @ResponseBody
    public Response getChnlTeamMem(@RequestBody ChnlTeamMemForm chnlTeamMemForm, @CurrentAccount Account account) {
        HashMap<String, Object> hashMap = scheduleService.getChnlTeamMem(chnlTeamMemForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/regMemSched")
    @ResponseBody
    public Response addNoWorkSched(@RequestBody @Valid MemSchedForm memSchedForm, @CurrentAccount Account account) {
        SchedForm schedForm = SchedForm.builder()
                .toDay(memSchedForm.getToDay())
                .startDay(memSchedForm.getStartDay())
                .endDay(memSchedForm.getEndDay())
                .startH(memSchedForm.getStartH())
                .startM(memSchedForm.getStartM())
                .endH(memSchedForm.getEndH())
                .endM(memSchedForm.getEndM())
                .build();
        scheduleService.scheduleCheck(schedForm, account);
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.regMemSched(memSchedForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/deleteMemberSchedule")
    @ResponseBody
    public Response deleteMemberSchedule(@RequestBody MemSchedForm memSchedForm, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.deleteMemberSchedule(memSchedForm, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    @PostMapping("/getWorkDetails")
    @ResponseBody
    public Response getWorkDetails(@RequestBody WorkDetail workDetail, @CurrentAccount Account account) {
        checkRoleAdmin(account);
        HashMap<String, Object> hashMap = scheduleService.getWorkDetails(workDetail, account);
        return Response.builder()
                .status(ResponseEnum.Status.SUCCESS.value())
                .code(ResponseEnum.ErrorCode.AICC_0000_200.value())
                .data(hashMap)
                .message(messageSource.getMessage(ResponseEnum.ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    private void checkRoleAdmin (Account account) {
        if (! (account.hasRole("ROLE_ADMIN") || account.hasRole("ROLE_MANAGER"))) {
            throw new BizException("Timeline.checkRole.admin");
        }
    }

}
