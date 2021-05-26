package kr.co.aicc.modules.schedule.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class RegProgramForm {

    private String chnlNo;
    private String chnlNm;
    private String programName;
    private String programType;
    private String startH;
    private String startM;
    private String endH;
    private String endM;
    private String toDay;
    private String chooseDay;
    private String fileNo;
    private String memNo;
    private String isMem;
    private MultipartFile schedExcel;

}
