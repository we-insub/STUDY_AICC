package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.enums.ResponseEnum.ErrorCode;
import kr.co.aicc.infra.enums.ResponseEnum.Status;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.settings.dto.ResourceDto;
import kr.co.aicc.modules.settings.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class ResourceRestController {
    private final ReloadableResourceBundleMessageSource messageSource;
	private final ResourceService resourceService;

    /**
     * 메뉴 순서 UP
     * @return
     */
    @PostMapping("resOrdUp")
    @ResponseBody
    public Response resOrdUp(ResourceDto resourceDto, @CurrentAccount Account account) {
    	log.debug("메뉴 순서 UP  resourceDto ::: " + resourceDto);
    	
    	LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
    	String status = Status.SUCCESS.value();

    	if (account != null) {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
    	}
    	try {
	    	if (resourceService.resOrdUp(resourceDto) > 0) {
	        	map.put("msg", "순서 변경 완료");
	    	} else {
	        	map.put("msg", "상위메뉴로 변경이 불가합니다.");
	        	status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "순서 변경 ERROR");
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
     * 메뉴 순서 DOWN
     * @return
     */
    @PostMapping("resOrdDown")
    @ResponseBody
    public Response resOrdDown(ResourceDto resourceDto, @CurrentAccount Account account) {
    	log.debug("# 메뉴 순서 DOWN  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();

    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.resOrdDown(resourceDto) > 0) {
	        	map.put("msg", "순서 변경 완료");
	    	} else {
	        	map.put("msg", "하위메뉴로 변경이 불가합니다.");
	        	status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "순서 변경 ERROR");
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
     * 메뉴 등록
     * @return
     */
    @PostMapping("createRes")
    @ResponseBody
    public Response createRes(ResourceDto resourceDto, Errors errors, @CurrentAccount Account account) {
    	log.debug("메뉴 등록  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(resourceDto.getResNm()) || resourceDto.getResNm() == null) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getResUrl()) || resourceDto.getResUrl() == null) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getRole()) || resourceDto.getRole() == null) {
            errors.rejectValue("role", "");
    		msg += "<br/>권한을 선택해주세요.";
    	}
    	if ("".equals(resourceDto.getOrd()) || resourceDto.getOrd() == 0) {
            errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getDispYn()) || resourceDto.getDispYn() == null) {
            errors.rejectValue("ord", "");
    		msg += "<br/>노출여부를 선택해주세요.";
    	}

    	if (resourceDto.getResNm().length() > 20) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 20자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResUrl().length() > 100) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 100자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResMeth().length() > 10) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>메소드 타입을 10자 내로 입력해주세요.";
    	}
    	if (resourceDto.getOrd() > 99) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>정렬번호를 100이하로 입력해주세요.";
    	}

        if (errors.hasErrors()) {
    		log.debug("메뉴 등록 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
        	status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
        }

    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());
	    	if (resourceService.createRes(resourceDto) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	        	status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "메뉴 등록실패");
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
     * 메뉴 수정
     * @return
     */
    @PostMapping("updateRes")
    @ResponseBody
    public Response updateRes(ResourceDto resourceDto, Errors errors, @CurrentAccount Account account) {
    	log.debug("메뉴 수정  resourceDto ::: " + resourceDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(resourceDto.getResNm()) || resourceDto.getResNm() == null) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getResUrl()) || resourceDto.getResUrl() == null) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getRole()) || resourceDto.getRole() == null) {
            errors.rejectValue("role", "");
    		msg += "<br/>권한을 선택해주세요.";
    	}
    	if ("".equals(resourceDto.getOrd()) || resourceDto.getOrd() == 0) {
            errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 입력해주세요.";
    	}
    	if ("".equals(resourceDto.getDispYn()) || resourceDto.getDispYn() == null) {
            errors.rejectValue("ord", "");
    		msg += "<br/>노출여부를 선택해주세요.";
    	}

    	if (resourceDto.getResNm().length() > 20) {
            errors.rejectValue("resNm", "");
    		msg += "<br/>메뉴명을 20자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResUrl().length() > 100) {
            errors.rejectValue("resUrl", "");
    		msg += "<br/>메뉴 URL을 100자 내로 입력해주세요.";
    	}
    	if (resourceDto.getResMeth().length() > 10) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>메소드 타입을 10자 내로 입력해주세요.";
    	}
    	if (resourceDto.getOrd() > 99) {
            errors.rejectValue("resMeth", "");
    		msg += "<br/>정렬번호를 100이하로 입력해주세요.";
    	}

        if (errors.hasErrors()) {
    		log.debug("메뉴 수정 : 에러발생 :: " + errors);
    		map.put("msg", msg.replaceFirst("<br/>", ""));
        	status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
        }

    	try {
    		resourceDto.setCretr(account.getMemNo());
    		resourceDto.setChgr(account.getMemNo());		
	    	if (resourceService.updateRes(resourceDto) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정실패");
	        	status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "메뉴 수정실패");
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
     * 메뉴 삭제
     * @return
     */
    @PostMapping("/deleteRes")
    @ResponseBody
    public Response deleteRes(ResourceDto resourceDto) {
    	log.debug("# 메뉴 삭제  resourceDto ::: " + resourceDto);

		HashMap<String, Object> map = new HashMap<String, Object>();
    	String status = Status.SUCCESS.value();
		
		if (resourceService.deleteRes(resourceDto) > 0 ) {
			map.put("msg", "삭제되었습니다.");
		} else {
			map.put("msg", "삭제할 정보가 없습니다.");
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
