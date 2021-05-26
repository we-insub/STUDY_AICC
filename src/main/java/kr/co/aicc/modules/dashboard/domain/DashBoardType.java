package kr.co.aicc.modules.dashboard.domain;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardType {
    private String typeCode;
    private String typeNm;
    private String kind;
    private int memberCnt;
}
