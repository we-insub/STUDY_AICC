package kr.co.aicc.modules.settings.service;

import kr.co.aicc.modules.dashboard.domain.Schedule;
import kr.co.aicc.modules.settings.dto.ChannelForm;

import java.util.*;

public interface ChannelSettingService {
    List<ChannelForm> getChannelList(ChannelForm channelForm);
    int getChannelListCnt(ChannelForm channelForm);
    int createChannel(ChannelForm channelForm);
    int updateChannel(ChannelForm channelForm);
    int deleteChannel(ChannelForm channelForm);
    Schedule getMemberScheduleList(Schedule schedule);
}
