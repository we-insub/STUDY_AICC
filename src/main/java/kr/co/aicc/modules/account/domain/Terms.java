package kr.co.aicc.modules.account.domain;


import kr.co.aicc.infra.common.domain.Common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "tncNo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Terms extends Common{
	private Long tncNo;
	private String tncNm;
	private String tncDesc;
	private String tncGb;
	private String tncMandYn;
	private String dispYn;
	private String bgnDt;
	private String endDt;
	private String ord;	

	private Long memNo;
	private String agreeYn;
	private String agreeDt;
	
	private String ntncno;
	
}
