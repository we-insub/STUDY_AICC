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
 * @file BizException.java
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

import java.text.MessageFormat;
import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class BizException extends BaseException {

	private String messageId;
	private Object[] messageParams;
	private LinkedHashMap<String, Object> dataBox;
	
	/**
	 * Instantiates a new biz exception.
	 */
	public BizException() {
		super();
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 */
	public BizException(String messageId) {
		super("");
		this.messageId = messageId;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 * @param defaultMessage the default message
	 */
	public BizException(String messageId, String defaultMessage) {
		super(defaultMessage);
		this.messageId = messageId;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 * @param defaultMessage the default message
	 * @param cause the cause
	 */
	public BizException(String messageId, String defaultMessage, Throwable cause) {
		super(defaultMessage, cause);
		this.messageId = messageId;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 * @param messageParams the message params
	 */
	public BizException(String messageId, Object[] messageParams) {
		super("");
		this.messageId = messageId;
		this.messageParams = messageParams;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 * @param defaultMessage the default message
	 * @param messageParams the message params
	 */
	public BizException(String messageId, String defaultMessage, Object[] messageParams) {
		super(defaultMessage);
		this.messageId = messageId;
		this.messageParams = messageParams;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param messageId the message id
	 * @param defaultMessage the default message
	 * @param messageParams the message params
	 * @param cause the cause
	 */
	public BizException(String messageId, String defaultMessage, Object[] messageParams, Throwable cause) {
		super(defaultMessage, cause);
		this.messageId = messageId;
		this.messageParams = messageParams;
	}

	/**
	 * Instantiates a new normal exception.
	 *
	 * @param cause the cause
	 */
	public BizException(Throwable cause) {
		super(cause);
		if (cause instanceof BizException) {
			BizException bizException = ((BizException) cause);
			this.messageId = bizException.messageId;
			this.messageParams = bizException.messageParams;
			this.dataBox = bizException.dataBox;
		}
	}
	
	/**
	 * Instantiates a new biz exception.
	 *
	 * @param messageId the message id
	 * @param dataBox the data box
	 */
	public BizException(String messageId, LinkedHashMap<String, Object> dataBox) {
		super("");
		this.messageId = messageId;
		this.dataBox = dataBox;
	}
	
	/**
	 * Instantiates a new biz exception.
	 *
	 * @param messageId the message id
	 * @param messageParams the message params
	 * @param dataBox the data box
	 */
	public BizException(String messageId, Object[] messageParams, LinkedHashMap<String, Object> dataBox) {
		super("");
		this.messageId = messageId;
		this.messageParams = messageParams;
		this.dataBox = dataBox;
	}

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Gets the message params.
	 *
	 * @return the message params
	 */
	public Object[] getMessageParams() {
		return messageParams;
	}
	
	/**
	 * Gets the data box.
	 *
	 * @return the data box
	 */
	public LinkedHashMap<String, Object> getDataBox() {
		return dataBox;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
        if (messageParams == null) {
        	return super.getMessage();
        }
        return MessageFormat.format(super.getMessage(), messageParams);
    }

    /**
     * To string.
     *
     * @return the string
     */
    /* (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": [" + messageId + "] " + message) : (s + ": [" + messageId + "]");
    }
	
}
