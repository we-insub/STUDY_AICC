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
 * @file EmailNotVerifiedException.java
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

@SuppressWarnings("serial")
public class EmailNotVerifiedException extends BaseException {

	/**
	 * Instantiates a new resource not found exception.
	 */
	public EmailNotVerifiedException() {
		super();
	}

	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param message the message
	 */
	public EmailNotVerifiedException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public EmailNotVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param cause the cause
	 */
	public EmailNotVerifiedException(Throwable cause) {
		super(cause);
	}
	
}
