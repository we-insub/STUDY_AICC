package kr.co.aicc.modules.schedule.domain;

import kr.co.aicc.modules.account.domain.Account;
import kr.co.aicc.modules.dashboard.domain.Channel;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TimeLine {
    @Builder.Default
    private LocalDate curDay = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth());
    private LocalDate prevDay;
    private LocalDate nextDay;
    private List<Channel> channelList;
    private Account account;
    private List<Integer> chnlNos;
    private LocalDateTime searchStartTime;
    private LocalDateTime searchEndTime;
}
