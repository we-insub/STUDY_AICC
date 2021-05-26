package kr.co.aicc.modules.dashboard.domain;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.webfos.dto.ProgramInfo;
import lombok.*;
import java.util.*;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Channel extends Common {
    private long chnlNo; // 채널 번호
    private String chnlNm; // 채널 명
    private String chnlDesc; // 채널 설명
    private String linkUrl; // 채널 링크
    private String chnlImgData; // 채널이미지데이터
    private String trnsIp; // 송출ip
    private String trnsPort; // 송출port
    private List<Schedule> scheduleList; // 프로그램 리스트
    private Schedule schedule; // 프로그램
    private List<ChnlTeam> chnlTeams;
    private String filePath;
    private String sysFileNm;
    private String thumbSysFileNm;

    public void setWebfosButtonDisplay(List<ProgramInfo> programInfos, Account account) {
        //this.scheduleList
        for (Schedule schedule : this.scheduleList) {
            schedule.setWebfosButtonDisplayByProgramInfos(programInfos, account);
        }
    }

}
