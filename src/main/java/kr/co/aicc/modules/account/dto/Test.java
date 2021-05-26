package kr.co.aicc.modules.account.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Test {
    @Size(min=5,max=11)
    @NotEmpty
    private String data2;
    @NotBlank
    private String data1;
}
