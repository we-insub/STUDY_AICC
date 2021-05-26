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
 * @file HtmlExceptionAdvice.java
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

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.logging.Level;

@Slf4j
@Order(RestExceptionAdvice.ORDER + 1)
@ControllerAdvice(annotations = Controller.class)
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class HtmlExceptionAdvice {
    private static final String PATH = "/error";

    @ExceptionHandler(RequestRejectedException.class)
    public String handleRequestRejectedException(final HttpServletRequest request, final RequestRejectedException ex) {
        log.error("========== handleRequestRejectedException ==========\n", ex);
        log.error("Rejected request for [ {} ]. Reason: {}", request.getRequestURL().toString(), ex.getMessage());
        return PATH + "/404";
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public String handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        log.error("========== handleEmailNotVerifiedException ==========\n", ex);

        return "account/check_email";
    }

    @ExceptionHandler(MemberNotApprovedException.class)
    public String MemberNotApprovedException(MemberNotApprovedException ex) {
        log.error("========== handleEmailNotVerifiedException ==========\n", ex);

        return "/account/check_email";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(@CurrentAccount Account account, Exception ex, Model model, HttpServletRequest request) {
        log.error("========== handleException ==========\n", ex);
        logRequestUri(account, request);

        if (ex instanceof AccessDeniedException) {
            if (account == null) {
                return "/account/sign_in";
            } else {
                return "/error/denied";
            }
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        setModel(model, request, HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage(), String.valueOf(status));
        return PATH + "/error";
    }

    private void setModel(Model model, HttpServletRequest request, String strException,
                          String message, String status) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", strException);
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("message", message);
        model.addAttribute("status", status);
    }

    private void logRequestUri(@CurrentAccount Account account, HttpServletRequest request) {
        if (account != null) {
            log.info("'{}' requested '{}'", account.getMemId(), request.getRequestURI());
        } else {
            log.info("requested '{}'", request.getRequestURI());
        }
    }
}
