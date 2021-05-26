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
package kr.co.aicc.modules.webfos.repository;

import java.util.List;

import kr.co.aicc.modules.webfos.domain.AutoTxt;
import kr.co.aicc.modules.webfos.domain.SubtitleDetail;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListReq;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListRes;
import kr.co.aicc.modules.webfos.dto.MemberInfo;
import kr.co.aicc.modules.webfos.dto.ScheduleInfo;
import kr.co.aicc.modules.webfos.dto.SearchInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.dto.TransServerInfo;
import kr.co.aicc.modules.webfos.dto.UserInfo;
import kr.co.aicc.modules.webfos.dto.WebfosProgram;

public interface WebfosDao {
	
	/** Redis */ 
	List<WebfosProgram> findAllProgram();
	
	WebfosProgram findBySchedNoProgram(String schedNo);
	
	WebfosProgram createProgram(String schedNo, String progNm);
	
	void deleteProgram(String schedNo);
	
	void createParticipatingProgram(String sessionId, String schedNo);
	
	String findBySessionIdParticipatingProgram(String sessionId);
	
	void deleteParticipatingProgram(String sessionId);
	
	void createUserOfProgram(String schedNo, String memId);
	
	void deleteUserOfProgram(String schedNo, String memId);
	
	List<UserInfo> findBySchedNoUserOfProgram(String schedNo);
	
	void createWritePermissionOfProgram(String schedNo, String memId);
	
	void deleteWritePermissionOfProgram(String schedNo, String memId);
	
	List<String> findBySchedNoWritePermissionOfProgram(String schedNo);
	
	void createTransmissionPermissionOfProgram(String schedNo, String memId);
	
	int deleteTransmissionPermissionOfProgram(String schedNo, String memId);
	
	List<String> findBySchedNoTransmissionPermissionOfProgram(String schedNo);
	
	/** PostgreSQL */
	List<Long> findBySchedNoAndMemNoMemSched(Long schedNo, Long memNo);
	
	List<MemberInfo> findBySchedNoMemSched(Long schedNo);
	
	String findByKeywordAndMemNoMemAutoTxt(String key, Long memNo);
	
	int createMemAutoTxt(Long memNo, String key, String value, String overwrite);
	
	boolean updateMemAutoTxt(Long memNo, String key, String value);
	
	boolean deleteMemAutoTxt(Long memNo, String key);
	
	List<AutoTxt> findByMemNoMemAutoTxt(Long memNo);
	
	List<AutoTxt> findBySearchInfoMemAutoTxt(SearchInfo searchInfo);
	
	// 송출
	List<SubtitleDetailInfo> findByChnlNoOfSchedSubDtl(Long chnlNo, String searchNm, String searchDt, Long srchSchedNo);
	
	int selChnlSubDtlListCnt(ChnlSubDtlListReq chnlSubDtlListReq);
	
	List<ChnlSubDtlListRes> selChnlOfSubDtlList(ChnlSubDtlListReq chnlSubDtlListReq);
	
	int createSubDtl(SubtitleDetail subtitleDetail);
	
	boolean updateSubDtl(SubtitleDetail subtitleDetail);
	
	ScheduleInfo findOneBySchedNoSched(Long schedNo);
	
	String findOneBySchedNoSubDtl(Long targetSchedNo);
	
	List<TransServerInfo> findByChnlNoTrnsSvr(Long chnlNo);

	boolean deleteMemAllAutoTxt(Long memNo);
}
