package kr.co.aicc.modules.schedule.domain;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WebposButtonExposure {
    private long schedNo;
    private boolean isWebposButton;
}
