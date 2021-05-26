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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.repository.AccountMapper;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WebfosDaoImpl implements WebfosDao {

	private static final String PROGRAM = "PROG";
	private static final String PARTICIPATION = "PART";
	private static final String WRITE = "AUTHW"; // 글쓰기 권한
	private static final String TRANSMISSION = "AUTHT"; // 송출 권한
	
	@Resource(name = "redisTemplate")
    private HashOperations<String, String, WebfosProgram> hashOpsPrograms;
	
	@Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsParticipatingPrograms;
	
	@Resource(name = "redisTemplate")
    private HashOperations<String, String, UserInfo> hashOpsParticipatingUsers;
	
	@Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsParticipatingUserInfo;
	
	private final WebfosMapper webfosMapper;
	private final AccountMapper accountMapper;
	
	/** Redis */
	// 프로그램방 생성/조회/삭제
	@Override
    public List<WebfosProgram> findAllProgram() {
        return hashOpsPrograms.values(PROGRAM);
    }

	@Override
    public WebfosProgram findBySchedNoProgram(String schedNo) {
        return hashOpsPrograms.get(PROGRAM, schedNo);
    }

	@Override
    public WebfosProgram createProgram(String schedNo, String progNm) {
    	WebfosProgram program = WebfosProgram.builder().schedNo(schedNo).progNm(progNm).build();
        
        hashOpsPrograms.put(PROGRAM, program.getSchedNo(), program);
        
        return program;
    }
    
	@Override
    public void deleteProgram(String schedNo) {
    	hashOpsPrograms.delete(PROGRAM, schedNo);
    }
	
	// Webfos 작업을 위하여 사용자가 입장한 프로그램방 정보 생성/조회/삭제
	@Override
    public void createParticipatingProgram(String sessionId, String schedNo) {
		// 생성시 simple sessionId를 hashkey로 사용시 문제가 될만한 소지가 있음
		// 좀 더 테스트 진행 후 확인이 필요
		hashOpsParticipatingPrograms.put(PARTICIPATION, sessionId, schedNo);
    }

	@Override
    public String findBySessionIdParticipatingProgram(String sessionId) {
        return hashOpsParticipatingPrograms.get(PARTICIPATION, sessionId);
    }

	@Override
    public void deleteParticipatingProgram(String sessionId) {
		hashOpsParticipatingPrograms.delete(PARTICIPATION, sessionId);
    }
	
	// 프로그램방에 입장한 사용자 정보 생성/조회/삭제
	@Override
    public void createUserOfProgram(String schedNo, String memId) {
		Account account = accountMapper.findByMemId(memId);
		
		if (!ObjectUtils.isEmpty(account)) {
			UserInfo userInfo = UserInfo.builder().memId(memId).memNm(account.getMemNm()).build();
			hashOpsParticipatingUsers.put(schedNo, getHashkey(schedNo, memId), userInfo);
		}
    }
    
	@Override
    public void deleteUserOfProgram(String schedNo, String memId) {
		hashOpsParticipatingUsers.delete(schedNo, getHashkey(schedNo, memId));
    }
    
	@Override
    public List<UserInfo> findBySchedNoUserOfProgram(String schedNo) {
        return hashOpsParticipatingUsers.values(schedNo);
    }
	
    // 프로그램방에 입장한 사용자들의 글쓰기권한 정보 생성/조회/삭제
	@Override
    public void createWritePermissionOfProgram(String schedNo, String memId) {
    	List<String> writePermissionList = hashOpsParticipatingUserInfo.values(getHashkey(WRITE, schedNo));
    	
    	// 각 방당 글쓰기 권한은 2명으로 제한
    	if (CollectionUtils.isEmpty(writePermissionList) || writePermissionList.size() < 2) {
    		hashOpsParticipatingUserInfo.put(getHashkey(WRITE, schedNo), getHashkey(schedNo, memId), memId);
    	}
    }
    
	@Override
    public void deleteWritePermissionOfProgram(String schedNo, String memId) {
    	hashOpsParticipatingUserInfo.delete(getHashkey(WRITE, schedNo), getHashkey(schedNo, memId));
    }
    
	@Override
    public List<String> findBySchedNoWritePermissionOfProgram(String schedNo) {
        return hashOpsParticipatingUserInfo.values(getHashkey(WRITE, schedNo));
    }
    
    // 프로그램방에 송출 권한 정보 생성/조회/삭제
	@Override
    public void createTransmissionPermissionOfProgram(String schedNo, String memId) {
    	List<String> transPermissionList = hashOpsParticipatingUserInfo.values(getHashkey(TRANSMISSION, schedNo));
    	
    	// 각 프로그램방당 송출 권한은 1명으로 제한
    	if (CollectionUtils.isEmpty(transPermissionList) || transPermissionList.size() < 1) {
    		hashOpsParticipatingUserInfo.put(getHashkey(TRANSMISSION, schedNo), getHashkey(schedNo, memId), memId);
    	}
    }
    
    // 프로그램방에 송출 권한 정보 삭제
	@Override
    public int deleteTransmissionPermissionOfProgram(String schedNo, String memId) {
    	return hashOpsParticipatingUserInfo.delete(getHashkey(TRANSMISSION, schedNo), getHashkey(schedNo, memId)).intValue();
    }
    
    // 특정 프로그램방의 송출 권한 조회
	@Override
    public List<String> findBySchedNoTransmissionPermissionOfProgram(String schedNo) {
        return hashOpsParticipatingUserInfo.values(getHashkey(TRANSMISSION, schedNo));
    }
    
	/** PostgreSQL */
	// 특정 스케줄에 배정된 멤버 조회 
	@Override
	public List<Long> findBySchedNoAndMemNoMemSched(Long schedNo, Long memNo) {
		Map<String, Object> paramMap  = new HashMap<String, Object>();

		paramMap.put("schedNo", schedNo);
		paramMap.put("memNo", memNo);
		
		return webfosMapper.findBySchedNoAndMemNoMemSched(paramMap);
	}
	
	// 특정 스케줄에 배정된 멤버 리스트 조회 
	public List<MemberInfo> findBySchedNoMemSched(Long schedNo) {
		return webfosMapper.findBySchedNoMemSched(schedNo);
	}
	
	// 상용구 등록/조회/삭제
	@Override
	public String findByKeywordAndMemNoMemAutoTxt(String key, Long memNo) {
		Map<String, Object> paramMap  = new HashMap<String, Object>();
		
		paramMap.put("keyword", key);
		paramMap.put("memNo", memNo);
		
		return webfosMapper.findByKeywordAndMemNoMemAutoTxt(paramMap);
	}
	
	@Override
	public int createMemAutoTxt(Long memNo, String key, String value, String overwrite) {
		return webfosMapper.insertMemAutoTxt(AutoTxt.builder().memNo(memNo).keyword(key).autotxt(value).overwrite(overwrite).build());
	}
	
	@Override
	public boolean updateMemAutoTxt(Long memNo, String key, String value) {
		return webfosMapper.updateMemAutoTxt(AutoTxt.builder().memNo(memNo).keyword(key).autotxt(value).build());
	}
	
	@Override
	public boolean deleteMemAutoTxt(Long memNo, String key) {
		return webfosMapper.deleteMemAutoTxt(AutoTxt.builder().memNo(memNo).keyword(key).build());
	}
	
	@Override
	public boolean deleteMemAllAutoTxt(Long memNo) {
		return webfosMapper.deleteMemAllAutoTxt(AutoTxt.builder().memNo(memNo).build());
	}
	

	@Override
	public List<AutoTxt> findByMemNoMemAutoTxt(Long memNo) {
		return webfosMapper.findByMemNoMemAutoTxt(memNo);
	}
	
	@Override
	public List<AutoTxt> findBySearchInfoMemAutoTxt(SearchInfo searchInfo) {
		return webfosMapper.findBySearchInfoMemAutoTxt(searchInfo);
	}
	
	@Override
	public List<SubtitleDetailInfo> findByChnlNoOfSchedSubDtl(Long chnlNo, String searchNm, String searchDt, Long srchSchedNo) {
		Map<String, Object> paramMap  = new HashMap<String, Object>();
		
		paramMap.put("chnlNo", chnlNo);
		paramMap.put("progNm", searchNm);
		paramMap.put("searchDt", searchDt);
		paramMap.put("srchSchedNo", srchSchedNo);
		
		return webfosMapper.findByChnlNoOfSchedSubDtl(paramMap);
	}
	
	@Override
	public int selChnlSubDtlListCnt(ChnlSubDtlListReq chnlSubDtlListReq) {
		return webfosMapper.selChnlSubDtlListCnt(chnlSubDtlListReq);
	}
	
	@Override
	public List<ChnlSubDtlListRes> selChnlOfSubDtlList(ChnlSubDtlListReq chnlSubDtlListReq) {
		return webfosMapper.selChnlSubDtlList(chnlSubDtlListReq);
	}
	
	@Override
	public int createSubDtl(SubtitleDetail subtitleDetail) {
		return webfosMapper.insertSubDtl(subtitleDetail);
	}
	
	@Override
	public boolean updateSubDtl(SubtitleDetail subtitleDetail) {
		return webfosMapper.updateSubDtl(subtitleDetail);
	}
	
	@Override
	public ScheduleInfo findOneBySchedNoSched(Long schedNo) {
		return webfosMapper.findOneBySchedNoSched(schedNo);
	}
	
	@Override
	public String findOneBySchedNoSubDtl(Long targetSchedNo) {
		return webfosMapper.findOneBySchedNoSubDtl(targetSchedNo);
	}
	
	@Override
	public List<TransServerInfo> findByChnlNoTrnsSvr(Long chnlNo) {
		return webfosMapper.findByChnlNoTrnsSvr(chnlNo);
	}
	
	private String getHashkey(String id, String key) {
		StringBuffer hashKey = new StringBuffer();
    	
    	hashKey.append(id);
    	hashKey.append("_");
    	hashKey.append(key);
    	
    	return hashKey.toString();
	}
}

