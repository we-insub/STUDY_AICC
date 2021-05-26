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
 * @package kr.co.aicc.infra.config
 * @file SecurityConfig.java
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
 * 2020. 06. 18	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.webfos.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import kr.co.aicc.infra.enums.CommonEnum;
import kr.co.aicc.infra.enums.WebfosEnum.MessageType;
import kr.co.aicc.infra.exception.BaseException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.webfos.domain.SubtitleDetail;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListReq;
import kr.co.aicc.modules.webfos.dto.ChnlSubDtlListRes;
import kr.co.aicc.modules.webfos.dto.LineInfo;
import kr.co.aicc.modules.webfos.dto.MemberInfo;
import kr.co.aicc.modules.webfos.dto.ProgramInfo;
import kr.co.aicc.modules.webfos.dto.ScheduleInfo;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import kr.co.aicc.modules.webfos.dto.TransServerInfo;
import kr.co.aicc.modules.webfos.dto.UserInfo;
import kr.co.aicc.modules.webfos.dto.WebfosMessage;
import kr.co.aicc.modules.webfos.dto.WebfosProgram;
import kr.co.aicc.modules.webfos.repository.WebfosDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WebfosServiceImpl implements WebfosService {

	private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    
	private final WebfosDao webfosDao;

	private final SocketManagementService socketManagementService;
	/**
	 * 프로그램방 생성 
	 */
	@Override
	public WebfosProgram createProgram(String schedNo, String progNm) {
		WebfosProgram webfosProgram = null;
		
		webfosProgram = webfosDao.findBySchedNoProgram(schedNo);
		
		if (ObjectUtils.isEmpty(webfosProgram)) {
			webfosProgram = webfosDao.createProgram(schedNo, progNm);
		}
		
		return webfosProgram;
	}
	
	/**
	 * 특정 스케줄에 배정된 멤버 유무 확인  
	 */
	@Override
	public boolean checkMemberOfSchedule(Long schedNo, Long memNo) {
		return !CollectionUtils.isEmpty(webfosDao.findBySchedNoAndMemNoMemSched(schedNo, memNo));
	}
	
	/**
	 * 참자의 송출권한 유무 확인  (삭제예정)
	 */
	@Override
	public boolean checkTransmissionPermission(String schedNo) {
		WebfosProgram webfosProgram = webfosDao.findBySchedNoProgram(schedNo);
		
		return !ObjectUtils.isEmpty(webfosProgram);
	}
	
	/**
     * 프로그램방에 입장 (구독) 
     */
	@Override
    public void subscribeOfProgram(String schedNo, Account account, String sessionId) {
		// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
		webfosDao.createParticipatingProgram(sessionId, schedNo);
		
		// ROLE_WATCH 아닐경우 webfos 사용자 list에 추가한다.(ROLE_WATCH일 경우는 작업자가 진입여부를 알지 못하게 하기 위하여 제외)
		if (!account.hasRole(CommonEnum.Role.ROLE_WATCH.value())) {
			
			// 프로그램방에 입장한 사용자의 list 관리를 위해 추가 
			webfosDao.createUserOfProgram(schedNo, account.getMemId());
			
			// 프로그램방 글쓰기 권한 list 관리를 위한 추가
			webfosDao.createWritePermissionOfProgram(schedNo, account.getMemId());
			
			// 프로그램방 송출 권한 list 관리를 위한 추가
			webfosDao.createTransmissionPermissionOfProgram(schedNo, account.getMemId());
			
			// 메시지 발송
			sendWebfosMessage(WebfosMessage.builder().type(MessageType.ENTER).schedNo(schedNo).sender(account.getMemId()).build());
			log.info("SUBSCRIBE - {} [schedNo : {}, memId : {}]", MessageType.ENTER, schedNo, account.getMemId());
		}
    }
    
    /**
     * 프로그램방에서 나가기 (disconnected) 
     */
	@Override
    public String disconnectedProgram(String sessionId, Account account) {
		String result = null;
		
		// 해당 sessionId가 입장한 채팅방Id 축출
		String schedNo = webfosDao.findBySessionIdParticipatingProgram(sessionId);
		
		List<UserInfo> userInfoList = null;
		
		if (!StringUtils.isEmpty(schedNo)) {
			// ROLE_WATCH인 경우 webfos 사용자 list에 추가되지 않았기때문에 삭제에서 제외
			if (!account.hasRole(CommonEnum.Role.ROLE_WATCH.value())) {
				
				// 프로그램방에 입장한 사용자의 list 관리를 위해 삭제 
				webfosDao.deleteUserOfProgram(schedNo, account.getMemId());
				
				// 프로그램방에 입장한 사용자의 글쓰기 권한 정보 관리를 위해 삭제
				webfosDao.deleteWritePermissionOfProgram(schedNo, account.getMemId());
				
				// 프로그램방에 입장한 사용자의 글쓰기 권한 정보 관리를 위해 삭제
				webfosDao.deleteTransmissionPermissionOfProgram(schedNo, account.getMemId());
				
				// 프로그램방에 입장한 사용자의 수를 확인
				userInfoList = webfosDao.findBySchedNoUserOfProgram(schedNo);
				
				// 클라이언트 퇴장 메시지를 프로그램방에 발송한다. 마지막 클라이언트인 경우 publish하지 않는다.(redis publish)
				//if (!CollectionUtils.isEmpty(userInfoList)) {
				sendWebfosMessage(WebfosMessage.builder().type(MessageType.QUIT).schedNo(schedNo).sender(account.getMemId()).userInfoList(userInfoList).build());
				//}
				log.info("DISCONNECT - {} [schedNo : {}, memId : {}]", MessageType.QUIT, schedNo, account.getMemId());
			}
			
			// 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
			webfosDao.deleteParticipatingProgram(sessionId);
			
			// 프로그램방에서 마지막으로 퇴장하는 사용자일 경우 해당 채팅방을 삭제
			if (CollectionUtils.isEmpty(userInfoList)) webfosDao.deleteProgram(schedNo);
			
			result = schedNo;
		}
		
        return result;
    }
    
    /**
     * 프로그램방에 메시지 발송
     */
	@Override
	public void sendWebfosMessage(WebfosMessage webfosMessage) {
		
		//log.info("webfosMessage : {}", webfosMessage.toString());
        if (MessageType.AUTHW.equals(webfosMessage.getType())) {
        	// 프로그램방에 입장한 사용자의 글쓰기 권한 정보 관리를 위해 삭제
        	webfosDao.deleteWritePermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getSender());
        	// 프로그램방에 입장한 사용자의 송출 권한 정보 관리를 위해 삭제
            int result = webfosDao.deleteTransmissionPermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getSender());
            // 프로그램방 글쓰기 권한 list 관리를 위한 추가
            webfosDao.createWritePermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getTargetUser());
            if (result > 0) {
            	webfosDao.createTransmissionPermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getTargetUser());
            }
        }

        if (MessageType.AUTHT.equals(webfosMessage.getType())) {
        	webfosDao.deleteTransmissionPermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getSender());
        	List<String> writePermissionList = webfosDao.findBySchedNoWritePermissionOfProgram(webfosMessage.getSchedNo());
        	//log.info("writePermissionList : {} ({})", writePermissionList, writePermissionList.size());
        	// 쓰기 권한이 있는 사용자들중 현재 송출 권한을 갖고있던 사용자가 아닌 다른 사용자에게 권한 이양
        	writePermissionList.forEach(memId -> {
        		if (!webfosMessage.getSender().equals(memId)) webfosMessage.setTargetUser(memId);
        	});
        	webfosDao.createTransmissionPermissionOfProgram(webfosMessage.getSchedNo(), webfosMessage.getTargetUser());
        }
        
        // 송출시 송출내용 저장 및 전송(전송 spec 미정)
        if ((MessageType.TRANS.equals(webfosMessage.getType()) || MessageType.TRANSALL.equals(webfosMessage.getType())) && !StringUtils.isEmpty(webfosMessage.getTransWords())) {
        	
        	String relpaceTransMessage = this.replaceWords(webfosMessage.getTransMessage());
        	String relpaceTransWords = this.replaceWords(webfosMessage.getTransWords());
        	
        	webfosMessage.setTransMessage(relpaceTransMessage);
        	webfosMessage.setTransWords(relpaceTransWords);
        	// HTML 태그 변환
			String replaceWord = this.replaceLine(webfosMessage.getTransWords());
			replaceWord += " "; // 문장끝에 공백추가 2020-11-23 담당자 요청
			webfosMessage.setTransWords(replaceWord);
			log.info("webfosMessage.getTransWords() : {}", webfosMessage.getTransWords());
        	socketManagementService.transmissionMessageUDP(webfosMessage.getLocalPort(), webfosMessage.getTransWords());
        	saveSubtitleDetail(webfosMessage.getSchedNo(), webfosMessage.getTransWords(), webfosMessage.getMemNo());
        }
        
        List<UserInfo> userInfoList = getUserInfoOfProgram(webfosMessage.getSchedNo());
        
        //webfosMessage.setUserCount(userInfoList.size());
        webfosMessage.setUserInfoList(userInfoList);
        
        if (webfosMessage.getMessage() != null && webfosMessage.getMessage().length() > 0) {
        	//log.info("chatMessage : {}", webfosMessage.getMessage());
        	String relpaceMessage = webfosMessage.getMessage();
        	log.info("replace chatMessage : {}", relpaceMessage);
        	webfosMessage.setMessage(relpaceMessage);
        }
        
        if (MessageType.TRANSALL.equals(webfosMessage.getType())) {
        	webfosMessage.setTransWords("");
        }
        
        redisTemplate.convertAndSend(channelTopic.getTopic(), webfosMessage);
	}
    
	@Override
    public List<UserInfo> getUserInfoOfProgram(String schedNo) {
    	List<UserInfo> participatingUserList = webfosDao.findBySchedNoUserOfProgram(schedNo);
    	List<String> writePermissionList = webfosDao.findBySchedNoWritePermissionOfProgram(schedNo);
    	List<String> transPermissionList = webfosDao.findBySchedNoTransmissionPermissionOfProgram(schedNo);
    	
    	int wirtePermissionCount = 0;
    	
        if (!CollectionUtils.isEmpty(writePermissionList)) wirtePermissionCount = writePermissionList.size();
        // 입장한 사용자들 권한 세팅
        for (UserInfo userInfo : participatingUserList) {
        	// 글쓰기 권한 세팅
        	for (String writePermissionMemId : writePermissionList) {
        		if (wirtePermissionCount >= 2) {
        			if (userInfo.getMemId().equals(writePermissionMemId)) userInfo.setWritePermission(true);
        		} else {
        			if (userInfo.getMemId().equals(writePermissionMemId)) {
        				userInfo.setWritePermission(true);
        			} else if (!userInfo.getMemId().equals(writePermissionMemId)) {
        				userInfo.setWritePermission(true);
        				// 프로그램방 글쓰기 권한 list 관리를 위한 추가
        				webfosDao.createWritePermissionOfProgram(schedNo, userInfo.getMemId());
        				
        	            wirtePermissionCount++;
        			}
        		}
        	}
        	
        	// 송출 권한 세팅 (쓰기 권한이 있는 사용자에 한해서)
        	if (userInfo.isWritePermission()) {
        		if (CollectionUtils.isEmpty(transPermissionList)) {
        			userInfo.setTransmissionPermission(true);
        			// 프로그램방 송출 권한 list 관리를 위한 추가
        			webfosDao.createTransmissionPermissionOfProgram(schedNo, userInfo.getMemId());
        		} else {
        			if (userInfo.getMemId().equals(transPermissionList.get(0))) {
        				userInfo.setTransmissionPermission(true);
        			}
        		}
        	}
        }
        
        return participatingUserList;
    }
	
	@Override
	public ScheduleInfo getScheduleInfo(Long schedNo) {
		return webfosDao.findOneBySchedNoSched(schedNo);
	}
	
	@Override
	public List<SubtitleDetailInfo> getSubtitleDetailListOfChennal(Long chnlNo, String searchNm, String searchDt, Long srchSchedNo) {
		return webfosDao.findByChnlNoOfSchedSubDtl(chnlNo, searchNm, searchDt, srchSchedNo);
	}
	
	@Override
	public int getChnlSubDtlListCnt(ChnlSubDtlListReq chnlSubDtlListReq) {
		return webfosDao.selChnlSubDtlListCnt(chnlSubDtlListReq);
	}
	
	@Override
	public List<ChnlSubDtlListRes> getChnlSubDtlList(ChnlSubDtlListReq chnlSubDtlListReq) {
		return webfosDao.selChnlOfSubDtlList(chnlSubDtlListReq);
	}
	
	@Override
	public List<LineInfo> getSubtitleDetailInfo(Long targetSchedNo, int maxByte) {
		// 한 프로그램의 자막작업이 완료된 전체내용
		String text = webfosDao.findOneBySchedNoSubDtl(targetSchedNo);

		List<LineInfo> lineInfoList = new ArrayList<>();
		if (StringUtils.isEmpty(text)) {
			return lineInfoList;
		}
		try {
			String charset = "EUC-KR";
			byte[] textBytes = text.getBytes(charset);
			int textLength = textBytes.length;
			
			if (textLength <= maxByte) {
				lineInfoList.add(LineInfo.builder().num(0).info(text).size(textLength).build());
			} else {
				int lineNum = 0;
				int len = 0;
				int charSize = 0;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String line = null;
				for (int i = 0; i < textLength; i++) {
					if (((int) textBytes[i] & 0x80) != 0) {
						charSize = 2;
					} else {
						charSize = 1;
					}
					if ((len + charSize) > maxByte) {
						line = baos.toString(charset);
						lineInfoList.add(LineInfo.builder().num(lineNum++).info(line).size(baos.size()).build());
						
						len = 0;
						baos.reset();
					}
					len += charSize;
					baos.write(textBytes, i, charSize);
					if (charSize == 2) {
						i++;
					}
				}
				if (baos.size() > 0) {
					line = baos.toString(charset);
					lineInfoList.add(LineInfo.builder().num(lineNum++).info(line).size(baos.size()).build());
				}
			}
		} catch (Exception e) {
			throw new BaseException(e);
		}
		
		/*
		int maxLength = 15;
		int textLength = text.length();
		int totalCount = textLength / maxLength + 1;

		String line = "";
		
		for (int i = 0; i < totalCount; i++) {
			int lastIndex = (i + 1) * maxLength; 
		  
			if(textLength > lastIndex){
				line = text.substring(i * maxLength, lastIndex);
			}else{
				line = text.substring(i * maxLength);
			}
			
			try {
				lineInfoList.add(LineInfo.builder().num(i).info(line).size(line.getBytes("MS949").length).build());
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
			}
		}
		*/
		
		return lineInfoList;
	}
	
	@Override
	public List<ProgramInfo> getProgramInfoListOfWebfos() {
		List<ProgramInfo> programInfoList = new ArrayList<ProgramInfo>();
		
		// webfos로 생성된 프로그램Room의 list를 조회 
		List<WebfosProgram> programList = webfosDao.findAllProgram();
		
		for (WebfosProgram program : programList) {
			List<MemberInfo> memberList = webfosDao.findBySchedNoMemSched(Long.parseLong(program.getSchedNo()));
			List<UserInfo> userList = webfosDao.findBySchedNoUserOfProgram(program.getSchedNo());
			
			if (!CollectionUtils.isEmpty(userList)) {
				for (MemberInfo member :memberList) {
					member.setParticipation(userList.stream().anyMatch(user -> user.getMemId().equalsIgnoreCase(member.getMemId())));
				}
				
				if (memberList.stream().anyMatch(member -> member.isParticipation() == true)) {
					programInfoList.add(ProgramInfo.builder().schedNo(Long.parseLong(program.getSchedNo())).memberInfoList(memberList).build());
				}
			}
			
		}
		
		return programInfoList;
	}
	
	@Override
	public void sendSubtileDetailOfRerun(String schedNo, String transWords, Long memNo, String localPort) {
//    	socketManagementService.transmissionMessageUDP(Integer.parseInt(localPort), transWords);
    	saveSubtitleDetail(schedNo, transWords, memNo);
	}
	
	@Async
	public void saveSubtitleDetail(String schedNo, String transWords, Long memNo) {
		SubtitleDetail subtitleDetail = new SubtitleDetail();

		subtitleDetail.setSchedNo(Long.parseLong(schedNo));
		subtitleDetail.setSubDesc(transWords);
		subtitleDetail.setCretr(memNo);
		subtitleDetail.setChgr(memNo);
				
		webfosDao.createSubDtl(subtitleDetail);
  	}
	
	@Override
	public List<TransServerInfo> getTrnsSvrInfo(Long chnlNo) {
		return webfosDao.findByChnlNoTrnsSvr(chnlNo);
	}
	
	private String replaceWords(String words) {
		String result = null;
		
		result = words.replaceAll("<div><br></div>", "\n").replaceAll("<div>", "\n").replaceAll("</div>", "").replaceAll("<br>", "\n");
		
		return words;
	}
	
	private String replaceLine(String words) {
		String result = words;

		result = result.replaceAll("<span style=\"color: rgb(151, 151, 151);\">", "");
		result = result.replaceAll("<span style=\"color: rgb(0, 0, 0);\">", "");
		result = result.replaceAll("</span>", "");
		result = result.replaceAll("<p><br>", "\r\n");
		result = result.replaceAll("<p>", "");
		result = result.replaceAll("</p>", "");
		result = result.replaceAll("<div><br></div>", "\r\n");
		result = result.replaceAll("</div>", "");
		result = result.replaceAll("<br>", "");
		result = result.replaceAll("<div>", "\r\n");
		result = result.replaceAll("&nbsp;", " ");
		return result;
	}
	
}
