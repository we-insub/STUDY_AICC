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
 * @package kr.co.aicc.modules.main.controller
 * @file MainController.java
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
package kr.co.aicc.modules.main.controller;

import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.account.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    /**
     * Main
     * @param account
     * @param model
     * @return
     */
    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if(account != null) {
            log.debug("acccount {}:", account);
            return "redirect:dashboard/work_status";
        }
        return "redirect:account/sign_in";
    }

    /**
     * 접근권한없음 페이지
     * @param account
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value="/denied")
    public String denied(@CurrentAccount Account account, Model model) throws Exception {
        if(account != null) {
            model.addAttribute("username", account.getMemNm());
        }

        return "error/denied";
    }

    /**
     * 세션만료 페이지
     * @return
     * @throws Exception
     */
    @GetMapping(value="/expired")
    public String expired() throws Exception {
        return "error/expired";
    }
}
