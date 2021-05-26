package kr.co.aicc.modules.settings.dto;

import kr.co.aicc.infra.common.domain.Common;
import lombok.Data;

@Data
public class RoleForm extends Common {

    private Long roleNo;
    private Long prntRoleNo;
    private String prntRoleNm;
    private String roleNm;
    private String useYn;
    private String roleDesc;
    private String lv;
    private String childYn;
}
