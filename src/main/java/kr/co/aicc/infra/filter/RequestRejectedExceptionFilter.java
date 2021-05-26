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
 * @package kr.co.aicc.infra.filter
 * @file RequestRejectedExceptionFilter.java
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
package kr.co.aicc.infra.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestRejectedExceptionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            RequestRejectedException requestRejectedException=(RequestRejectedException) servletRequest.getAttribute("isNormalized");
            if(Objects.nonNull(requestRejectedException)) {
                throw requestRejectedException;
            }else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (RequestRejectedException requestRejectedException) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            log
                    .error(
                            "request_rejected: remote={}, user_agent={}, request_url={}",
                            httpServletRequest.getRemoteHost(),
                            httpServletRequest.getHeader(HttpHeaders.USER_AGENT),
                            httpServletRequest.getRequestURL(),
                            requestRejectedException
                    );

            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
