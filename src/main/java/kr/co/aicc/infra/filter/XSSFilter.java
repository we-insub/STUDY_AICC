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
 * @file XSSFilter.java
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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
public class XSSFilter extends OncePerRequestFilter  { // cf. OpenSessionInViewFilter 최대절전모드?? 쓰는게 맞나??? 추후고민
	static final Pattern STATIC_RESOURCES = Pattern.compile(
			"(^/resources/css/.*)|(^/resources/font/.*)|(^/resources/image/.*)|(^/resources/js/.*)|(^/resources/webfonts/.*)");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getServletPath();
//		log.debug(">>>>> path: {}", path);

		if (STATIC_RESOURCES.matcher(path).matches()) {
//			log.debug(">>>>> static resources");
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
		}
	}
}
