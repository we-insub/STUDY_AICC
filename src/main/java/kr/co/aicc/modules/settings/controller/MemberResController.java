package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.enums.ResponseEnum.ErrorCode;
import kr.co.aicc.infra.enums.ResponseEnum.Status;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.settings.dto.MemberForm;
import kr.co.aicc.modules.settings.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class MemberResController {
    private final ReloadableResourceBundleMessageSource messageSource;
    private final MemberService memberService;

    /**
     * 회원정보 상세
     * @return
     */
    @GetMapping("/memberDtl")
    @ResponseBody
    public Response memberDtl(MemberForm memberForm, Model model) {

		HashMap<String, Object> map = new HashMap<String, Object>();
    	String status = Status.SUCCESS.value();
		
		List<MemberForm> memberList = memberService.selectMemList(memberForm);
		log.debug("memberList.get(0) " + memberList.get(0));
		
		map.put("member", memberList.get(0));

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * 회원정보 수정
     * @return
     */
    @PostMapping("/memberUpdate")
    @ResponseBody
    public Response memberUpdate(@Valid MemberForm memberForm, Errors errors, Model model, @CurrentAccount Account account) {
		log.debug("memberForm :::: " + memberForm);

		HashMap<String, Object> map = new HashMap<String, Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
        if (errors.hasErrors()) {        	
    		log.debug("회원정보 수정 : 에러발생 :: " + errors);
    		if (errors.hasFieldErrors("memId")) {
        		msg += "<br/>회원을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("memNm")) {
        		msg += "<br/>성명은 한글,영문을 이용한 2자~20자로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("brthday")) {
        		msg += "<br/>생년월일을 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("sex")) {
        		msg += "<br/>성별을 체크해주세요.";
    		}
    		if (errors.hasFieldErrors("role")) {
        		msg += "<br/>권한을 체크해주세요.";
    		}
    		if (errors.hasFieldErrors("ptblTelNo")) {
        		msg += "<br/>휴대폰번호 11자리를 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("zipNo")) {
        		msg += "<br/>우편번호를 6자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("baseAddr")) {
        		msg += "<br/>주소를 100자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("dtlAddr")) {
        		msg += "<br/>나머지주소를 100자 내로 입력해주세요.";
    		}
    		if (errors.hasFieldErrors("lastEdu")) {
        		msg += "<br/>최종학력을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("useModel")) {
        		msg += "<br/>사용기종을 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("stat")) {
        		msg += "<br/>승인여부를 선택해주세요.";
    		}
    		if (errors.hasFieldErrors("careerDesc")) {
        		msg += "<br/>경력사항을 500자내로 입력해주세요.";
    		}
    		map.put("msg", msg.replaceFirst("<br/>", ""));
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
        }
    	//수정
        try {
            memberForm.setCretr(account.getMemNo());
            memberForm.setChgr(account.getMemNo());
    		if (memberService.updateMember(memberForm) > 0 ) {
    			map.put("msg", "수정되었습니다.");
    		} else {
    			map.put("msg", "수정할 정보가 없습니다.");
        		status = Status.ERROR.value();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "회원정보 수정실패");
    		status = Status.ERROR.value();    		
    	}

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * 회원정보 삭제
     * @return
     */
    @PostMapping("/memberDelete")
    @ResponseBody
    public Response memberDelete(MemberForm memberForm, Model model, @CurrentAccount Account account) {

		HashMap<String, Object> map = new HashMap<String, Object>();
    	String status = Status.SUCCESS.value();

        try {
	        memberForm.setChgr(account.getMemNo());
			if (memberService.deleteMember(memberForm) > 0 ) {
				map.put("msg", "탈퇴되었습니다.");
			} else {
				map.put("msg", "탈퇴할 정보가 없습니다.");
	    		status = Status.ERROR.value();
			}
		} catch (Exception e) {
			e.printStackTrace();
	    	map.put("msg", "회원정보 탈퇴실패");
			status = Status.ERROR.value();    		
		}

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }
}
