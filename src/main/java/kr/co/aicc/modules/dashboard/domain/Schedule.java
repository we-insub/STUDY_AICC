package kr.co.aicc.modules.dashboard.domain;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.infra.enums.SchedTypeEnum;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.webfos.dto.MemberInfo;
import kr.co.aicc.modules.webfos.dto.ProgramInfo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@EqualsAndHashCode(of = "schedNo", callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends Common {
    private long schedNo; // 스케줄 번호
    private long prntSchedNo; // 부모 스케줄 번호 (복사된 경우)
    private long chnlNo; // 채널 번호
    private String progNm; // 프로그램 명
    private String baseDate; // 기준 시간
    private LocalDateTime bgnTime; // 방영 시작 시간
    private LocalDateTime endTime; // 방영 종료 시간
    private LocalDate curDay;
    private String totalWorkAmt; // 일정 총 작업 분량
    private String curWorkAmt; // 현제 작업 분량
    private SchedTypeEnum schedType; // 스케줄 타입 (01,02,03,04,05)
    private String schedTypeNm; // 스케줄 타입 명 (생방, 재방, 지원)
    private MemberSchedule memberSchedule; // 작업자
    private List<MemberSchedule> memberSchedules; // 작업자 리스트
    private int record; // 파일업로드 이력 참조 번호
    private Account account;
    private boolean isWebposButton; // 웹포스 버튼 노출 여부
    private LocalDateTime searchStartTime;
    private LocalDateTime searchEndTime;

    public void setWebfosButtonDisplayByProgramInfos (List<ProgramInfo> programInfos, Account account) {
        List<MemberInfo> memberInfos = null;
        for (ProgramInfo programInfo : programInfos) {
            if (programInfo.getSchedNo() == this.schedNo) {
                memberInfos = programInfo.getMemberInfoList();
                for (MemberInfo memberInfo : memberInfos) {
                    if (memberInfo.getMemNo().intValue() == account.getMemNo().intValue() && memberInfo.isParticipation()) {
                        this.setWebposButton(false);
                    }
                }
            }
        }
    }

}
