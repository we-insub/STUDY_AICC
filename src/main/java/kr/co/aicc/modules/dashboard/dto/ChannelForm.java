package kr.co.aicc.modules.dashboard.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ChannelForm {
    @NotBlank
    private String chnlNm;
    @NotBlank
    private String chnlDesc;
    @NotBlank
    private String linkUrl;
}
