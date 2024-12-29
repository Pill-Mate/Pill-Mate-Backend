package com.example.Pill_Mate_Backend.domain.check.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WeekDTO {
    private Boolean sunday;
    private Boolean monday;
    private Boolean tuesday;
    private Boolean wednesday;
    private Boolean thursday;
    private Boolean friday;
    private Boolean saturday;

    public WeekDTO(Boolean sunday, Boolean monday, Boolean tuesday, Boolean wednesday, Boolean thursday, Boolean friday, Boolean saturday) {
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
    }
}
