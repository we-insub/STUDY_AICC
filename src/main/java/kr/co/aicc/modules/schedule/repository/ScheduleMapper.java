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
 * @package kr.co.aicc.modules.schedule.repository
 * @file ScheduleMapper.java
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
package kr.co.aicc.modules.schedule.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.*;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.schedule.domain.GroupTeam;
import kr.co.aicc.modules.schedule.domain.ScheduleFile;
import kr.co.aicc.modules.schedule.domain.TimeLine;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface ScheduleMapper {
    int insertMember(Account account);

    Account findByMemId(String memId);

    int insertSchedule(Schedule schedule);

    List<Account> selectMemberList();

    int insertMemScheduleList(List<MemberSchedule> memSchList);

    TimeLine selectTimeLineData(TimeLine pTimeLine);

    List<Channel> selectChannelList(HashMap<String, Object> map);

    int updateDelschedByChnlNo(Schedule schedule);

    int updateDelMemSchedByChnlNo(Schedule schedule);

    List<Schedule> selectScheduleList(Schedule schedule);

    void insertScheduleAsList(List<Schedule> scheduleList);

    void insertScheduleFileHistory(ScheduleFile scheduleFile);

    List<ScheduleFile> selectFileHistory(ScheduleFile scheduleFile);

    ScheduleFile selectScheduleFile(long refNo);

    List<ChnlTeam> selectChnlTeamListAll();

    List<ChnlTeam> selectChnlTeamMemList(ChnlTeam chnlTeam);

    void insertMemberSchedule(MemberSchedule memberSchedule);

    List<MemberSchedule> selectMemberScheduleList(MemberSchedule memberSchedule);

    void updateMemberSchedule(MemberSchedule memberSchedule);

    void updateDelMemSchedByMemSchedNo(@Param("memSchedNo") long memSchedNo, @Param("memNo") long memNo);

    void updateSchedule(Schedule schedule);

    Schedule selectScheduleBySchedNo(int schedNo);

    List<Channel> selectChannelAllList();

    List<Schedule> selectWebfosButtonScheduleList(Schedule schedule);

    MemberSchedule selectMemberSchedule(MemberSchedule memberSchedule);

    List<GroupTeam> selectGroupTeamListAll();

    List<ChnlTeam> selectGroupTeamMemList(GroupTeam groupTeam);

    List<Channel> selectTotalChnlList(Account account);

    List<DashBoardInfo> selectWorkDetailList(DashBoard dashBoard);

    SubtitleDetailInfo selectSubDtlInfo(long schedNo);

}
