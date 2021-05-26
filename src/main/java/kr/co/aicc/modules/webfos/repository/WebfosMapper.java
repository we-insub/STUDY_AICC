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
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.aicc.modules.webfos.domain.AutoTxt;
import kr.co.aicc.modules.webfos.domain.SubtitleDetail;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListReq;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListRes;
import kr.co.aicc.modules.webfos.dto.MemberInfo;
import kr.co.aicc.modules.webfos.dto.ScheduleInfo;
import kr.co.aicc.modules.webfos.dto.SearchInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.dto.TransServerInfo;

@Repository
@Mapper
public interface WebfosMapper {
	
	List<Long> findBySchedNoAndMemNoMemSched(Map<String, Object> paramMap);
	
	List<MemberInfo> findBySchedNoMemSched(Long schedNo);
	
	String findByKeywordAndMemNoMemAutoTxt(Map<String, Object> paramMap);
	
	int insertMemAutoTxt(AutoTxt autoTxt);
	
	boolean updateMemAutoTxt(AutoTxt autoTxt);
	
	boolean deleteMemAutoTxt(AutoTxt autoTxt);
	
	List<AutoTxt> findByMemNoMemAutoTxt(Long memNo);
	
	List<AutoTxt> findBySearchInfoMemAutoTxt(SearchInfo searchInfo);
	
	List<SubtitleDetailInfo> findByChnlNoOfSchedSubDtl(Map<String, Object> paramMap);
	
	int selChnlSubDtlListCnt(ChnlSubDtlListReq chnlSubDtlListReq);
	
	List<ChnlSubDtlListRes> selChnlSubDtlList(ChnlSubDtlListReq chnlSubDtlListReq);
	
	int insertSubDtl(SubtitleDetail subtitleDetail);
	
	boolean updateSubDtl(SubtitleDetail subtitleDetail);
	
	ScheduleInfo findOneBySchedNoSched(Long schedNo);
	
	String findOneBySchedNoSubDtl(Long targetSchedNo);
	
	List<TransServerInfo> findByChnlNoTrnsSvr(Long chnlNo);

	boolean deleteMemAllAutoTxt(AutoTxt build);
}
