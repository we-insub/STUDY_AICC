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
 * @package kr.co.aicc.modules.account.validator
 * @file SignUpFormValidator.java
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
package kr.co.aicc.modules.account.validator;

import kr.co.aicc.modules.account.service.AccountService;
import kr.co.aicc.modules.account.dto.FindAccount;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FindAccountValidator implements Validator {

    private final AccountService accountService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(FindAccount.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
    	FindAccount findAccount = (FindAccount) object;
        if (accountService.findAccount(findAccount).size() < 1) {
            errors.rejectValue("msg", "FindAccount.msg.Error");
        }
    }
}
