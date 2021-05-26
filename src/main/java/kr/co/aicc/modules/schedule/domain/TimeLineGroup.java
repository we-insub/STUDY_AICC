package kr.co.aicc.modules.schedule.domain;


import kr.co.aicc.modules.dashboard.domain.Channel;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineGroup {
    private String type; // 작업자 작업 타입 W : 작업 E : 식사 R : 휴식
    private List<Channel> programs;
}
