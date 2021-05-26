package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;
import java.util.List;

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
import kr.co.aicc.modules.settings.dto.RoleForm;
import kr.co.aicc.modules.settings.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class AuthorityResController {
    private final ReloadableResourceBundleMessageSource messageSource;
	private final AuthorityService authorityService;

    /**
     * 권한 상세
     * @return
     */
    @GetMapping("roleDtl")
    @ResponseBody
    public Response roleDtl(RoleForm roleForm, Model model) {
    	log.debug("권한 상세  roleForm ::: " + roleForm);
    	List<RoleForm> roleList = authorityService.findRoleList(roleForm);	
    	log.debug("roleList.get(0) ::: " + roleList.get(0));

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
    	map.put("roleDtl", roleList.get(0));

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * 권한 등록
     * @return
     */
    @PostMapping("createRole")
    @ResponseBody
    public Response createRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("권한 등록  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();

    	if (roleForm.getPrntRoleNo() == null) {
            errors.rejectValue("prntRoleNo", "");
    		msg += "<br/>상위권한을 선택해주세요.";
		}
    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	} 
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (authorityService.chkRoleNm(roleForm) > 0) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명이 중복 되었습니다.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("권한 등록 : 에러발생 :: " + errors);
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
    		roleForm.setCretr(account.getMemNo());
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.createRole(roleForm) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 등록실패");
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
     * 권한 수정
     * @return
     */
    @PostMapping("updateRole")
    @ResponseBody
    public Response updateRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("권한 수정  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();

//    	if (roleForm.getPrntRoleNo() == null) {
//            errors.rejectValue("prntRoleNo", "");
//    		msg += "<br/>상위권한을 선택해주세요.";
//		}
    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	}
    	if (authorityService.chkRoleNm(roleForm) > 0) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명이 중복 되었습니다.";
    	}
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("권한 수정 : 에러발생 :: " + errors);
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
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.updateRole(roleForm) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정실패");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 수정실패");
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
     * 최상위 권한 등록
     * @return
     */
    @PostMapping("createTopRole")
    @ResponseBody
    public Response createTopRole(RoleForm roleForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("최상위 권한 등록  roleForm ::: " + roleForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();

    	if ("".equals(roleForm.getRoleNm()) || roleForm.getRoleNm() == null) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 입력해주세요.";
    	} 
//		else if ("".equals(roleForm.getUseYn()) || roleForm.getUseYn() == null) {
//        	map.put("msg", "사용여부를 체크해주세요.");
//            return map;    		
//    	}

    	if (roleForm.getRoleNm().length() > 20) {
            errors.rejectValue("roleNm", "");
    		msg += "<br/>권한명을 20자 내로 입력해주세요.";
    	}
    	if (roleForm.getRoleDesc().length() > 200) {
            errors.rejectValue("roleDesc", "");
    		msg += "<br/>권한 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("최상위 권한 등록 : 에러발생 :: " + errors);
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
    		roleForm.setCretr(account.getMemNo());
    		roleForm.setChgr(account.getMemNo());
	    	if (authorityService.createTopRole(roleForm) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록실패");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "최상위 등록실패");  
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
     * 권한 삭제
     * @return
     */
    @PostMapping("/deleteRole")
    @ResponseBody
    public Response deleteRole(RoleForm roleForm) {
    	log.debug("권한 삭제 roleForm ::::: " + roleForm);

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
    	if (roleForm.getRoleNo() == null) {
        	map.put("msg", "삭제할 권한을 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}
    	
    	if (authorityService.chkDeleteRoleYn(roleForm) > 0) {
        	map.put("msg", "해당 권한은 시스템 지정 권한으로 삭제할 수 없습니다. <br/>해당 권한의 삭제가 필요한 경우 관리자에게 문의해 주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}
    	
    	if (authorityService.chkChildRoleYn(roleForm) > 0) {
        	map.put("msg", "하위 권한이 있습니다. 먼저 하위 권한을 정리 후 다시 시도 하세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}
    	
    	try {
	    	if (authorityService.deleteRole(roleForm) > 0) {
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "권한 삭제실패");
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
