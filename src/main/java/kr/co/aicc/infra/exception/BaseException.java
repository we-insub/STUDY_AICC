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
 * @file BaseException.java
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

public class BaseException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new base exception.
	 */
	public BaseException() {
		super();
	}
	
	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 */
	public BaseException(String message) {
        super(message);
    }
	
	/**
	 * Instantiates a new base exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
	
	/**
	 * Instantiates a new base exception.
	 *
	 * @param cause the cause
	 */
	public BaseException(Throwable cause) {
        super(cause);
    }
	
	/**
	 * getRootCause
	 * @return
	 */
	public Throwable getRootCause() {
        return getCause();
    }
	
}
