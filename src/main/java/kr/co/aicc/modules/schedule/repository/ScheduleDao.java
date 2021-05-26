package kr.co.aicc.modules.schedule.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.*;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.schedule.domain.GroupTeam;
import kr.co.aicc.modules.schedule.domain.ScheduleFile;
import kr.co.aicc.modules.schedule.domain.TimeLine;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ScheduleDao {
    public int insertTestSchData();

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

    void updateDelMemSchedByMemSchedNo(long memSchedNo, long memNo);

    void updateSchedule(Schedule schedule);

    Schedule selectScheduleBySchedNo(int parseInt);

    List<Channel> selectChannelAllList();

    List<Schedule> selectWebfosButtonScheduleList(Schedule schedule);

    MemberSchedule selectMemberSchedule(MemberSchedule memberSchedule);

    List<GroupTeam> selectGroupTeamListAll();

    List<ChnlTeam> selectGroupTeamMemList(GroupTeam groupTeam);

    List<Channel> selectTotalChnlList(Account account);

    List<DashBoardInfo> selectWorkDetailList(DashBoard dashBoard);

    SubtitleDetailInfo selectSubDtlInfo(long schedNo);
}
