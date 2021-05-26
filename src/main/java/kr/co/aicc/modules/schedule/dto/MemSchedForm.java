package kr.co.aicc.modules.schedule.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class MemSchedForm {

    private String memSchedNo;
    private String schedNo;
    @NotEmpty(message = "{MemSchedForm.memNo.NotEmpty}")
    private String memNo;
    private String memNm;
    private String memId;
    @NotEmpty(message = "{MemSchedForm.startH.NotEmpty}")
    private String startH;
    @NotEmpty(message = "{MemSchedForm.startM.NotEmpty}")
    private String startM;
    @NotEmpty(message = "{MemSchedForm.endH.NotEmpty}")
    private String endH;
    @NotEmpty(message = "{MemSchedForm.endM.NotEmpty}")
    private String endM;
    @NotEmpty(message = "{MemSchedForm.schedGb.NotEmpty}")
    private String schedGb;
    private long chnlTeam;
    @NotEmpty(message = "{MemSchedForm.startMemAmt.NotEmpty}")
    private String startMemAmt;
    @NotEmpty(message = "{MemSchedForm.endMemAmt.NotEmpty}")
    private String endMemAmt;
    private String mode;
    private int ord;
    private LocalDate toDay;
    private LocalDate startDay;
    private LocalDate endDay;
    private String isWork; // 앞단에서 작업외 일정인지 작업일정인지 구분하기위함

}
