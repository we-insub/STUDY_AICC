package kr.co.aicc.modules.dashboard.domain;

import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRow {
    private List<Channel> channelList;
}
