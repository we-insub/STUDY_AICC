package kr.co.aicc.modules.schedule.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleFile extends Common {
    private long fileNo;
    @Builder.Default
    private String fileGb = "SCHED";
    private long refNo; // 채널 번호
    private String fileNm;
    private String sysFileNm;
    private String filePath;
    private String fileSize;
}
