package kr.co.aicc.modules.settings.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.aicc.infra.common.domain.Common;
import kr.co.aicc.modules.account.domain.Role;
import lombok.Data;

@Data
public class ResourceDto extends Common {

    private Long resNo;
    private Long prntResNo;
    private String useYn;
    private String dispYn;
    private String resGb;
    private String resNm;
    private String resDesc;
    private String resType;
    private String resMeth;
    private String resUrl;
    private String resLvl;
    private int ord;
    private int resLvlInt;
    
    /* 메뉴 순번 변경 */
    private Long resNoA;
    private int ordA;
    private Long resNoB;
    private int ordB;
    
    private String role;
    private Long roleNo;
    private String resRolesString;
    private List<Role> resRoles = new ArrayList<>();
    
    private String childYn;
    private String icon;
}
