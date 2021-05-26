package kr.co.aicc.modules.schedule.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupTeam extends Common {
    private long memGrpNo;
    private String grpType;
    private String grpName;
    private long memNo;
    private String memId;
    private String memNm;
}
