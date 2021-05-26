package kr.co.aicc.modules.settings.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.Model;
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
import kr.co.aicc.modules.settings.dto.ChnlTeamDto;
import kr.co.aicc.modules.settings.dto.MemGrpDto;
import kr.co.aicc.modules.settings.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/settings")
public class TeamResController {
    private final ReloadableResourceBundleMessageSource messageSource;
	private final TeamService teamService;

    /**
     * 특정 그룹 맴버 정보
     * @return
     */
    @GetMapping("getGrpMemInfo")
    @ResponseBody
    public Response getGrpMemInfo(MemGrpDto memGrpDto) {
    	log.debug("특정 그룹 맴버 정보 memGrpDto ::::: " + memGrpDto);	

    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
    	List<MemGrpDto> memList = teamService.getGrpMemInfo(memGrpDto);

		map.put("memList", memList);

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }

    /**
     * 특정 그룹 맴버 등록
     * @return
     */
    @PostMapping("createGrpMem")
    @ResponseBody
    public Response createGrpMem(MemGrpDto memGrpDto, Model model, @CurrentAccount Account account) {
    	log.debug("특정 그룹 맴버 등록 memGrpDto ::::: " + memGrpDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
    	if (memGrpDto.getPMemNo().length == 0 || memGrpDto.getPMemNo() == null) {
        	map.put("msg", "맴버를 배정해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	} else if ("".equals(memGrpDto.getGrpType()) || memGrpDto.getGrpType() == null) {
        	map.put("msg", "맴버그룹(팀)을 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}

    	try {
    		memGrpDto.setCretr(account.getMemNo());
    		memGrpDto.setChgr(account.getMemNo());
	    	if (teamService.createGrpMem(memGrpDto) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "맴버 등록실패");
    		status = Status.ERROR.value();
    	}

        return Response.builder()
                .status(status)
                .code(ErrorCode.AICC_0000_200.value())
                .data(map)
                .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                .build();
    }
    
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 특정 채널 맴버 등록
     * @return
     */
    @PostMapping("createChnlTeam")
    @ResponseBody
    public Response createChnlTeam(ChnlTeamDto chnlTeamDto, Model model, @CurrentAccount Account account) {
    	log.debug("특정 채널 맴버 등록 chnlTeamDto ::::: " + chnlTeamDto);
    	
    	HashMap<String,Object> map = new HashMap<String,Object>();
    	String status = Status.SUCCESS.value();
    	
//    	if (chnlTeamDto.getPMemNo().length == 0 || chnlTeamDto.getPMemNo() == null) {
//        	map.put("msg", "맴버를 배정해주세요.");
//            return map;
//    	} else 
    	if (chnlTeamDto.getChnlNo() == 0) {
        	map.put("msg", "맴버그룹(팀)을 선택해주세요.");
    		status = Status.ERROR.value();
            return Response.builder()
                    .status(status)
                    .code(ErrorCode.AICC_0000_200.value())
                    .data(map)
                    .message(messageSource.getMessage(ErrorCode.AICC_0000_200.value(), null, null))
                    .build();
    	}

    	try {
    		chnlTeamDto.setCretr(account.getMemNo());
    		chnlTeamDto.setChgr(account.getMemNo());
	    	if (teamService.createChnlTeam(chnlTeamDto) > 0) {
	        	map.put("msg", "수정되었습니다.");
	    	} else {
	        	map.put("msg", "수정할 정보가 없습니다.");
	    		status = Status.ERROR.value();
	    	}
        	
    	} catch (Exception e) {
    		e.printStackTrace();
        	map.put("msg", "맴버 등록실패");
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
