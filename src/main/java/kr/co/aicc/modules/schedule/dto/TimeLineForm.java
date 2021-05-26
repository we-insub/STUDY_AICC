package kr.co.aicc.modules.schedule.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimeLineForm {
    private String curday;
    private List<Integer> chnlNos;
}
