package kr.co.aicc.modules.schedule.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkDetail {
    private long chnlNo;
    private LocalDate toDay;
}
