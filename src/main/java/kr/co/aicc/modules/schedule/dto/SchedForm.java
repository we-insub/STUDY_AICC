package kr.co.aicc.modules.schedule.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class SchedForm {

    @NotNull
    private int chnlNo;
    @NotBlank
    private String chnlNm;
    @NotBlank(message = "{SchedForm.progNm.NotBlank}")
    private String progNm;
    private int schedNo;
    @NotBlank(message = "{SchedForm.schedType.NotBlank}")
    private String schedType;
    @NotEmpty(message = "{SchedForm.startH.NotEmpty}")
    private String startH;
    @NotEmpty(message = "{SchedForm.startM.NotEmpty}")
    private String startM;
    @NotEmpty(message = "{SchedForm.endH.NotEmpty}")
    private String endH;
    @NotEmpty(message = "{SchedForm.endM.NotEmpty}")
    private String endM;
    private LocalDate toDay;
    private LocalDate startDay;
    private LocalDate endDay;
    private String chooseDay;
    private String mode;
    private int diffStart;
}
