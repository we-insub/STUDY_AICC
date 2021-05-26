package kr.co.aicc.modules.schedule.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "memSchedNo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedExcelUpload {
    @NotBlank
    private String chnlNo;
    private MultipartFile schedExcel;
    private String toDay;
}
