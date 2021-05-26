package kr.co.aicc.modules.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.AccountService;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.common.service.CodeCacheService;
import kr.co.aicc.modules.mypage.dto.ProfileForm;
import kr.co.aicc.modules.mypage.service.MypageService;
import kr.co.aicc.modules.mypage.validator.ProfileFormValidator;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "mypage")
public class ProfileController {
    private final ProfileFormValidator profileFormValidator;
    private final MypageService mypageService;
    private final CodeCacheService codeCacheService;
    private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;
	
    /**
     * 내정보 화면
     * @return
     */
    @GetMapping("/profile")
	@PreAuthorize ("isAuthenticated()")
    public String profileView(ProfileForm profileForm, Model model, @CurrentAccount Account account) {
		log.debug("# 내정보 화면 CurrentAccount :: " + account);
    	
		ProfileForm profile =  mypageService.findProfileByMemNo(account.getMemNo());
		log.debug("profile ::::  " + profile);	
    	model.addAttribute("profile", profile);
        model.addAttribute("yearList", codeCacheService.getYearList());		//년도
        model.addAttribute("monthList", codeCacheService.getMonthList());	//월
        model.addAttribute("dayList", codeCacheService.getDayList());		//일
        model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));		//성별
        model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
        model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
        model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종

        return "mypage/profile";
    }


	//회원 탈퇴
	@PostMapping("/profileDelete")
	@PreAuthorize ("isAuthenticated()")
    @ResponseBody
    public HashMap<String,Object> profileDelete(@CurrentAccount Account cAccount, Account pAccount) {
    	log.debug("# 회원 탈퇴 account ::::: " + pAccount);

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "회원 탈퇴");

		Account account = accountService.findByMemId(cAccount.getMemId());

    	boolean matchYn = passwordEncoder.matches(pAccount.getPasswd(), account.getPasswd());
    	log.debug("passwordEncoder.matches ::::: " + matchYn);

    	if (matchYn) {
    		pAccount.setMemNo(cAccount.getMemNo());
    		if (mypageService.profileDelete(pAccount) > 0 ) {
	        	map.put("result", "success");
				map.put("msg", "탈퇴되었습니다.");
			} else {
				map.put("msg", "탈퇴할 정보가 없습니다.");
			}
    	} else {
			map.put("msg", "비밀번호가 일치하지 않습니다.");
    	}

        return map;
	}
	
	//비밀번호 변경
	@PostMapping("/profilePassChg")
	@PreAuthorize ("isAuthenticated()")
    @ResponseBody
    public HashMap<String,Object> profilePassChg(@CurrentAccount Account cAccount, Account pAccount) {
    	log.debug("# 비밀변호 변경 account ::::: " + pAccount);

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	map.put("title", "비밀변호 변경");

		Account account = accountService.findByMemId(cAccount.getMemId());

    	boolean matchYn = passwordEncoder.matches(pAccount.getPasswd(), account.getPasswd());
    	log.debug("passwordEncoder.matches ::::: " + matchYn);

    	if (matchYn) {
        	map.put("result", "success");
			map.put("msg", "비밀번호 일치");
    	} else {
			map.put("msg", "비밀번호가 일치하지 않습니다.");
    	}

        return map;
	}
	
	// 수정 폼
	@PostMapping("/profileUpdateForm")
	@PreAuthorize ("isAuthenticated()")
	public String profileUpdateForm(@CurrentAccount Account account, ProfileForm profileForm, Model model) {
		log.debug("# 내정보 수정 폼 CurrentAccount :: " + account);
		log.debug("# 개인정보수정 폼 profileForm :: " + profileForm);

		profileForm.setMemNo(account.getMemNo());

		ProfileForm profile =  mypageService.findProfileByMemNo(account.getMemNo());
		profile.setPassChgYn(profileForm.getPassChgYn());
		log.debug("profile ::"  + profile);
    	
    	model.addAttribute("profileForm", profile);
        model.addAttribute("yearList", codeCacheService.getYearList());		//년도
        model.addAttribute("monthList", codeCacheService.getMonthList());	//월
        model.addAttribute("dayList", codeCacheService.getDayList());		//일
        model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));		//성별
        model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
        model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
        model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종

		return "mypage/profile_edit";
	}

	
    /**
     * 내정보 수정
     * @return
     */
    @PostMapping("/profileUpdate")
	@PreAuthorize ("isAuthenticated()")
    public String profileUpdate(@Valid ProfileForm profileForm, Errors errors, Model model, @CurrentAccount Account cAccount, RedirectAttributes redirectAttributes) {
		log.debug("# 내정보 수정 CurrentAccount :: " + cAccount);
		log.debug("# 내정보 수정 profileForm :: " + profileForm);
		profileForm.setMemId(cAccount.getMemId());
		profileForm.setMemIdF(cAccount.getMemId().split("@")[0]);
		profileForm.setMemIdL(cAccount.getMemId().split("@")[1]);

        profileForm.setMemNo(cAccount.getMemNo());
		Account account = accountService.findByMemId(cAccount.getMemId());		
		boolean pwChkYn = passwordEncoder.matches(profileForm.getPasswd(), account.getPasswd());
		
		// 비밀번호 변경 유무
		if ("Y".equals(profileForm.getPassChgYn())) {
            log.debug("# 내정보 수정 : 비밀번호 변경");
	        if (profileForm.getPasswd().equals(profileForm.getPasswd2()) == false) {
	            errors.rejectValue("passwd", "ProfileForm.passwd.NotEquals");
	            errors.rejectValue("passwd2", "ProfileForm.passwd.NotEquals");
	        }

			String passwdPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
			String passwd = profileForm.getPasswd();
	        if (!java.util.regex.Pattern.matches(passwdPattern,passwd)) {
	            errors.rejectValue("passwd", "ProfileForm.passwd.Pattern");
	        }
	        
		} else {
            log.debug("# 내정보 수정 : 비밀번호 확인");
			if (!pwChkYn) {
	            log.debug("# 내정보 : 비밀번호 MISS MATCH");
	            errors.rejectValue("passwd", "ProfileForm.passwd.NotEquals");
			}
		}
		
        if (errors.hasErrors()) {
    		log.debug("# 내정보 수정 : 에러발생 :: " + errors);

            model.addAttribute("yearList", codeCacheService.getYearList());		//년도
            model.addAttribute("monthList", codeCacheService.getMonthList());	//월
            model.addAttribute("dayList", codeCacheService.getDayList());		//일
            model.addAttribute("cmnSex", codeCacheService.getCode(CommonEnum.GroupCode.SEX.name()));		//성별
            model.addAttribute("cmnTelNo", codeCacheService.getCode(CommonEnum.GroupCode.TEL_NO.name()));	//전화번호그룹
            model.addAttribute("cmnEdu", codeCacheService.getCode(CommonEnum.GroupCode.EDU.name()));		//최종학력
            model.addAttribute("cmnModel", codeCacheService.getCode(CommonEnum.GroupCode.MODEL.name()));	//사용기종

            return "mypage/profile_edit";
        }

        mypageService.updateProfile(profileForm);
    	redirectAttributes.addFlashAttribute("cfrmMessage", "수정되었습니다.");
		return "redirect:/mypage/profile";
		
    }

    /**
     * 회원정보 삭제
     * @return
     */
//    @PostMapping("/memberDelete")
//    public String memberDelete(MemberForm memberForm, Model model) {
//    	
//		List<Account> memberList = new ArrayList<Account>();
//		memberList = memberService.selectMemList(memberForm);
//        model.addAttribute("cmnMemStat", codeCacheService.getCode(CommonEnum.GroupCode.MEM_STAT.name()));	//회원상태
//    	model.addAttribute("memberList", memberList);
//        return "settings/member";
//    }


    /**
     * memberForm validation
     * @param webDataBinder
     */
    @InitBinder("profileForm")
    public void initBinderMemberForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(profileFormValidator);
    } 
}
