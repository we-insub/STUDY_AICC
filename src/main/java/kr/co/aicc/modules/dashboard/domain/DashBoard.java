package kr.co.aicc.modules.dashboard.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashBoard extends Common {
    @Builder.Default
    private String dashBoardType = "chnl";
    private long chnlNo;
    private String cnmlNm;
    private String groupCode;
    private String groupCodeName;
    private List<String> pChnlNo;
    private List<String> pGroupCode;
    @Builder.Default
    private LocalDateTime startTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), 8, 0);
    @Builder.Default
    private LocalDateTime endTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().plusDays(1).getDayOfMonth(), 8, 0);
    private List<DashBoardInfo> dashBoardInfoList;
}
