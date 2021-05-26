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
 * @package kr.co.aicc.modules.account
 * @file UserAccount.java
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "username")
public class UserAccount extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private Account account;

    public UserAccount(Account account, List<GrantedAuthority> roles) {
        super(account.getMemId(), account.getPasswd(), roles);
//        super(account.getMemId(), account.getPasswd(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        this.account = account;
        this.username = account.getMemId();
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserAccount that = (UserAccount) o;
        return Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), account);
    }*/
}
