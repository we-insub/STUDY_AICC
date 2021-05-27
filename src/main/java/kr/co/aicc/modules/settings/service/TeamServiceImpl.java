package kr.co.aicc.modules.settings.service;

import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.aicc.infra.config.AppProperties;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.settings.domain.MemGrp;
import kr.co.aicc.modules.settings.dto.ChnlTeamDto;
import kr.co.aicc.modules.settings.dto.CtDTO;
import kr.co.aicc.modules.settings.dto.MemGrpDto;
import kr.co.aicc.modules.settings.repository.SettingsDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
	
    private final SettingsDao settingsDao;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;
    
	@Override
	public List<MemGrpDto> findMemGrpList(MemGrpDto memGrpDto) {

		List<MemGrpDto> list = settingsDao.findMemGrpList(memGrpDto);
		
		for (int i=0; i<list.size(); i++) {
			
			Iterator<MemGrp> itr = list.get(i).getGrpMembers().iterator();
			String resRoles = "";
			while (itr.hasNext()) {
				if (resRoles == "") {
					resRoles = itr.next().getMemNm();
				} else {
					resRoles = resRoles + "," + itr.next().getMemNm();
				}				
			}
			list.get(i).setGrpMembersString(resRoles);
			
		}
		return list;
	}
	
	@Override
	public int findMemGrpListCnt(MemGrpDto memGrpDto) {
		return settingsDao.findMemGrpListCnt(memGrpDto);
	}

	@Override
	public List<MemGrpDto> findMemList(MemGrpDto memGrpDto) {
		return settingsDao.findMemList(memGrpDto);
	}

	@Override
	public int findMemListCnt(MemGrpDto memGrpDto) {
		return settingsDao.findMemListCnt(memGrpDto);
	}

	@Override
	public List<MemGrpDto> getGrpMemInfo(MemGrpDto memGrpDto) {
		return settingsDao.getGrpMemInfo(memGrpDto);
	}

	@Override
	public int createGrpMem(MemGrpDto memGrpDto) {
		
		int result = 0;
		
		settingsDao.deleteGrpMem(memGrpDto);
		
		for (int i=0; i<memGrpDto.getPMemNo().length; i ++) {
			MemGrp memGrp = new MemGrp();
			memGrp.setMemNo(memGrpDto.getPMemNo()[i]);
			memGrp.setGrpType(memGrpDto.getGrpType());
			memGrp.setCretr(memGrpDto.getCretr());
			memGrp.setChgr(memGrpDto.getChgr());
			result += settingsDao.createGrpMem(memGrp);
		}
		
		return result;
	}

	@Override
	public List<ChnlTeamDto> findChnlTeamList(ChnlTeamDto chnlTeamDto) {

		List<ChnlTeamDto> list = settingsDao.findChnlTeamList(chnlTeamDto);
		
		for (int i=0; i<list.size(); i++) {
			
			Iterator<ChnlTeam> itr = list.get(i).getChnlMembers().iterator();
			String resRoles = "";
			while (itr.hasNext()) {
				if (resRoles == "") {
					resRoles = itr.next().getMemNm();
				} else {
					resRoles = resRoles + "," + itr.next().getMemNm();
				}				
			}
			list.get(i).setChnlMembersString(resRoles);

    		if (!"".equals(list.get(i).getThumbSysFileNm()) && list.get(i).getThumbSysFileNm() != null) {
    			String path = list.get(i).getFilePath().replace(appProperties.getUploadDirChannel(), "/file/image/channel");
    			list.get(i).setThumbSysFileNm(path + "/" + list.get(i).getThumbSysFileNm());
    		}
		}
		return list;
	}

	@Override
	public int findChnlTeamListCnt(ChnlTeamDto chnlTeamDto) {
		return settingsDao.findChnlTeamListCnt(chnlTeamDto);
	}

	@Override
	public int createChnlTeam(ChnlTeamDto chnlTeamDto) {
		
		int result = 0;
		
		result += settingsDao.deleteChnlTeam(chnlTeamDto);
		
		for (int i=0; i<chnlTeamDto.getPMemNo().length; i ++) {
			ChnlTeam chnlTeam = new ChnlTeam();
			chnlTeam.setMemNo(chnlTeamDto.getPMemNo()[i]);
			chnlTeam.setChnlNo(chnlTeamDto.getChnlNo());
			chnlTeam.setCretr(chnlTeamDto.getCretr());
			chnlTeam.setChgr(chnlTeamDto.getChgr());
			result += settingsDao.createChnlTeam(chnlTeam);
		}
		
		return result;
	}



	
	
}
