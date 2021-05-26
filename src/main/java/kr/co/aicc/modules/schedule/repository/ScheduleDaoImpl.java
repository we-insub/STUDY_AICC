package kr.co.aicc.modules.schedule.repository;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.*;
import kr.co.aicc.modules.schedule.domain.ChnlTeam;
import kr.co.aicc.modules.schedule.domain.GroupTeam;
import kr.co.aicc.modules.schedule.domain.ScheduleFile;
import kr.co.aicc.modules.schedule.domain.TimeLine;
import kr.co.aicc.modules.webfos.dto.SubtitleDetailInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleDaoImpl implements ScheduleDao {

    private final ScheduleMapper scheduleMapper;

    @Override
    public int insertTestSchData() {
        return 0;
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        return scheduleMapper.insertSchedule(schedule);
    }

    @Override
    public List<Account> selectMemberList() {
        return scheduleMapper.selectMemberList();
    }

    @Override
    public int insertMemScheduleList(List<MemberSchedule> memSchList) {
        return scheduleMapper.insertMemScheduleList(memSchList);
    }

    @Override
    public TimeLine selectTimeLineData(TimeLine pTimeLine) {
        return scheduleMapper.selectTimeLineData(pTimeLine);
    }

    @Override
    public List<Channel> selectChannelList(HashMap<String, Object> map) {
        return scheduleMapper.selectChannelList(map);
    }

    @Override
    public int updateDelschedByChnlNo(Schedule schedule) {
        return scheduleMapper.updateDelschedByChnlNo(schedule);
    }

    @Override
    public int updateDelMemSchedByChnlNo(Schedule schedule) {
        return scheduleMapper.updateDelMemSchedByChnlNo(schedule);
    }

    @Override
    public List<Schedule> selectScheduleList(Schedule schedule) {
        return scheduleMapper.selectScheduleList(schedule);
    }

    @Override
    public void insertScheduleAsList(List<Schedule> scheduleList) {
        scheduleMapper.insertScheduleAsList(scheduleList);
    }

    @Override
    public void insertScheduleFileHistory(ScheduleFile scheduleFile) {
        scheduleMapper.insertScheduleFileHistory(scheduleFile);
    }

    @Override
    public List<ScheduleFile> selectFileHistory(ScheduleFile scheduleFile) {
        return scheduleMapper.selectFileHistory(scheduleFile);
    }

    @Override
    public ScheduleFile selectScheduleFile(long refNo) {
        return scheduleMapper.selectScheduleFile(refNo);
    }

    @Override
    public List<ChnlTeam> selectChnlTeamListAll() {
        return scheduleMapper.selectChnlTeamListAll();
    }

    @Override
    public List<ChnlTeam> selectChnlTeamMemList(ChnlTeam chnlTeam) {
        return scheduleMapper.selectChnlTeamMemList(chnlTeam);
    }

    @Override
    public void insertMemberSchedule(MemberSchedule memberSchedule) {
        scheduleMapper.insertMemberSchedule(memberSchedule);
    }

    @Override
    public List<MemberSchedule> selectMemberScheduleList(MemberSchedule memberSchedule) {
        return scheduleMapper.selectMemberScheduleList(memberSchedule);
    }

    @Override
    public void updateMemberSchedule(MemberSchedule memberSchedule) {
        scheduleMapper.updateMemberSchedule(memberSchedule);
    }

    @Override
    public void updateDelMemSchedByMemSchedNo(long memSchedNo, long memNo) {
        scheduleMapper.updateDelMemSchedByMemSchedNo(memSchedNo, memNo);
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        scheduleMapper.updateSchedule(schedule);
    }

    @Override
    public Schedule selectScheduleBySchedNo(int schedNo) {
        return scheduleMapper.selectScheduleBySchedNo(schedNo);
    }

    @Override
    public List<Channel> selectChannelAllList() {
        return scheduleMapper.selectChannelAllList();
    }

    @Override
    public List<Schedule> selectWebfosButtonScheduleList(Schedule schedule) {
        return scheduleMapper.selectWebfosButtonScheduleList(schedule);
    }

    @Override
    public MemberSchedule selectMemberSchedule(MemberSchedule memberSchedule) {
        return scheduleMapper.selectMemberSchedule(memberSchedule);
    }

    @Override
    public List<GroupTeam> selectGroupTeamListAll() {
        return scheduleMapper.selectGroupTeamListAll();
    }

    @Override
    public List<ChnlTeam> selectGroupTeamMemList(GroupTeam groupTeam) {
        return scheduleMapper.selectGroupTeamMemList(groupTeam);
    }

    @Override
    public List<Channel> selectTotalChnlList(Account account) {
        return scheduleMapper.selectTotalChnlList(account);
    }

    @Override
    public List<DashBoardInfo> selectWorkDetailList(DashBoard dashBoard) {
        return scheduleMapper.selectWorkDetailList(dashBoard);
    }

    @Override
    public SubtitleDetailInfo selectSubDtlInfo(long schedNo) {
        return scheduleMapper.selectSubDtlInfo(schedNo);
    }


}
