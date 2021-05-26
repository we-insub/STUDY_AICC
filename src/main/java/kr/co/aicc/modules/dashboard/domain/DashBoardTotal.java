package kr.co.aicc.modules.dashboard.domain;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardTotal {

    private int workTimeAmt; // 총작업시간
    private int liveWorkTImeAmt; // 총 생방송 작업시간
    private int reWorkTimeAmt; // 총 재방송 작업시간
    private int adminWorkTimeAmt; // 총 관리자지원작업 시간
    private int noSuppotWorkTimeAmt; // 총 미지원재방 작업 시간
    private int noWorkTimeAmt; // 총 작업외 시간
    private int restTimeAmt; // 총 휴식 시간
    private int mealTimeAmt; // 총 식사 시간

}
