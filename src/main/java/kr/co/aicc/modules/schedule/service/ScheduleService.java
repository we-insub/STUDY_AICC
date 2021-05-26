package kr.co.aicc.modules.schedule.service;

import kr.co.aicc.infra.exception.BizException;
import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import kr.co.aicc.modules.schedule.dto.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface ScheduleService {

    public boolean createTestData(Account account);

    HashMap<String, Object> getTimeLineData(TimeLineForm timeLineForm, Account account) throws BizException;

    HashMap<String, Object> regSchedule(SchedForm schedForm, Account account);

    HashMap<String, Object> deleteSchedule(SchedForm schedForm, Account account);

    HashMap<String, Object> scheduleExcelUpload(SchedExcelUpload schedExcelUpload, Account account);

    HashMap<String, Object> getUploadHistoryData(SchedExcelUploadHistory schedExcelUploadHistory, Account account);

    ResponseEntity<Resource> downloadHistoryExcel(SchedExcelFile schedExcelFile, Account account);

    HashMap<String, Object> copySchedule(CopySchedule copySchedule, Account account);

    HashMap<String, Object> getGroupTeam(Account account);

    HashMap<String, Object> getChnlTeamMem(ChnlTeamMemForm chnlTeamMemForm, Account account);

    HashMap<String, Object> regMemSched(MemSchedForm memSchedForm, Account account);

    HashMap<String, Object> copyNoWorkSchedule(CopyNoWorkSchedule copyNoWorkSchedule, Account account);

    HashMap<String, Object> deleteMemberSchedule(MemSchedForm memSchedForm, Account account);

    void scheduleCheck(SchedForm schedForm, Account account) throws BizException;

    HashMap<String, Object> isWebposButtonExposure(TimeLineForm timeLineForm, Account account);

    List<Channel> getTotalChnlList(Account account);

    HashMap<String, Object> getGroupTeamMem(GroupTeamMemForm groupTeamMemForm, Account account);

    HashMap<String, Object> getWorkDetails(WorkDetail workDetail, Account account);

    void textDownload(HttpServletResponse response, long schedNo) throws Exception;
}
