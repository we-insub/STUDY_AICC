package kr.co.aicc.modules.dashboard.domain;

import lombok.*;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardInfo {
    private long memNo;
    private String memNm;
    private String memId;
    private int liveWork;
    private int reWork;
    private int adminWork;
    private int nosupotWork;
    private int filerWork;
    private int workTotal;
    private int restWork;
    private int eWork;
    private int restTotal;
}
