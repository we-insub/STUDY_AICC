package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.aicc.infra.common.dto.Response;
import kr.co.aicc.infra.enums.ResponseEnum.ErrorCode;
import kr.co.aicc.infra.enums.ResponseEnum.Status;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.CurrentAccount;
import kr.co.aicc.modules.settings.dto.ChannelForm;
import kr.co.aicc.modules.settings.service.ChannelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class ChannelResController {
    private final ReloadableResourceBundleMessageSource messageSource;
    private final ChannelSettingService channelSettingService;

    /**
     * 채널 상세
     * @return
     */
    @PostMapping("channelDtl")
    @ResponseBody
    public Response channelDtl(ChannelForm channelForm, Model model) {
    	log.debug("채널 상세 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
        List<ChannelForm> channelList = channelSettingService.getChannelList(channelForm);
		log.debug("channelList.get(0) " + channelList.get(0));
		
		map.put("channel", channelList.get(0));

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * 채널 등록
     * @return
     */
    @PostMapping("createChannel")
    @ResponseBody
    public Response createChannel(ChannelForm channelForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("채널 등록 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();
    	
    	if ("".equals(channelForm.getChnlNm()) || channelForm.getChnlNm() == null) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 입력해주세요.";
    	}
    	if (channelForm.getPicFile().isEmpty()) {
            errors.rejectValue("picFile", "");
    		msg += "<br/>채널로고를 등록해주세요.";
    	}
    	if ("".equals(channelForm.getLinkUrl()) || channelForm.getLinkUrl() == null) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsPort()) || channelForm.getTrnsPort() == null) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsIp()) || channelForm.getTrnsIp() == null) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를 입력해주세요.";
    	}

    	if (channelForm.getChnlNm().length() > 20) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 20자 내로 입력해주세요.";
    	}
    	if (channelForm.getChnlDesc().length() > 200) {
            errors.rejectValue("chnlDesc", "");
    		msg += "<br/>채널설명을 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getLinkUrl().length() > 200) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsIp().length() > 15) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를  15자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsPort().length() > 15) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를  15자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("채널 등록 : 에러발생 :: " + errors);
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
    		channelForm.setCretr(account.getMemNo());
    		channelForm.setChgr(account.getMemNo());
	    	if (channelSettingService.createChannel(channelForm) > 0) {
	        	map.put("msg", "등록되었습니다.");
	    	} else {
	        	map.put("msg", "등록할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 등록실패");
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
     * 채널 수정
     * @return
     */
    @PostMapping("updateChannel")
    @ResponseBody
    public Response updateChannel(ChannelForm channelForm, Errors errors, Model model, @CurrentAccount Account account) {
    	log.debug("채널 수정 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String msg = "";
    	String status = Status.SUCCESS.value();

    	if (channelForm.getChnlNo() == null) {
            errors.rejectValue("chnlNo", "");
    		msg += "<br/>수정할 채널을 선택해주세요.";
    	}
    	if ("".equals(channelForm.getChnlNm()) || channelForm.getChnlNm() == null) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 입력해주세요.";
    	}
    	if ("".equals(channelForm.getLinkUrl()) || channelForm.getLinkUrl() == null) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsPort()) || channelForm.getTrnsPort() == null) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를 입력해주세요.";
    	}
    	if ("".equals(channelForm.getTrnsIp()) || channelForm.getTrnsIp() == null) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를 입력해주세요.";
    	}

    	if (channelForm.getChnlNm().length() > 20) {
            errors.rejectValue("chnlNm", "");
    		msg += "<br/>채널명을 20자 내로 입력해주세요.";
    	}
    	if (channelForm.getChnlDesc().length() > 200) {
            errors.rejectValue("chnlDesc", "");
    		msg += "<br/>채널설명을 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getLinkUrl().length() > 200) {
            errors.rejectValue("linkUrl", "");
    		msg += "<br/>채널링크를 200자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsIp().length() > 15) {
            errors.rejectValue("trnsIp", "");
    		msg += "<br/>송출IP를  15자 내로 입력해주세요.";
    	}
    	if (channelForm.getTrnsPort().length() > 15) {
            errors.rejectValue("trnsPort", "");
    		msg += "<br/>송출Port를  15자 내로 입력해주세요.";
    	}

        if (errors.hasErrors()) {        	
    		log.debug("채널 수정 : 에러발생 :: " + errors);
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
    		channelForm.setCretr(account.getMemNo());
    		channelForm.setChgr(account.getMemNo());
	    	if (channelSettingService.updateChannel(channelForm) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 수정실패");
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
     * 채널 삭제
     * @return
     */
    @PostMapping("deleteChannel")
    @ResponseBody
    public Response deleteChannel(ChannelForm channelForm, Model model) {
    	log.debug("채널 삭제 channelForm ::::: " + channelForm);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();

    	if (channelForm.getChnlNo() == null) {
        	map.put("msg", "삭제할 채널을 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}
    	
    	try {
	    	if (channelSettingService.deleteChannel(channelForm) > 0) {
	        	map.put("msg", "삭제되었습니다.");
	    	} else {
	        	map.put("msg", "삭제할 데이터가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "채널 삭제실패");
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
