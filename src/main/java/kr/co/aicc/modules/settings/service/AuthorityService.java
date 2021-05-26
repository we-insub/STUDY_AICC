package kr.co.aicc.modules.settings.service;

import java.util.List;

import kr.co.aicc.modules.settings.dto.RoleForm;

public interface AuthorityService {
	
    List<RoleForm> findRoleList(RoleForm roleForm);

    int chkRoleNm(RoleForm roleForm);

    int createTopRole(RoleForm roleForm);
    
    int createRole(RoleForm roleForm);
    
    int updateRole(RoleForm roleForm);

    int chkChildRoleYn(RoleForm roleForm);
    
    int chkDeleteRoleYn(RoleForm roleForm);
    
    int deleteRole(RoleForm roleForm);

}
