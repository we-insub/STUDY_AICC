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
 * @package kr.co.aicc.modules.account.service
 * @file AccountService.java
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
package kr.co.aicc.modules.account.service;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.LoginDetail;
import kr.co.aicc.modules.account.domain.Terms;
import kr.co.aicc.modules.account.dto.FindAccount;
import kr.co.aicc.modules.account.dto.SignUpForm;

import java.util.List;

public interface AccountService {
    Account signUp(SignUpForm signUpForm);

    Account findByEmail(Account account);

    Account findByMemId(String memId);
    
    void completeSignUp(Account account);

    void resendConfirmEmail(Account account);
    
    void sendSignUpConfirmEmail(Account account);

    void insertLoginDetail(LoginDetail loginDetail);

    List<Terms> selectTermsList();

    int checkByMemId(String memId);

    boolean checkReqTerms(SignUpForm signUpForm);
    
    List<Account> findAccount(FindAccount findAccount);

    int updatePasswdReset(Account account);

    List<String> getActiveMembers();

}
