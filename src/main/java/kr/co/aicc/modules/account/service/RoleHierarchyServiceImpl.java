package kr.co.aicc.modules.account.service;

import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.account.repository.AccountDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleHierarchyServiceImpl implements RoleHierarchyService {
    private final AccountDao accountDao;

    @Override
    public String findAllHierarchy() {

        List<Role> rolesHierarchy = accountDao.findAllRoleHierarchy();
        Iterator<Role> itr = rolesHierarchy.iterator();
        StringBuilder concatedRoles = new StringBuilder();
        while (itr.hasNext()) {
            Role role = itr.next();
            if (role.getPrntRole() != null) {
                concatedRoles.append(role.getPrntRole().getRoleNm());
                concatedRoles.append(" > ");
                concatedRoles.append(role.getRoleNm());
                concatedRoles.append("\n");
            }
        }
        log.info("=====> [findAllHierarchy] rolesHierarchy : {}", concatedRoles.toString());
        return concatedRoles.toString();
    }
}
