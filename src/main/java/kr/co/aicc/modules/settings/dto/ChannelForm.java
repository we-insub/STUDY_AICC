package kr.co.aicc.modules.settings.dto;

import org.springframework.web.multipart.MultipartFile;

import kr.co.aicc.infra.common.domain.Common;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class ChannelForm extends Common {

	private String rnum;
    private Long chnlNo; // 채널 번호
    private String chnlNm; // 채널 명
    private String chnlDesc; // 채널 설명
    private String linkUrl; // 채널 링크
    private String chnlImgData; // 채널이미지데이터
    private String trnsIp; // 송출ip
    private String trnsPort; // 송출port
    private String cretDtString;

    private MultipartFile picFile;
    private String fileNm;
    private String sysFileNm;
    private String thumbSysFileNm;
    private String filePath;
    private String fileSize;
    
	private int pageNo = 1;
	private int pageRowCont = 10;
    @Getter(AccessLevel.NONE)
	private int startIdx;
	@Getter(AccessLevel.NONE)
	private int endIdx;
	
	public int getStartIdx() {
		return (pageNo-1)*pageRowCont +1;
	}

	public int getEndIdx() {
		return pageNo * pageRowCont;
	}
}
