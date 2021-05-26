package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
import kr.co.aicc.modules.settings.dto.DtlCode;
import kr.co.aicc.modules.settings.dto.GrpCode;
import kr.co.aicc.modules.settings.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class CodeResController {
    private final ReloadableResourceBundleMessageSource messageSource;
	private final CodeService codeService;
    
    /**
     * 그룹코드 상세
     * @return
     */
    @GetMapping("/grpCodeDtl")
    @ResponseBody
    public Response grpCodeDtl(GrpCode grpCode) {
    	log.debug("# 그룹코드 상세 code : " + grpCode);
    	String status = Status.SUCCESS.value();
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	GrpCode grpCodeDtl = codeService.findCodeGrpList(grpCode).get(0);

    	map.put("grpCodeDtl", grpCodeDtl);
        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }
    
    /**
     * 코드 상세
     * @return
     */
    @GetMapping("/codeDtl")
    @ResponseBody
    public Response codeDtl(DtlCode dtlcode) {
    	log.debug("# 코드 상세 code : " + dtlcode);
    	String status = Status.SUCCESS.value();
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	DtlCode codeDtl = codeService.findCodeDtlList(dtlcode).get(0);

    	map.put("codeDtl", codeDtl);
        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }
    
    /**
     * 그룹코드 등록
     * @return
     */
    @PostMapping("/createCodeGrp")
    @ResponseBody
    public Response createCodeGrp(GrpCode grpCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("# 그룹코드 등록 grpCode : " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
            errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>그룹코드를 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpGrpCdNm()) || grpCode.getGrpGrpCdNm() == null) {
            errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpUseYn()) || grpCode.getGrpUseYn() == null) {
            errors.rejectValue("grpUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (grpCode.getGrpGrpCd().length() > 20) {
        	errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>그룹코드를 20자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpGrpCdNm().length() > 50) {
        	errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 50자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpDesc().length() > 200) {
        	errors.rejectValue("grpGrpDesc", "");
    		msg += "<br/>그룹코드 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("그룹코드 등록 에러발생 : " + errors);
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
    		grpCode.setCretr(account.getMemNo());
    		grpCode.setChgr(account.getMemNo());
	    	if (codeService.createCodeGrp(grpCode) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}

    	} catch (DuplicateKeyException ex1) {
    		ex1.printStackTrace();
        	map.put("msg", "그룹코드 중복");
    		status = Status.ERROR.value();
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 등록실패");
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
     * 상세코드 등록
     * @return
     */
    @PostMapping("/createCodeDtl")
    @ResponseBody
    public Response createCodeDtl(DtlCode dtlCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("# 상세코드 등록 dtlCode : " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>그룹코드를 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getCd()) || dtlCode.getCd() == null) {
        	errors.rejectValue("cd", "");
    		msg += "<br/>상세코드를 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getCdNm()) || dtlCode.getCdNm() == null) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getDtlUseYn()) || dtlCode.getDtlUseYn() == null) {
        	errors.rejectValue("dtlUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (dtlCode.getDtlGrpCd().length() > 20) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>그룹코드를 20자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCd().length() > 10) {
        	errors.rejectValue("cd", "");
    		msg += "<br/>상세코드를 10자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdNm().length() > 200) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 200자 내로 입력해주세요.";
    	}
    	if (dtlCode.getOrd() > 99) {
        	errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 100 미만으로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal1().length() > 50) {
        	errors.rejectValue("cdVal1", "");
    		msg += "<br/>기타1을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal2().length() > 50) {
        	errors.rejectValue("cdVal2", "");
    		msg += "<br/>기타2을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal3().length() > 50) {
        	errors.rejectValue("cdVal3", "");
    		msg += "<br/>기타3을 50자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("상세코드 등록 에러발생 : " + errors);
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
    		dtlCode.setCretr(account.getMemNo());
    		dtlCode.setChgr(account.getMemNo());
	    	if (codeService.createCodeDtl(dtlCode) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}

    	} catch (DuplicateKeyException ex1) {
    		ex1.printStackTrace();
        	map.put("msg", "상세코드 중복");
    		status = Status.ERROR.value();
    	} catch (DataIntegrityViolationException ex2) {
    		ex2.printStackTrace();
        	map.put("msg", "존재하지 않는 그룹코드");
    		status = Status.ERROR.value();
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "상세코드 등록실패");
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
     * 그룹코드 수정
     * @return
     */
    @PostMapping("/updateCodeGrp")
    @ResponseBody
    public Response updateCodeGrp(GrpCode grpCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("# 그룹코드 수정 grpCode : " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
            errors.rejectValue("grpGrpCd", "");
    		msg += "<br/>수정할 그룹코드를 선택해주세요.";
    	}
    	if ("".equals(grpCode.getGrpGrpCdNm()) || grpCode.getGrpGrpCdNm() == null) {
            errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 입력해주세요.";
    	}
    	if ("".equals(grpCode.getGrpUseYn()) || grpCode.getGrpUseYn() == null) {
            errors.rejectValue("grpUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}

    	if (grpCode.getGrpGrpCdNm().length() > 50) {
        	errors.rejectValue("grpGrpCdNm", "");
    		msg += "<br/>그룹코드명을 50자 내로 입력해주세요.";
    	}
    	if (grpCode.getGrpDesc().length() > 200) {
        	errors.rejectValue("grpGrpDesc", "");
    		msg += "<br/>그룹코드 설명을 200자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("그룹코드 수정 에러발생 : " + errors);
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
    		grpCode.setChgr(account.getMemNo());
	    	if (codeService.updateCodeGrp(grpCode) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}

    	} catch (DuplicateKeyException ex1) {
    		ex1.printStackTrace();
        	map.put("msg", "그룹코드 중복");
    		status = Status.ERROR.value();
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 수정실패");
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
     * 상세코드 수정
     * @return
     */
    @PostMapping("/updateCodeDtl")
    @ResponseBody
    public Response updateCodeDtl(DtlCode dtlCode, Errors errors, @CurrentAccount Account account) {
    	log.debug("# 상세코드 수정 dtlCode : " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(dtlCode.getCd()) || dtlCode.getCd() == null || "".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null) {
        	errors.rejectValue("dtlGrpCd", "");
    		msg += "<br/>수정할 상세코드를 선택해주세요.";
    	}
    	if ("".equals(dtlCode.getCdNm()) || dtlCode.getCdNm() == null) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 입력해주세요.";
    	}
    	if ("".equals(dtlCode.getDtlUseYn()) || dtlCode.getDtlUseYn() == null) {
        	errors.rejectValue("dtlUseYn", "");
    		msg += "<br/>사용여부를 체크해주세요.";
    	}
    	
    	if (dtlCode.getCdNm().length() > 200) {
        	errors.rejectValue("cdNm", "");
    		msg += "<br/>상세코드명을 200자 내로 입력해주세요.";
    	}
    	if (dtlCode.getOrd() > 99) {
        	errors.rejectValue("ord", "");
    		msg += "<br/>정렬번호를 100 미만으로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal1().length() > 50) {
        	errors.rejectValue("cdVal1", "");
    		msg += "<br/>기타1을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal2().length() > 50) {
        	errors.rejectValue("cdVal2", "");
    		msg += "<br/>기타2을 50자 내로 입력해주세요.";
    	}
    	if (dtlCode.getCdVal3().length() > 50) {
        	errors.rejectValue("cdVal3", "");
    		msg += "<br/>기타3을 50자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("상세코드 수정 에러발생 : " + errors);
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
    		dtlCode.setChgr(account.getMemNo());
	    	if (codeService.updateCodeDtl(dtlCode) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}    		

    	} catch (DuplicateKeyException ex1) {
    		ex1.printStackTrace();
        	map.put("msg", "상세코드 중복");
    		status = Status.ERROR.value();
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "상세코드 수정실패");
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
     * 그룹코드 삭제
     * @return
     */
    @PostMapping("/deleteCodeGrp")
    @ResponseBody
    public Response deleteCodeGrp(GrpCode grpCode) {
    	log.debug("# 그룹코드 삭제 grpCode : " + grpCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(grpCode.getGrpGrpCd()) || grpCode.getGrpGrpCd() == null) {
        	map.put("msg", "삭제할 그룹코드를 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}

    	try {
	    	if (codeService.deleteCodeGrp(grpCode) > 0) {
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "그룹코드 삭제실패");    
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
     * 상세코드 삭제
     * @return
     */
    @PostMapping("/deleteCodeDtl")
    @ResponseBody
    public Response deleteCodeDtl(DtlCode dtlCode) {
    	log.debug("# 상세코드 삭제 dtlCode : " + dtlCode);
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();

    	if ("".equals(dtlCode.getDtlGrpCd()) || dtlCode.getDtlGrpCd() == null || "".equals(dtlCode.getCd()) || dtlCode.getCd() == null) {
        	map.put("msg", "삭제할 상세코드를 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}
    	
    	try {
	    	if (codeService.deleteCodeDtl(dtlCode) > 0) {
		    	map.put("msg", "삭제되었습니다.");
			} else {
		    	map.put("msg", "삭제할 데이터가 없습니다.");
	    		status = Status.ERROR.value();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "상세코드 삭제실패");
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
