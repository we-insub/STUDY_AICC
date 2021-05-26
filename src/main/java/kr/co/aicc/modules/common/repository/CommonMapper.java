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
 * @file CommonMapper.java
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

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommonMapper {
    List<Code> getCodeList();

    List<Role> findRoleList();
    
    List<ResourceDto> findResList();

    List<ResourceDto> findResList(Account account);
    
    ResourceDto findResSel(String requestUrl);
}
