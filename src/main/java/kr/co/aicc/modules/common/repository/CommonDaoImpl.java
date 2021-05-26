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
 * @package kr.co.aicc.modules.common.repository
 * @file CommonDaoImpl.java
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
package kr.co.aicc.modules.common.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.settings.domain.Code;
import kr.co.aicc.modules.settings.dto.ResourceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommonDaoImpl implements CommonDao {
    private final CommonMapper settingsMapper;

    @Override
    @Cacheable(value = "codeCache")
    public List<Code> getCodeList() {
        return settingsMapper.getCodeList();
    }

	@Override
	public List<Role> findRoleList() {
		return settingsMapper.findRoleList();
	}

	@Override
    @Cacheable(value = "resCache")
	public List<ResourceDto> findResList() {
		return settingsMapper.findResList();
	}

	@Override
    @Cacheable(value = "resCache")
	public List<ResourceDto> findResList(Account account) {
		return settingsMapper.findResList(account);
	}

	@Override
	public ResourceDto findResSel(String requestUrl) {
		return settingsMapper.findResSel(requestUrl);
	}
}
