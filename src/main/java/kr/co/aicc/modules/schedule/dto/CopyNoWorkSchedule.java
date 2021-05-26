package kr.co.aicc.modules.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CopyNoWorkSchedule {
    @NotBlank
    private String toDay;
    @NotBlank
    private String chooseDay;
    private String isMem;
}
