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
 * @package kr.co.aicc.infra.config
 * @file SecurityConfig.java
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
 * 2020. 06. 18	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.webfos.service;

import java.util.List;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListReq;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListRes;
import kr.co.aicc.modules.webfos.dto.LineInfo;
import kr.co.aicc.modules.webfos.dto.ProgramInfo;
import kr.co.aicc.modules.webfos.dto.ScheduleInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.dto.TransServerInfo;
import kr.co.aicc.modules.webfos.dto.UserInfo;
import kr.co.aicc.modules.webfos.dto.WebfosMessage;
import kr.co.aicc.modules.webfos.dto.WebfosProgram;

public interface WebfosService {

	WebfosProgram createProgram(String schedNo, String progNm);
	
	boolean checkMemberOfSchedule(Long schedNo, Long memNo);
	
	boolean checkTransmissionPermission(String schedNo);
	
	void subscribeOfProgram(String schedNo, Account account, String sessionId);
	
	String disconnectedProgram(String sessionId, Account account);
	
	void sendWebfosMessage(WebfosMessage webfosMessage);
	
	List<UserInfo> getUserInfoOfProgram(String schedNo);
	
	ScheduleInfo getScheduleInfo(Long schedNo);
	
	List<SubtitleDetailInfo> getSubtitleDetailListOfChennal(Long chnlNo, String searchNm, String searchDt, Long srchSchedNo);
	
	int getChnlSubDtlListCnt(ChnlSubDtlListReq chnlSubDtlListReq);
	
	List<ChnlSubDtlListRes> getChnlSubDtlList(ChnlSubDtlListReq chnlSubDtlListReq);
	
	List<LineInfo> getSubtitleDetailInfo(Long targetSchedNo, int maxByte);
	
	List<ProgramInfo> getProgramInfoListOfWebfos();
	
	void sendSubtileDetailOfRerun(String schedNo, String transWords, Long memNo, String localPort);
	
	List<TransServerInfo> getTrnsSvrInfo(Long chnlNo);
}
