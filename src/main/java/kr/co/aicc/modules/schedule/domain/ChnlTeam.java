package kr.co.aicc.modules.schedule.domain;

import kr.co.aicc.infra.common.domain.Common;
import lombok.*;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChnlTeam extends Common {
    private long chnlTeamNo;
    private long chnlNo;
    private long memNo;
    private String chnlNm;
    private String memId;
    private String memNm;
}
