package kr.co.aicc.modules.settings.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.settings.domain.MemGrp;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class MemGrpDto extends Common {

	private String rnum;
	private String grpType;
	private String grpTypeNm;
	private String cdNm;
	private String cdDesc;
	private String lstChgDt;
	private String grpMembersString;
    private List<MemGrp> grpMembers = new ArrayList<>();

	private String pageType;

	private int memNo;
	private String memNm;
	private String bgnRegDt;
	private String memDisableYn;

    private int[] pMemNo;

	private String pGrpType;
	private String pMemNm;
	private String pBgnRegDt;
	
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
