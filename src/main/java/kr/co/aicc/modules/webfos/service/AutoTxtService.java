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

import org.springframework.web.multipart.MultipartFile;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.webfos.dto.AutoTxtInfo;
import kr.co.aicc.modules.webfos.dto.SearchInfo;

public interface AutoTxtService {

	String getWordOfAutoTxt(String key, Long memNo);
	
	String createWordOfAutoTxt(Long memNo, String key, String value, String overwrite);
	
	String updateWordOfAutoTxt(Long memNo, String key, String value);
	
	String deleteWordOfAutoTxt(Long memNo, String key);
	
	List<AutoTxtInfo> getAutoTxtInfoList(Long memNo);
	
	List<AutoTxtInfo> getAutoTxtInfoList(SearchInfo searchInfo);
	
	void autoTxtUpload(Account account, MultipartFile uploadFile);
	
	String autoTxtDownload(Account account);
	
	void rerunAutoTxtUpload(Account account, MultipartFile uploadFile);
}
