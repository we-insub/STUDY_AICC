/*
 * GPAY (GiGAGenie Payment) version 1.0
 *
 *  Copyright ⓒ 2015 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 * 
 * @package com.olleh.gpay.api.common.controller
 * @file ErrorController.java
 * @author developer
 * @since 2018. 3. 01
 * @version 1.0
 * @title AICC
 * @description 
 *  
 * 
 * << 개정이력 (Modification Information) >>
 * 
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2018. 3. 01	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.infra.common.controller;

import kr.co.aicc.infra.exception.BaseException;
import kr.co.aicc.infra.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("common/error")
public class ErrorController {
	/**
	 * pageNotFound
	 */
	@RequestMapping("/pageNotFound")
	@ResponseBody
    public String pageNotFound(HttpServletRequest request) {
		System.out.println(request.getHeaderNames());
		log.debug("================= ErrorController pageNotFound controller Start =================");
		return "not found";
//		throw new ResourceNotFoundException();
//		throw new BizException(ErrorCode.GPAY_0000_404.value(), ErrorCode.GPAY_0000_404.dtlNm());		
    }

	/**
	 * defaultServletExceptionResponse
	 * @param request
	 * @param response
	 */
    @RequestMapping("/unknownException")
    @ResponseBody
    public void defaultServletExceptionResponse(HttpServletRequest request, HttpServletResponse response) {
    	log.debug("================= ErrorController defaultServletExceptionResponse controller Start =================");
    	throw new BizException("ErrorCode.GPAY_9001_500.value()", "ErrorCode.GPAY_9001_500.dtlNm()");
    } 
    
    /**
	 * Uncaught exception.
	 *
	 * @param request the request
	 */
	@RequestMapping("/interceptorException")
	public void uncaughtException(HttpServletRequest request) {
		Throwable throwable = (Throwable) request.getAttribute("interceptorException");
		if(throwable == null) {
			throw new BaseException();
		} else if(throwable instanceof BizException) {
			throw new BizException(throwable);	
		} else {
			throw new BaseException(throwable);
		}
	}
}
