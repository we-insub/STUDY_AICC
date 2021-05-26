package kr.co.aicc.modules.settings.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class ChnlTeamDto extends Common {
	
	private String rnum;
	private int chnlNo;
	private String chnlNm;
	private String lstChgDt;
	private String chnlMembersString;
	private String chnlImgData;
    private String fileNm;
    private String sysFileNm;
    private String thumbSysFileNm;
    private String filePath;
    private String fileSize;
    private List<ChnlTeam> chnlMembers = new ArrayList<>();

    private int[] pMemNo;

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
