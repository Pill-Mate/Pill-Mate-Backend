package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
public class WeekCountDTO {
    //주 유무
    private Boolean sunday;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;
    //약물 복용갯수/복약남은 갯수
    private Integer countAll;
    private Integer countLeft;

    public WeekCountDTO(Boolean sunday, Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday, Integer countAll, Integer countLeft) {
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.countAll = countAll;
        this.countLeft = countLeft;
    }
}
