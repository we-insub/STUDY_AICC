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
 * @file AccountDaoImpl.java
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
import kr.co.aicc.modules.account.dto.FindAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountDaoImpl implements AccountDao {
    private final AccountMapper accountMapper;

    @Override
    public int insertMember(Account account) {
        return accountMapper.insertMember(account);
    }

    @Override
    public Account findByMemId(String memId) {
        return accountMapper.findByMemId(memId);
    }

    @Override
    public Account findByEmail(Account account) {
        return accountMapper.findByEmail(account);
    }

	@Async
    @Override
    public void insertLoginDetail(LoginDetail loginDetail) {
        accountMapper.insertLoginDetail(loginDetail);
    }

	@Override
	public List<Terms> selectTermsList(Terms terms) {
		return accountMapper.selectTermsList(terms);
	}

	@Override
	public int insertTermsList(Terms terms) {
		return accountMapper.insertTermsList(terms);
	}

	@Override
	public int checkByMemId(String memId) {
		return accountMapper.checkByMemId(memId);
	}

	@Override
	public int updateCheckEmailToken(Account account) {
		return accountMapper.updateCheckEmailToken(account);
	}

	@Override
	public String selectReqTnc(Terms terms) {
		return accountMapper.selectReqTnc(terms);
	}

	@Override
	public List<Account> findAccount(FindAccount findAccount) {
		return accountMapper.findAccount(findAccount);
	}

	@Override
	public int updatePasswdReset(Account account) {
		return accountMapper.updatePasswdReset(account);
	}

	@Override
	public int updateEmailTokenIssueDt(Account account) {
		return accountMapper.updateEmailTokenIssueDt(account);
	}

	@Override
	public List<Resources> findAllResources() {
		return accountMapper.findAllResources();
	}
	@Override
	public List<Role> findAllRoleHierarchy() {
		return accountMapper.findAllRoleHierarchy();
	}

	@Override
	public int insertMemRole(Account account) {
		return accountMapper.insertMemRole(account);
	}

	@Override
	public void updateLastLgnDt(Account account) {
		accountMapper.updateLastLgnDt(account);
	}

	@Override
	public int updateMemFileInfo(Account account) {
		return accountMapper.updateMemFileInfo(account);
	}
}
