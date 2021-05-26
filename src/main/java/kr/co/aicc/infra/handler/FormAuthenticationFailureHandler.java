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
 * @file FormAuthenticationFailureHandler.java
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
import kr.co.aicc.modules.account.domain.LoginDetail;
import kr.co.aicc.modules.account.repository.AccountDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final AccountDao accountDao;
    private final ReloadableResourceBundleMessageSource messageSource;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException exception)
            throws IOException, ServletException {

        log.info("===== [sign_in fail] : {}, {},", request.getParameter("username"),
                exception.getMessage(), exception);
        //-----------------------------------------------
        // 로그인 이력 등록 - 정확한 회원정보를 알수없음
        // 이력을 mem_no 대신 memId로
        //-----------------------------------------------
        try {
            LoginDetail loginDetail = LoginDetail.builder()
                    .memId(request.getParameter("username"))
                    .acsDt(LocalDateTime.now())
                    .acsIp(request.getRemoteAddr())
                    .lgnScsYn(GlobalConstants.NO)
                    .hstDesc(exception.getMessage())
                    .build();

            // 이력등록(Async)
            accountDao.insertLoginDetail(loginDetail);
        } catch (Exception e){
            log.error("insertLoginDetail {}:", e);
        }

        //-----------------------------------------------
        // 로그인 실패후 이동 처리
        //-----------------------------------------------
        String errorMessage = messageSource.getMessage("AICC_1101_400", null, null);

        /*if(exception instanceof BadCredentialsException) {
            errorMessage = "Invalid Username or Password";
        } else if(exception instanceof DisabledException) {
            errorMessage = "Locked";
        } else if(exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        } else if(exception instanceof SessionAuthenticationException) {
            errorMessage = "접속중인 사용자가 있습니다. 연결을 끊고 로그인하겠습니까?";
        }*/
        if(exception instanceof AuthenticationServiceException){
            errorMessage = exception.getMessage();
        }
        //request.setAttribute("errorMessage", errorMessage);
        request.getSession().setAttribute("errorMessage", errorMessage);

        saveException(request, exception);
        String defaultFailureUrl = "/account/sign_in?error=true";
        redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
        //request.getRequestDispatcher(defaultFailureUrl).forward(request, response);

//        setDefaultFailureUrl("/account/sign_in?error=true&exception=" + errorMessage);
//        super.onAuthenticationFailure(request, response, exception);
    }
}