package kr.co.aicc.modules.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChnlTeamMemForm {
    @NotBlank
    private long chnlNo;
}
