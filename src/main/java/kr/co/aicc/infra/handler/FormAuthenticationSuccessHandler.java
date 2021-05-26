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
 * @package kr.co.aicc.infra.handler
 * @file FormAuthenticationSuccessHandler.java
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
package kr.co.aicc.infra.handler;

import kr.co.aicc.infra.constants.GlobalConstants;
import kr.co.aicc.infra.enums.CommonEnum.MemStat;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.LoginDetail;
import kr.co.aicc.modules.account.repository.AccountDao;
import kr.co.aicc.modules.account.service.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AccountDao accountDao;

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final ReloadableResourceBundleMessageSource messageSource;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        Account account = userAccount.getAccount();

        //-----------------------------------------------
        // 로그인 이력 등록
        //-----------------------------------------------
        try {
            // ip조회
            WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
            String remoteAddress = details.getRemoteAddress();

            // 회원정보조회
            LoginDetail loginDetail = LoginDetail.builder()
                    .memId(userAccount.getAccount().getMemId())
                    .acsDt(LocalDateTime.now())
                    .acsIp(remoteAddress)
                    .lgnScsYn(GlobalConstants.YES)
                    .build();
            // 이력등록(Async)
            accountDao.insertLoginDetail(loginDetail);

            // update 마지막 로그인 시간
            accountDao.updateLastLgnDt(account);
        } catch (Exception e){
            log.error("insertLoginDetail {}:", e);
        }

        //-----------------------------------------------
        // 이메일인증을 하지 않은 경우 이동 처리
        //-----------------------------------------------
        if(!"Y".equalsIgnoreCase(userAccount.getAccount().getEmailAuthYn()) ) {
            log.debug("================ 이메일 인증을 하지 않은 경우 logout 처리 및 이메일 페이지로 이동");
            String emailExpYn = account.canSendConfirmEmail() ? "Y" : "N";
            String targetUrl = "account/check_email?memId=" + account.getMemId() + "&emailExpYn=" + emailExpYn;

            if (authentication != null) {
                new CookieClearingLogoutHandler("JSESSIONID",AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY).logout(request, response, authentication);
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            redirectStrategy.sendRedirect(request, response, targetUrl);
            return;
        }

        //-----------------------------------------------
        // 이메일인증 완료한 고객 상태 체크
        //-----------------------------------------------
        if ("Y".equalsIgnoreCase(account.getEmailAuthYn())) {
            if (!MemStat.MEM_NORMAL.value().equals(account.getStat())) {
                String targetUrl = "/account/sign_in?error=true";

                if (authentication != null) {
                    new CookieClearingLogoutHandler("JSESSIONID",AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY).logout(request, response, authentication);
                    new SecurityContextLogoutHandler().logout(request, response, authentication);
                }

                if (MemStat.MEM_WAIT.value().equals(account.getStat())) {
        			log.info("================ 정회원 승인을 받지 않은 경우 오류 리턴");
        			request.getSession().setAttribute("errorMessage", messageSource.getMessage("AICC_1102_400", null, null));
                } else if (MemStat.MEM_STOP.value().equals(account.getStat())) {
        			log.info("================ 정지 회원");
        	        request.getSession().setAttribute("errorMessage", messageSource.getMessage("AICC_1104_400", null, null));
                } else if (MemStat.MEM_WDL.value().equals(account.getStat())) {
        			log.info("================ 탈퇴 회원");
        			request.getSession().setAttribute("errorMessage", messageSource.getMessage("AICC_1105_400", null, null));
                }

                redirectStrategy.sendRedirect(request, response, targetUrl);
                return;
            }
        }
        //-----------------------------------------------
        // 로그인 성공후 이동 처리
        //-----------------------------------------------
        // 로그인 이후 이동할 경로 설정
        setDefaultTargetUrl("/");
        // 이전 Request (URL) 정보 참조
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest != null) {  // 로그인 전 선택했던 경로로 이동
            String targetUrl = savedRequest.getRedirectUrl();
            log.debug("=====> savedRequest targetUrl : {}", targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        };
    }

}