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
 * @package kr.co.aicc.modules.account.controller
 * @file AccountRestController.java
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
package kr.co.aicc.modules.account.controller;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.constants.GlobalConstants;
import kr.co.aicc.infra.enums.ResponseEnum.ErrorCode;
import kr.co.aicc.infra.enums.ResponseEnum.Status;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.dto.Test;
import kr.co.aicc.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/account")
public class AccountRestController {
    private final ReloadableResourceBundleMessageSource messageSource;
    private final AccountService accountService;
    private final StringRedisTemplate redisTemplate;

    /**
     * 현재 접속된 회원아이디 목록을 응답해주는 API
     *
     * ex. Response
     * {
     *   "status": "success",
     *   "data": {
     *     "users": [
     *       "user1",
     *       "user2"
     *     ]
     *   },
     *   "code": "AICC.0000.200",
     *   "message": "정상"
     * }
     *
     * @return
     */
    @GetMapping(value = "active")
    @ResponseBody
    public Response activeMember() {
        Set<String> sessionKeys = redisTemplate.keys(
                GlobalConstants.SESSION_KEY_PATTERN+"*");
        List<String> memberList = new ArrayList<>();
        Iterator<String> it = sessionKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            memberList.add(data.replace(GlobalConstants.SESSION_KEY_PATTERN,""));
        }

        log.debug("### activeMember : {}", memberList);

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("users", memberList);
        return Response.builder()
                .status(Status.SUCCESS.value())
                .code(ErrorCode.AICC_0000_200.value())
                .data(result)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * AICC에서 사용하는 REST API 예제.
     *
     * - 외부에 제공하는 OPEN API는 아니므로 OAuth인증이나, JWT인증을 사용하지않음
     * - 401(Unauthorized), 403(Forbidden) 과 같은 경우는 JSON응답처리하지않고,
     *   해당 HTML페이지로 이동함
     *
     * - 업무적인(Biz) 오류는 BizException throw처리
     * ex. throw new BizException(ErrorCode.AICC_1102_400.value(), new Object[]{"userId"},null);    	// {0} - 사용자 정보 미존재
     * - DTO validation은 DTO에 JSR303, JSR380 로 정의 후 @Valid DTO앞에 붙이면 됩니다.
     *  ex.
     *  {
     *   "status": "error",
     *   "code": "AICC.1000.400",
     *   "message": "BAD REQUEST",
     *   "errors": [
     *     {
     *       "field": "data2",
     *       "value": "mbc",
     *       "reason": "반드시 최소값 5과(와) 최대값 11 사이의 크기이어야 합니다."
     *     },
     *     {
     *       "field": "data1",
     *       "value": "",
     *       "reason": "반드시 값이 존재하고 공백 문자를 제외한 길이가 0보다 커야 합니다."
     *     }
     *   ]
     * }
     *
     * @param test
     * @return
     */
    @PostMapping(value = "test")
    @ResponseBody
    public Response test(@RequestBody @Valid final Test test) {
        // Service 호출 진행....test....
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("user", accountService.getActiveMembers());

        return Response.builder()
                .status(Status.SUCCESS.value())
                .code(ErrorCode.AICC_0000_200.value())
                .data(result)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }


    /**
     * 이메일 인증메일 재전송요청페이지
     * @param account
     * @param model
     * @return
     */
    @PostMapping("/resend_confirm_email")
    @ResponseBody
    public Response resendConfirmEmail(@RequestParam String memId) {
    	log.debug("memId ::: " + memId);

    	Account account = accountService.findByMemId(memId);

    	String status = Status.SUCCESS.value();

    	if (account == null) {
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .message("요청하신 아이디가 확인되지 않습니다. <br/>관리자에게 문의해주세요.")
                    .build();
    	}

        if (!account.canSendConfirmEmail()) {
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .message("이미 발송 완료된 유효기간이 만료되지 않은 인증 메일이 존재합니다. <br/>등록하신 메일 계정의 수신 내역에서 확인 후 인증을 진행해주세요.")
                    .build();
        }
        try {
            accountService.resendConfirmEmail(account);
        } catch (Exception e) {
        	e.printStackTrace();
    		status = Status.ERROR.value();
        }
        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }
}
