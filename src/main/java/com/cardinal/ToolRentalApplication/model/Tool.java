package com.cardinal.ToolRentalApplication.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Tool {
    private String code;
    private String type;
    private String brand;
    private double dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    public Tool(String code, String type, String brand, double dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.code = code;
        this.type = type;
        this.brand = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }
}