package kr.co.aicc.modules.dashboard.domain;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.schedule.domain.GroupTeam;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@EqualsAndHashCode(of = "memSchedNo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSchedule extends Common {
    private long memSchedNo; // 회원 스케줄 번호
    private String schedGb; // 회원 스케줄 구분
    private long memNo; //  회원번호
    private String memId; //  회원아이디
    private String memNm; // 회원명
    private long schedNo; // 스케줄 번호
    private String baseDate; // 기준일 시간 포멧
    private LocalDateTime bgnTime; // 시작시간
    private LocalDateTime endTime; // 종료시간
    private LocalDate toDay;
    private int ord; // 순서
    private String workAmt; // 작업분량
    private String startAmt; // 작업시작분
    private long prntMemSchedNo;
    private String profImgData;
    private GroupTeam groupTeam;
    private ChnlTeam chnlTeam;
    private List<ChnlTeam> chnlTeamList;
    private String fileNm;
    private String sysFileNm;
    private String thumbSysFileNm;
    private String filePath;
    private String fileSize;
    private LocalDateTime searchStartTime;
    private LocalDateTime searchEndTime;
}
