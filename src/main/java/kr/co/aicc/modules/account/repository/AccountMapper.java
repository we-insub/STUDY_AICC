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
 * @package kr.co.aicc.modules.account.repository
 * @file AccountMapper.java
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
package kr.co.aicc.modules.account.repository;

import kr.co.aicc.modules.account.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import kr.co.aicc.modules.account.dto.FindAccount;

import java.util.List;

@Repository
@Mapper
public interface AccountMapper {
    int insertMember(Account account);
    
    int insertMemRole(Account account);

    Account findByMemId(String memId);

    Account findByEmail(Account account);

    int updateEmailTokenIssueDt(Account account);

    int insertLoginDetail(LoginDetail loginDetail);

	List<Terms> selectTermsList(Terms terms);
	
	int insertTermsList(Terms terms);

    int checkByMemId(String memId);
    
    int updateCheckEmailToken(Account account);
    
    String selectReqTnc(Terms terms);

    List<Account> findAccount(FindAccount findAccount);

    int updatePasswdReset(Account account);

    List<Resources> findAllResources();

    List<Role> findAllRoleHierarchy();

    void updateLastLgnDt(Account account);
    
    int updateMemFileInfo(Account account);
}
