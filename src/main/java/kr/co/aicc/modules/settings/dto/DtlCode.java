package kr.co.aicc.modules.settings.dto;

import kr.co.aicc.infra.common.domain.Common;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class DtlCode extends Common {

	private String rnum;
	private String dtlGrpCd;
	private String dtlGrpCdNm;
	private String cd;
	private String cdNm;
	private String cdVal1;
	private String cdVal2;
	private String cdVal3;
	private int ord;
	private String dtlUseYn;
	private String cdDesc;

	private String parmDtlGrpCd;
	private String[] parmDtlUseYn;
	private String parmCdNm;

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
