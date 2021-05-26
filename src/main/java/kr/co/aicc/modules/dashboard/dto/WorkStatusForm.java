package kr.co.aicc.modules.dashboard.dto;

import kr.co.aicc.modules.account.domain.Account;
import lombok.Data;

import java.util.List;

@Data
public class WorkStatusForm {
    private List<Integer> chnlNo;
    private Account account;
}
