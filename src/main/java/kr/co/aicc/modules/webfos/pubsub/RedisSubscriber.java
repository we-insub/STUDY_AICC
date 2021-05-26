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
package kr.co.aicc.modules.webfos.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.aicc.infra.enums.WebfosEnum.MessageType;
import kr.co.aicc.modules.webfos.dto.WebfosMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
     */
    public void sendMessage(String publishMessage) {
        try {
            // WebfosMessage 객채로 맵핑
        	WebfosMessage webfosMessage = objectMapper.readValue(publishMessage, WebfosMessage.class);
        	// log.info("schedNo : {}", webfosMessage.getSchedNo());
        	// 채팅방을 구독한 클라이언트에게 메시지 발송
        	if (!(MessageType.QUIT.equals(webfosMessage.getType()) && webfosMessage.getUserInfoList().size() == 0)) {
        		messagingTemplate.convertAndSend("/sub/webfos/program/" + webfosMessage.getSchedNo(), webfosMessage);
        	}
            
            if (MessageType.QUIT.equals(webfosMessage.getType()) || MessageType.ENTER.equals(webfosMessage.getType())) {
            	messagingTemplate.convertAndSend("/sub/schedule/timeline", webfosMessage);
            }
            
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
