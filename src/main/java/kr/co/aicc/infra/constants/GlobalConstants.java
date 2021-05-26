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
 * @package com.olleh.gpay.common.constants
 * @file GlobalConstants.java
 * @author developer
 * @since 2018. 3. 01
 * @version 1.0
 * @title GPAY
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
package kr.co.aicc.infra.constants;

public class GlobalConstants {

	/* Encode type */
	public static final String UTF8 = "UTF-8";
	public static final String EUCKR = "EUC-KR";
	public static final String MS949 = "MS949";
	public static final String CHARSET = UTF8;

	/* System */
	public static final String SYSTEM_SVC_NAME = "AICC";

	/* Response code */
	public static final String RES_CODE_SUCCESS = "200";  		// 정상
	public static final String RES_CODE_UNAUTHORIZED = "401";  	// UNAUTHORIZED
	public static final String RES_CODE_FORBIDDEN = "403";  	// FORBIDDEN
	public static final String RES_CODE_NOT_FOUND = "404";  	// NOT FOUND
	public static final String RES_CODE_ERROR = "500";  		// 시스템 점검중 입니다.

	/* HTTP Header Key */
	public static final String HTTP_HEADER_ACCEPT = "Accept";
	public static final String HTTP_HEADER_ACCEPT_CHARSET = "Accept-Charset";
	public static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
	public static final String HTTP_HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
	
	/* HTTP Req/Res Key */
	public static final String REQUEST_BODY = "requestBody";
	public static final String RESPONSE_BODY = "responseBody";
	
	/** Algorithm */
	/** Random Number Generation Algorithm name SHA-1 */
    public static final String ALGM_NAME_RNG_SHA1 = "SHA1PRNG";
    /** SHA-1 algorithm name */
    public static final String ALG_NAME_HASH_SHA1 = "SHA-1";
    /** SHA-256 algorithm name */
    public static final String ALG_NAME_HASH_SHA256 = "SHA-256";
    /** SHA-512 algorithm name */
    public static final String ALG_NAME_HASH_SHA512 = "SHA-512";
    
    /** YES NO */    
    public static final String YES = "Y";
    public static final String NO = "N";
    
    /** separator */
    public static final String SEP  = System.getProperty("file.separator");
    
    /** config file */
    public static final String CONFIG_FILE = 
    		System.getProperty("file.separator")+"properties"+System.getProperty("file.separator")+"config.properties";
    /** config path */
    public static final String CONFIG_PATH = "classpath:"+System.getProperty("file.separator")+"config";
    /** SYSTEM_CINFIG_PATH */ 
    public static final String SYSTEM_CINFIG_PATH = "aicc.config.path";

	public static final String SESSION_KEY_PATTERN = "spring:session:index:org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME:";
}
