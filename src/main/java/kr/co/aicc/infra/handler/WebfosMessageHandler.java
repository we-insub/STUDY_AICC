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
package kr.co.aicc.infra.handler;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.service.UserAccount;
import kr.co.aicc.modules.webfos.service.WebfosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebfosMessageHandler implements ChannelInterceptor {

	private final WebfosService webfosService;
	
    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
    	StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    	
    	Account account = null;
    	String memId = null;
        // ChannelInterceptor에 의해 session 정보가 유실되어 Authentication 정보를 Message에서 축출 함
    	Object object = message.getHeaders().get("simpUser");
    	
    	if (object instanceof UsernamePasswordAuthenticationToken) {
    		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)object;
    		UserAccount userAccount = (UserAccount) authentication.getPrincipal();
    		account = userAccount.getAccount();
    	} else if (object instanceof RememberMeAuthenticationToken) {
    		RememberMeAuthenticationToken authentication = (RememberMeAuthenticationToken)object;
    		UserAccount userAccount = (UserAccount) authentication.getPrincipal();
    		account = userAccount.getAccount();
    	}
    			
    	if (ObjectUtils.isEmpty(account)) {
    		memId = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
    		account = new Account();
    		account.setMemId(memId);
    	}
    	
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            //log.info("CONNECT message : {}", message);

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
        	//log.info("SUBSCRIBE message : {}", message.getHeaders());
        	
            // header정보에서 구독 destination정보를 얻고, schedNo를 추출한다.
            String destination = getSchedNo(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidSchedNo"));
            // 프로그램방에 들어온 클라이언트 sessionId를 schedNo와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            
            if (NumberUtils.isCreatable(destination)) {
            	webfosService.subscribeOfProgram(destination, account, sessionId);
            }
            
            //log.info("SUBSCRIBED - schedNo : {}, memId : {}, sessionId : {}", destination, memId, sessionId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
        	String sessionId = (String) message.getHeaders().get("simpSessionId");
        	
        	// 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
        	webfosService.disconnectedProgram(sessionId, account);
        	
        	//log.info("DISCONNECTED {}, {}", sessionId, schedNo);
        }
        
        return message;
    }
    
    private String getSchedNo(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }
}
