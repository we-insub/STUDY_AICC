package kr.co.aicc.modules.dashboard.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class DashBoardForm {
    private String dashBoardType;
    private Set<String> chnl;
    private Set<String> groupCode;
    private String startTime;
    private String endTime;
}
