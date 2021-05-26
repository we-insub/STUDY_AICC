/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.modules.settings.repository
 * @file SettingsDaoImpl.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 08	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.settings.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.account.domain.Role;
import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.settings.domain.MemGrp;
import kr.co.aicc.modules.settings.dto.ChannelForm;
import kr.co.aicc.modules.settings.dto.ChnlTeamDto;
import kr.co.aicc.modules.settings.dto.DtlCode;
import kr.co.aicc.modules.settings.dto.GrpCode;
import kr.co.aicc.modules.settings.dto.MemGrpDto;
import kr.co.aicc.modules.settings.dto.MemberForm;
import kr.co.aicc.modules.settings.dto.ResourceDto;
import kr.co.aicc.modules.settings.dto.RoleForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SettingsDaoImpl implements SettingsDao {
    private final SettingsMapper settingsMapper;


    @Override
    public List<ChannelForm> getChannelList(ChannelForm channelForm) {
        return settingsMapper.getChannelList(channelForm);
    }

	@Override
	public int getChannelListCnt(ChannelForm channelForm) {
		return settingsMapper.getChannelListCnt(channelForm);
	}

	@Override
	public int createChannel(ChannelForm channelForm) {
		return settingsMapper.createChannel(channelForm);
	}

	@Override
	public int updateChannel(ChannelForm channelForm) {
		return settingsMapper.updateChannel(channelForm);
	}

	@Override
	public int deleteChannel(ChannelForm channelForm) {
		return settingsMapper.deleteChannel(channelForm);
	}

    @Override
    public Schedule selectMemberSchedule(Schedule schedule) {
        return settingsMapper.selectMemberSchedule(schedule);
    }

	@Override
	public List<MemberForm> selectMemList(MemberForm memberForm) {
		return settingsMapper.selectMemList(memberForm);
	}

	@Override
	public int updateMember(Account account) {
		return settingsMapper.updateMember(account);
	}

	@Override
	public int createRoleByMemNo(Role role) {
		return settingsMapper.createRoleByMemNo(role);
	}

	@Override
	public int deleteRoleByMemNo(Long memNo) {
		return settingsMapper.deleteRoleByMemNo(memNo);
	}

	@Override
	public List<GrpCode> findCodeGrpList(GrpCode grpCode) {
		return settingsMapper.findCodeGrpList(grpCode);
	}

	@Override
	public List<DtlCode> findCodeDtlList(DtlCode dtlCode) {
		return settingsMapper.findCodeDtlList(dtlCode);
	}

	@Override
	public int createCodeGrp(GrpCode grpCode) {
		return settingsMapper.createCodeGrp(grpCode);
	}

	@Override
	public int createCodeDtl(DtlCode dtlCode) {
		return settingsMapper.createCodeDtl(dtlCode);
	}

	@Override
	public int updateCodeGrp(GrpCode grpCode) {
		return settingsMapper.updateCodeGrp(grpCode);
	}

	@Override
	public int updateCodeDtl(DtlCode dtlCode) {
		return settingsMapper.updateCodeDtl(dtlCode);
	}

	@Override
	public int deleteCodeGrp(GrpCode grpCode) {
		return settingsMapper.deleteCodeGrp(grpCode);
	}

	@Override
	public int deleteCodeDtl(DtlCode dtlCode) {
		return settingsMapper.deleteCodeDtl(dtlCode);
	}

	@Override
	public int selectMemListCnt(MemberForm memberForm) {
		return settingsMapper.selectMemListCnt(memberForm);
	}

	@Override
	public int findCodeGrpListCnt(GrpCode grpCode) {
		return settingsMapper.findCodeGrpListCnt(grpCode);
	}

	@Override
	public int findCodeDtlListCnt(DtlCode dtlCode) {
		return settingsMapper.findCodeDtlListCnt(dtlCode);
	}

	@Override
	public List<RoleForm> findRoleList(RoleForm roleForm) {
		return settingsMapper.findRoleList(roleForm);
	}

	@Override
	public int chkRoleNm(RoleForm roleForm) {
		return settingsMapper.chkRoleNm(roleForm);
	}

	@Override
	public int updateRole(RoleForm roleForm) {
		return settingsMapper.updateRole(roleForm);
	}

	@Override
	public int deleteRole(RoleForm roleForm) {
		return settingsMapper.deleteRole(roleForm);
	}

	@Override
	public int createRole(RoleForm roleForm) {
		return settingsMapper.createRole(roleForm);
	}

	@Override
	public int deleteMember(MemberForm memberForm) {
		return settingsMapper.deleteMember(memberForm);
	}

	@Override
	public int chkChildRoleYn(RoleForm roleForm) {
		return settingsMapper.chkChildRoleYn(roleForm);
	}
	
	@Override
	public int chkDeleteRoleYn(RoleForm roleForm) {
		return settingsMapper.chkDeleteRoleYn(roleForm);
	}

	@Override
	public List<ResourceDto> findResList(ResourceDto resourceDto) {
		return settingsMapper.findResList(resourceDto);
	}

	@Override
	public int createRes(ResourceDto resourceDto) {
		return settingsMapper.createRes(resourceDto);
	}

	@Override
	public int updateRes(ResourceDto resourceDto) {
		return settingsMapper.updateRes(resourceDto);
	}

	@Override
	public int createRoleByResNo(Role role) {
		return settingsMapper.createRoleByResNo(role);
	}

	@Override
	public int deleteRoleByResNo(Long resNo) {
		return settingsMapper.deleteRoleByResNo(resNo);
	}

	@Override
	public int findDeleteResYn(ResourceDto resourceDto) {
		return settingsMapper.findDeleteResYn(resourceDto);
	}

	@Override
	public int deleteRes(ResourceDto resourceDto) {
		return settingsMapper.deleteRes(resourceDto);
	}

	@Override
	public int deleteMemRole(RoleForm roleForm) {
		return settingsMapper.deleteMemRole(roleForm);
	}

	@Override
	public int deleteResRole(RoleForm roleForm) {
		return settingsMapper.deleteResRole(roleForm);
	}

	@Override
	public int deleteCodeGrpByDtl(GrpCode grpCode) {
		return settingsMapper.deleteCodeGrpByDtl(grpCode);
	}

	@Override
	public Long getResNo(ResourceDto resourceDto) {
		return settingsMapper.getResNo(resourceDto);
	}

	@Override
	public int deleteResByChild(ResourceDto resourceDto) {
		return settingsMapper.deleteResByChild(resourceDto);
	}

	@Override
	public int getResLvl(ResourceDto resourceDto) {
		return settingsMapper.getResLvl(resourceDto);
	}

	@Override
	public int updateResOrd(ResourceDto resourceDto) {
		return settingsMapper.updateResOrd(resourceDto);
	}

	@Override
	public ResourceDto getUpChgResInfo(ResourceDto resourceDto) {
		return settingsMapper.getUpChgResInfo(resourceDto);
	}

	@Override
	public ResourceDto getDownChgResInfo(ResourceDto resourceDto) {
		return settingsMapper.getDownChgResInfo(resourceDto);
	}

	@Override
	public List<MemGrpDto> findMemGrpList(MemGrpDto memGrpDto) {
		return settingsMapper.findMemGrpList(memGrpDto);
	}

	@Override
	public int findMemGrpListCnt(MemGrpDto memGrpDto) {
		return settingsMapper.findMemGrpListCnt(memGrpDto);
	}

	@Override
	public List<MemGrpDto> findMemList(MemGrpDto memGrpDto) {
		return settingsMapper.findMemList(memGrpDto);
	}

	@Override
	public int findMemListCnt(MemGrpDto memGrpDto) {
		return settingsMapper.findMemListCnt(memGrpDto);
	}

	@Override
	public List<MemGrpDto> getGrpMemInfo(MemGrpDto memGrpDto) {
		return settingsMapper.getGrpMemInfo(memGrpDto);
	}

	@Override
	public int deleteGrpMem(MemGrpDto memGrpDto) {
		return settingsMapper.deleteGrpMem(memGrpDto);
	}

	@Override
	public int createGrpMem(MemGrp memGrp) {
		return settingsMapper.createGrpMem(memGrp);
	}

	@Override
	public List<ChnlTeamDto> findChnlTeamList(ChnlTeamDto chnlTeamDto) {
		return settingsMapper.findChnlTeamList(chnlTeamDto);
	}

	@Override
	public int findChnlTeamListCnt(ChnlTeamDto chnlTeamDto) {
		return settingsMapper.findChnlTeamListCnt(chnlTeamDto);
	}

	@Override
	public int deleteChnlTeam(ChnlTeamDto chnlTeamDto) {
		return settingsMapper.deleteChnlTeam(chnlTeamDto);
	}

	@Override
	public int createChnlTeam(ChnlTeam chnlTeam) {
		return settingsMapper.createChnlTeam(chnlTeam);
	}

	@Override
	public int updateChnlFileInfo(ChannelForm channelForm) {
		return settingsMapper.updateChnlFileInfo(channelForm);
	}

	@Override
	public Long getChnlNo(ChannelForm channelForm) {
		return settingsMapper.getChnlNo(channelForm);
	}

	@Override
	public int deleteRoleByChildResNo(Long resNo) {
		return settingsMapper.deleteRoleByChildResNo(resNo);
	}

	@Override
	public int createTopRole(RoleForm roleForm) {
		return settingsMapper.createTopRole(roleForm);
	}
}
