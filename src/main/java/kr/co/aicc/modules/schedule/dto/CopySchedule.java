package kr.co.aicc.modules.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CopySchedule {
    @NotNull
    private long chnlNo;
    @NotNull
    private String toDay;
    @NotNull
    private String chooseDay;
    private String isMem;
}
