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
package kr.co.aicc.modules.webfos.dto;

import java.util.List;

import kr.co.aicc.infra.enums.WebfosEnum.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class WebfosMessage {

    private MessageType type; // 메시지 타입
    private String schedNo; // 프로그램번호
    private String sender; // 메시지 보낸사람
    private Long memNo;	// 멤버 번호
    private String message; // 메시지
    //private int userCount; // 프로그램방 인원수, 프로그램방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private List<UserInfo> userInfoList;
    private String targetUser; // 글쓰기 권한 이양을 위한 대상
    private String transMessage; // 송출내용 저장
    private String transWords; // 송출할 단어들 내역
    private List<Integer> localPort; // 송출목적지의 socket 연결시 받은 localPort 
    private String keyCode; // 입력된 키코드
}
