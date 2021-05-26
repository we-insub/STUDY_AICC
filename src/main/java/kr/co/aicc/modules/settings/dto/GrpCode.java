package kr.co.aicc.modules.settings.dto;

import kr.co.aicc.infra.common.domain.Common;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class GrpCode extends Common {

	private String rnum;
	private String grpGrpCd;
	private String grpGrpCdNm;
	private String grpDesc;
	private String grpUseYn;

	private String parmGrpGrpCd;
	private String[] parmGrpUseYn;
	private String parmGrpGrpCdNm;

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
