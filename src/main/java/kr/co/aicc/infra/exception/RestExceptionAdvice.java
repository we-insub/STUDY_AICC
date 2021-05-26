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
 * @package kr.co.aicc.infra.exception
 * @file RestExceptionAdvice.java
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
package kr.co.aicc.infra.exception;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.enums.ResponseEnum.ErrorCode;
import kr.co.aicc.infra.enums.ResponseEnum.Status;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(RestExceptionAdvice.ORDER)
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionAdvice {
    public static final int ORDER = 0;
    private final ReloadableResourceBundleMessageSource messageSource;
//    private final CodeCacheService codeCacheService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidationExceptions(@CurrentAccount Account account,
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.debug("===== Validation Error : {}", ex.getBindingResult().getAllErrors());
        logRequestUri(account, request);

        final BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> errors = bindingResult.getFieldErrors();

        return Response.builder()
                .status(Status.ERROR.value())
                .code(ErrorCode.AICC_1000_400.value())
                .message(ErrorCode.AICC_1000_400.dtlNm())
                .errors(errors.parallelStream()
                        .map(error -> Response.FieldError.builder()
                                .reason(error.getDefaultMessage())
                                .field(error.getField())
                                .value((String) error.getRejectedValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(value=HttpStatus.OK)
    public Response handleBizException(@CurrentAccount Account account, BizException ex, HttpServletRequest request) {
        log.error("========== handleBizException ==========\n ", ex);
        logRequestUri(account, request);

        // msgId, msgParams
        String msgId = null;
        Object[] msgParams = null;
        if(ex != null) {
            msgId = ((BizException)ex).getMessageId();
            msgParams = ((BizException)ex).getMessageParams();
        }
        log.debug("msgId: {}, msgParams: {} ", msgId, msgParams);

        if (StringUtils.isEmpty(msgId)) { // 정의되지 않은 오류인 경우
            msgId = ErrorCode.AICC_9001_500.value();
        }

        return Response.builder()
                .status(Status.ERROR.value())
                .code(msgId)
                .message(messageSource.getMessage(msgId, msgParams, null))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(@CurrentAccount Account account, Exception ex, HttpServletRequest request) {
        log.error("========== handleException ==========\n", ex);
        logRequestUri(account, request);

        return Response.builder()
                .status(Status.ERROR.value())
                .code(ErrorCode.AICC_9001_500.value())
                .message(messageSource.getMessage(ErrorCode.AICC_9001_500.value(), null, null))
                .build();
    }

    private void logRequestUri(@CurrentAccount Account account, HttpServletRequest request) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getMemId(), request.getRequestURI());
        } else {
            log.info("requested '{}'", request.getRequestURI());
        }
    }
}
