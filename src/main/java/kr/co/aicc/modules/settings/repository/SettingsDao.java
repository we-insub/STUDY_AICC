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
 * @file SettingsDao.java
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

import java.util.*;

public interface SettingsDao {
    List<ChannelForm> getChannelList(ChannelForm channelForm);
    int getChannelListCnt(ChannelForm channelForm);
    Long getChnlNo(ChannelForm channelForm);
    int createChannel(ChannelForm channelForm);
    int updateChannel(ChannelForm channelForm);
    int deleteChannel(ChannelForm channelForm);
    int updateChnlFileInfo(ChannelForm channelForm);
    Schedule selectMemberSchedule(Schedule schedule);
    
    List<MemGrpDto> findMemGrpList(MemGrpDto memGrpDto);
    int findMemGrpListCnt(MemGrpDto memGrpDto);
    List<MemGrpDto> findMemList(MemGrpDto memGrpDto);
    int findMemListCnt(MemGrpDto memGrpDto);
    List<MemGrpDto> getGrpMemInfo(MemGrpDto memGrpDto);
    int deleteGrpMem(MemGrpDto memGrpDto);
    int createGrpMem(MemGrp memGrp);
    List<ChnlTeamDto> findChnlTeamList(ChnlTeamDto chnlTeamDto);
    int findChnlTeamListCnt(ChnlTeamDto chnlTeamDto);
    int deleteChnlTeam(ChnlTeamDto chnlTeamDto);
    int createChnlTeam(ChnlTeam chnlTeam);
    
    
    List<MemberForm> selectMemList(MemberForm memberForm);
    int chkRoleNm(RoleForm roleForm);
    int selectMemListCnt(MemberForm memberForm);
    int updateMember(Account account);
    int deleteMember(MemberForm memberForm);
    int createRoleByMemNo(Role role);
    int deleteRoleByMemNo(Long memNo);

    List<GrpCode> findCodeGrpList(GrpCode grpCode);
    int findCodeGrpListCnt(GrpCode grpCode);
    List<DtlCode> findCodeDtlList(DtlCode dtlCode);
    int findCodeDtlListCnt(DtlCode dtlCode);
    int createCodeGrp(GrpCode grpCode);
    int createCodeDtl(DtlCode dtlCode);
    int updateCodeGrp(GrpCode grpCode);
    int updateCodeDtl(DtlCode dtlCode);
    int deleteCodeGrp(GrpCode grpCode);
    int deleteCodeGrpByDtl(GrpCode grpCode);
    int deleteCodeDtl(DtlCode dtlCode);
    
    List<RoleForm> findRoleList(RoleForm roleForm);
    int createTopRole(RoleForm roleForm);
    int createRole(RoleForm roleForm);
    int updateRole(RoleForm roleForm);
    int chkChildRoleYn(RoleForm roleForm);
    int chkDeleteRoleYn(RoleForm roleForm);
    int deleteMemRole(RoleForm roleForm);
    int deleteResRole(RoleForm roleForm);    
    int deleteRole(RoleForm roleForm);

    List<ResourceDto> findResList(ResourceDto resourceDto);
    int getResLvl(ResourceDto resourceDto);
    ResourceDto getUpChgResInfo(ResourceDto resourceDto);
    ResourceDto getDownChgResInfo(ResourceDto resourceDto);
    int updateResOrd(ResourceDto resourceDto);
    int createRes(ResourceDto resourceDto);
    Long getResNo(ResourceDto resourceDto);
    int updateRes(ResourceDto resourceDto);
    int createRoleByResNo(Role role);
    int deleteRoleByResNo(Long resNo);
    int findDeleteResYn(ResourceDto resourceDto);
    int deleteRes(ResourceDto resourceDto);
    int deleteRoleByChildResNo(Long resNo);
    int deleteResByChild(ResourceDto resourceDto);
}
