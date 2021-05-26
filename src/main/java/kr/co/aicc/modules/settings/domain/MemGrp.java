package kr.co.aicc.modules.settings.domain;

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
@EqualsAndHashCode(of = "memGrpNo")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemGrp extends Common {

	private int memGrpNo;
	private String grpType;
	private int memNo;
	private String memNm;
}
