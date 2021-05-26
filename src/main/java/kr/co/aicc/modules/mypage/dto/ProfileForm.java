package kr.co.aicc.modules.mypage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileForm {
	
	private String menuNo;
	
	private Long memNo;
	
    private String memId;
    private String memIdF;
    private String memIdL;

    @Pattern(regexp = "[가-힣a-zA-Z]{2,20}", message = "{ProfileForm.memNm.Pattern}")
    private String memNm;

//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "{ProfileForm.passwd.Pattern}")
    private String passwd;

    private String passwd2;

    @Pattern(regexp = "[0-9]{4,4}+\\-[0-9]{2,2}+\\-[0-9]{2,2}", message = "{ProfileForm.brthday.Pattern}")
    private String brthday;
    
    private String brthdayY;
    private String brthdayM;
    private String brthdayD;

    @NotBlank(message = "{ProfileForm.sex.NotBlank}")
    private String sex;

    private String ptblTelNo;

    @NotBlank(message = "{ProfileForm.ptblTelNoF.NotBlank}")
    private String ptblTelNoF;

    @Pattern(regexp = "[0-9]{4,4}", message = "{ProfileForm.ptblTelNoM.Pattern}")
    private String ptblTelNoM;

    @Pattern(regexp = "[0-9]{4,4}", message = "{ProfileForm.ptblTelNoL.Pattern}")
    private String ptblTelNoL;

    @Pattern(regexp = "[0-9]{2,6}", message = "{ProfileForm.zipNo.Pattern}")
    private String zipNo;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}", message = "{ProfileForm.baseAddr.Pattern}")
    private String baseAddr;
    
    @Pattern(regexp = "[가-힣a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\|\\[\\]\\{\\}\\'\\:\\;\\/\\?\\<\\>\\.\\,\\~\\`\\s\\\\]{1,100}", message = "{ProfileForm.dtlAddr.Pattern}")
    private String dtlAddr;

    private String lastEdu;

    @NotBlank
    private String useModel;

    @Size(min=0, max=500, message = "{ProfileForm.careerDesc.Pattern}")
    private String careerDesc;
    
    private MultipartFile picFile;
    private String fileNm;
    private String sysFileNm;
    private String thumbSysFileNm;
    private String filePath;
    private String fileSize;
    private String lastEduNm;
    private String useModelNm;

	private String passChgYn;
    
    private String fileData;
}
