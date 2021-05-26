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
 * @package kr.co.aicc.modules.account.controller
 * @file AccountController.java
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
package kr.co.aicc.modules.account.controller;

import kr.co.aicc.infra.constants.GlobalConstants;
import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.dto.FindAccount;
import kr.co.aicc.modules.account.dto.SignUpForm;
import kr.co.aicc.modules.account.service.AccountService;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.account.validator.FindAccountValidator;
import kr.co.aicc.modules.account.validator.SignUpFormValidator;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.account.domain.Terms;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "account")
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final FindAccountValidator findAccountValidator;
    private final AccountService accountService;
//    private final SessionRegistry sessionRegistry;

//    private final AppProperties appProperties;
//    private final ReloadableResourceBundleMessageSource messageSource;
    private final CodeCacheService codeCacheService;
    private final StringRedisTemplate redisTemplate;

    /**
     * 로그인 폼 UI
     * @return
     */
    @RequestMapping("/sign_in")
    public String signIn(Locale locale, @CurrentAccount Account account) {
        //=============================================================
        // sample
//        log.debug("====> locale : {}", locale);
//        log.debug("=====>"+appProperties.getUploadDirProfile());
//        log.debug("=====>"+appProperties.getHost());
//
//        String message = messageSource.getMessage("TEST.MESSAGE", new Object[]{"홍길동", 12}, locale);
//        log.debug("====> message : {}", message);
//
//        System.out.println(codeCacheService.getCode(CommonEnum.formFilter.name()));
        //=============================================================
    	log.debug("account :: " + account);
    	if (account == null) {
            return "account/sign_in";
    	} else {
            return "redirect:/";
    	}
    }

    /**
     * Get방식의 로그아웃 제공
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/account/sign_in";
    }

    /**
     * 약관동의 화면
     * @param model
     * @return
     */
    @GetMapping("/agree_tnc")
    public String agreeTncView(Model model) {
    	//DB에서 약관을 불러온다.
    	List<Terms> result = accountService.selectTermsList();
        model.addAttribute("terms",result);
        return "account/agree_tnc";
    }

    /**
     * 회원가입 폼
     * @param model
     * @return
     */
    @PostMapping("/sign_up_form")
    public String signUpForm(SignUpForm signUpForm, HttpSession session, Model model) {
    	String terms = "";
    	String reqTerms = signUpForm.getReqTerms();
    	String optTerms = signUpForm.getOptTerms();

    	if (!("".equals(optTerms) || optTerms == null)) {
    		terms = reqTerms + "," + optTerms;
    	} else {
    		terms = reqTerms;
    	}

    	signUpForm.setTerms(terms);
    	session.setAttribute("terms",terms);
        model.addAttribute("cmnEmail", codeCacheService.getCode(CommonEnum.GroupCode.EMAIL_ADDR.name()));	//이메일
        model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));	//성별
        model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
        model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
        model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종
    	if (accountService.checkReqTerms(signUpForm)) {
        	log.debug("SUCCES] 필수 약관 체크");
            return "account/sign_up";
    	} else {
        	log.debug("FAIL] 필수 약관 체크");
        	//DB에서 약관을 불러온다.
        	List<Terms> result = accountService.selectTermsList();

            model.addAttribute("terms",result);
            model.addAttribute("selTerms",terms);
            model.addAttribute("errorMsg","필수 동의 약관을 확인해 주세요.");
            return "account/agree_tnc";

    	}
    }

    /**
     * 회원가입처리
     * @param signUpForm
     * @param errors
     * @return
     */
    @PostMapping("/sign_up")
    public String signUpProcess(@Valid SignUpForm signUpForm, Errors errors, HttpSession session, Model model) {
        signUpForm.setTerms((String) session.getAttribute("terms"));
        signUpForm.setMemId(signUpForm.getMemIdF() + "@" + signUpForm.getMemIdL());
        log.debug("signUpForm: {}",signUpForm);
        if (errors.hasErrors()) {
            log.debug("회원가입 : 에러발생");
            log.debug("errors: {}",errors);
            model.addAttribute("cmnEmail", codeCacheService.getCode(CommonEnum.GroupCode.EMAIL_ADDR.name()));	//이메일
            model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));	//성별
            model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
            model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
            model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종
            return "account/sign_up";
        }

        accountService.signUp(signUpForm);
        model.addAttribute("memberInfo", signUpForm);
        return "account/signup_complete";
    }

    /**
     * signUpForm validation
     * @param webDataBinder
     */
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @RequestMapping(value = "/active_user")
    public String admin(Map<String, Object> model, HttpServletRequest request) {
        Set<String> sessionKeys = redisTemplate.keys(
                GlobalConstants.SESSION_KEY_PATTERN+"*");
        List<String> memberList = new ArrayList<>();
        Iterator<String> it = sessionKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            memberList.add(data.replace(GlobalConstants.SESSION_KEY_PATTERN,""));
        }

        model.put("activeuserCnt",  memberList.size());
        model.put("activeUserList",  memberList);

        /*if (sessionRegistry.getAllPrincipals().size() != 0) {
            log.info("ACTIVE USER: " + sessionRegistry.getAllPrincipals().size());

            List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
            List<String> activeUserList = new ArrayList<String>();
            for (Object principal: allPrincipals) {
                if (principal instanceof User) {
                    log.debug("user : {}", ((UserAccount) principal).getUsername());
                    activeUserList.add(((UserAccount) principal).getUsername());
                }
            }

            model.put("activeuserCnt",  sessionRegistry.getAllPrincipals().size());
            model.put("activeUserList",  sessionRegistry.getAllPrincipals());
        }
        else
            log.warn("EMPTY" );*/

        return "account/active_user";
    }

    /**
     * 에미일인증 확인 페이지
     * @param token
     * @param memId
     * @param model
     * @return
     */
    @GetMapping("/check_email_token")
    public String checkEmailToken(@RequestParam String token, @RequestParam String memId, Model model) {

        Account account = accountService.findByMemId(memId);
        String view = "account/checked_email";
        if (account == null) {
            model.addAttribute("state", "error");
            model.addAttribute("msg", "요청하신 아이디가 확인되지 않습니다. 관리자에게 문의해주세요.");
            return view;
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("state", "error");
            model.addAttribute("msg", "이메일 인증토큰이 일치하지 않습니다. 관리자에게 문의해주세요.");
            return view;
        }

        accountService.completeSignUp(account);
        model.addAttribute("state", "success");
        model.addAttribute("memNm", account.getMemNm());
        return view;
    }

    /**
     * 회원가입완료
     * @param
     * @param
     * @return
     */
    @GetMapping("/check_email")
    public String checkEmailForm(@RequestParam String memId,
                                 @RequestParam String emailExpYn, Model model) {
        log.debug("[checkEmailForm] memId : {}, emailExpYn : {} ", memId, emailExpYn);
//        String path = "account/check_email?memId="+memId+"&email="+email+"&emailExpYn="+emailExpYn;

        model.addAttribute("memId",memId);
        model.addAttribute("emailExpYn",emailExpYn);
    	return "account/check_email";
    }

    /**
     * 이메일 인증메일 재전송요청페이지
     * @param account
     * @param model
     * @return
     */
    @PostMapping("/resend_confirm_email")
    @ResponseBody
    public HashMap<String, Object> resendConfirmEmail(@RequestParam String memId) {
    	log.debug("memId ::: " + memId);

    	Account account = accountService.findByMemId(memId);

        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        if (!account.canSendConfirmEmail()) {
        	hashMap.put("result", "error");
            return hashMap;
        }
        try {
            accountService.resendConfirmEmail(account);
            hashMap.put("result", "success");
        } catch (Exception e) {
        	e.printStackTrace();
            hashMap.put("result", "error");
        }
        return hashMap;
    }

    /**
     * 아이디/비밀번호찾기 폼 UI
     * @return
     */
    @GetMapping("/find_account")
    public String findAccountForm(FindAccount findAccount, Model model) {
    	log.debug("# 아이디/비번찾기 findAccount : " + findAccount);
    	
    	if ("".equals(findAccount.getPageType()) || findAccount.getPageType() == null) {
    		findAccount.setPageType("id");
    	}    	
        model.addAttribute("pageType", findAccount.getPageType());
    	
        return "account/find_account";
    }

    /**
     * 아이디찾기
     * @return
     */
    @PostMapping("/find_account_id")
    public String findAccountId(@Valid FindAccount findAccount, Errors errors, Model model) {
        log.debug("# id 찾기 findAccount : " + findAccount);
        findAccount.setPageType("id");
        
        if (errors.hasErrors()) {
        	model.addAttribute("pageType", "id");
            return "account/find_account";
        }

        List<Account> result = new ArrayList<Account>();

        result = accountService.findAccount(findAccount);
        
        // 마스킹 처리
        for (int i=0; i<result.size(); i++) {
        	String[] splitMemId = result.get(i).getMemId().split("@");
        	String memIdF = "";
        	if (splitMemId[0].length() > 2) {
            	memIdF = splitMemId[0].replaceAll("(?<=.{2})." , "*");
        	} else {
            	memIdF = splitMemId[0].replaceAll("(?<=.{1})." , "*");	
        	}
        	result.get(i).setMemId(memIdF + "@" +  splitMemId[1]);
        }
        
        log.debug("# id 찾기 accountService.findAccount : " + result);

        model.addAttribute("result", result);
    	model.addAttribute("pageType", "id");
        model.addAttribute("memNm", result.get(0).getMemNm());

        return "account/find_account_complete";
    }


    /**
     * 비밀번호찾기
     * @return
     */
    @PostMapping("/find_account_pw")
    public String findAccountPw(@Valid FindAccount findAccount, Errors errors, Model model) {
        log.debug("# pw 찾기 findAccount : " + findAccount);
        findAccount.setPageType("pw");
        
        String pattern = "^[a-zA-Z0-9]{4,20}+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        if (!Pattern.matches(pattern,findAccount.getMemId())) {
            errors.rejectValue("memId", "FindAccount.memId.Pattern");
        }
        
        if (errors.hasErrors()) {
            model.addAttribute("pageType", "pw");
            return "account/find_account";
        }
        List<Account> result = new ArrayList<Account>();

        result = accountService.findAccount(findAccount);
        
        if (result.size() > 0) {
            log.debug("# pw 찾기  accountService.findAccount : " + result);

            accountService.updatePasswdReset(result.get(0));

            model.addAttribute("pageType", "pw");
            model.addAttribute("result", result.get(0));

            return "account/find_account_complete";
        } else {
            log.debug("# pw 찾기 계정정보 없음 ");
            errors.rejectValue("msg", "FindAccount.msg.Error");
            model.addAttribute("pageType", "pw");
            return "account/find_account";
        }
    }

    /**
     * findAccount validation
     * @param webDataBinder
     */
    @InitBinder("findAccount")
    public void initBinderFindAccount(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(findAccountValidator);
    }

}
