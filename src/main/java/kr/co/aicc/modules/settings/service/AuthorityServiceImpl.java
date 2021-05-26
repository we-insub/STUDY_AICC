package kr.co.aicc.modules.settings.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.aicc.modules.settings.dto.RoleForm;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final SettingsDao settingsDao;
    private final ModelMapper modelMapper;
	
	@Override
	public List<RoleForm> findRoleList(RoleForm roleForm) {
		return settingsDao.findRoleList(roleForm);
	}

	@Override
	public int chkRoleNm(RoleForm roleForm) {
		return settingsDao.chkRoleNm(roleForm);
	}

	@Override
	public int createRole(RoleForm roleForm) {
		return settingsDao.createRole(roleForm);
	}

	@Override
	public int updateRole(RoleForm roleForm) {
		return settingsDao.updateRole(roleForm);
	}

	@Override
	public int chkChildRoleYn(RoleForm roleForm) {
		return settingsDao.chkChildRoleYn(roleForm);
	}

	@Override
	public int chkDeleteRoleYn(RoleForm roleForm) {
		return settingsDao.chkDeleteRoleYn(roleForm);
	}

	@Override
	public int deleteRole(RoleForm roleForm) {
		settingsDao.deleteMemRole(roleForm);
		settingsDao.deleteResRole(roleForm);
		return settingsDao.deleteRole(roleForm);
	}

	@Override
	public int createTopRole(RoleForm roleForm) {
		return settingsDao.createTopRole(roleForm);
	}

}
