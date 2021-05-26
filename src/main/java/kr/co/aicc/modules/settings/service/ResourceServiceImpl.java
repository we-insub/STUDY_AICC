package kr.co.aicc.modules.settings.service;

import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.settings.dto.ResourceDto;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final SettingsDao settingsDao;
    private final ModelMapper modelMapper;
	@Override
	public List<ResourceDto> findResList(ResourceDto resourceDto) {

		List<ResourceDto> resList = settingsDao.findResList(resourceDto);
		
		for (int i=0; i<resList.size(); i++) {
			
			Iterator<Role> itr = resList.get(i).getResRoles().iterator();
			String resRoles = "";
			while (itr.hasNext()) {
				if (resRoles == "") {
					resRoles = itr.next().getRoleNm();
				} else {
					resRoles = resRoles + "," + itr.next().getRoleNm();
				}				
			}
			resList.get(i).setResRolesString(resRoles);
			
		}
		return resList;
	}
	
	@Override
	public int createRes(ResourceDto resourceDto) {

		resourceDto.setUseYn("Y");
		resourceDto.setResGb("CC");
		resourceDto.setResDesc(resourceDto.getResNm());
		resourceDto.setResType("URL");
		if (resourceDto.getPrntResNo() == null) {
			resourceDto.setResLvlInt(1);
		} else {
			resourceDto.setResLvlInt(2);
		}
		
        int result = settingsDao.createRes(resourceDto);
		Long resNo = settingsDao.getResNo(resourceDto);

        String[] resRoles = resourceDto.getRole().split(",");
        
        if (result > 0) {

            for (int i=0; i<resRoles.length; i++) {
                Role role = new Role();
                role.setRoleNm(resRoles[i]);
                role.setResNo(resNo);
                role.setCretr(resourceDto.getCretr());
                role.setChgr(resourceDto.getChgr());

                settingsDao.createRoleByResNo(role);
            }
        }
        
		return result;
	}
	
	@Override
	public int updateRes(ResourceDto resourceDto) {
        
        String[] resRoles = resourceDto.getRole().split(",");

        int result = settingsDao.updateRes(resourceDto);
        
        if (result > 0) {
            settingsDao.deleteRoleByResNo(resourceDto.getResNo());
            
            for (int i=0; i<resRoles.length; i++) {
                Role role = new Role(); 
                role.setRoleNm(resRoles[i]);
                role.setResNo(resourceDto.getResNo());
                role.setCretr(resourceDto.getCretr());
                role.setChgr(resourceDto.getChgr());

                settingsDao.createRoleByResNo(role);
            }        	
        }
        
		return result;
	}
	
	@Override
	public int findDeleteResYn(ResourceDto resourceDto) {
		return settingsDao.findDeleteResYn(resourceDto);
	}
	
	@Override
	public int deleteRes(ResourceDto resourceDto) {

        settingsDao.deleteRoleByResNo(resourceDto.getResNo());
        
		int result = settingsDao.deleteRes(resourceDto);
		if (result > 0) {
	        settingsDao.deleteRoleByChildResNo(resourceDto.getResNo());
			settingsDao.deleteResByChild(resourceDto);
		}
		
		return result;
	}

	@Override
	public int resOrdUp(ResourceDto resourceDto) {
		int getResLvl = settingsDao.getResLvl(resourceDto);
		
		resourceDto.setResLvlInt(getResLvl);
		
		ResourceDto upChgResInfo = settingsDao.getUpChgResInfo(resourceDto);

		log.debug("upChgResInfo ::" + upChgResInfo);
		if (upChgResInfo == null) {
			log.debug("선택한 메뉴가 최상위 메뉴 일때");
			
			return 0;
		} else {
			// 현재 선택된 메뉴 A / 바뀔 메뉴 B
			// A메뉴에 B순번 입력
			resourceDto.setResNo(upChgResInfo.getResNoA());
			resourceDto.setOrd(upChgResInfo.getOrdB());
			settingsDao.updateResOrd(resourceDto);

			// B메뉴에 A순번 입력
			resourceDto.setResNo(upChgResInfo.getResNoB());
			resourceDto.setOrd(upChgResInfo.getOrdA());
			settingsDao.updateResOrd(resourceDto);
			
			return 1;	
		}
		
	}

	@Override
	public int resOrdDown(ResourceDto resourceDto) {
		int getResLvl = settingsDao.getResLvl(resourceDto);
		
		resourceDto.setResLvlInt(getResLvl);
		
		ResourceDto downChgResInfo = settingsDao.getDownChgResInfo(resourceDto);

		log.debug("downChgResInfo ::" + downChgResInfo);
		if (downChgResInfo == null) {
			log.debug("선택한 메뉴가 최하위 메뉴 일때");
			
			return 0;
		} else {
			// 현재 선택된 메뉴 A / 바뀔 메뉴 B
			// A메뉴에 B순번 입력
			resourceDto.setResNo(downChgResInfo.getResNoA());
			resourceDto.setOrd(downChgResInfo.getOrdB());
			settingsDao.updateResOrd(resourceDto);

			// B메뉴에 A순번 입력
			resourceDto.setResNo(downChgResInfo.getResNoB());
			resourceDto.setOrd(downChgResInfo.getOrdA());
			settingsDao.updateResOrd(resourceDto);
			
			return 1;
		}
	}
    
}
