package com.cardinal.demo.service;

import com.cardinal.demo.model.RentalAgreement;
import com.cardinal.demo.model.Tool;
import com.cardinal.demo.model.ToolRequest;
import com.cardinal.demo.util.HolidayUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CheckoutService {

    private final Map<String, Tool> tools;

    @Autowired
    public CheckoutService(Map<String, Tool> tools) {
        this.tools = tools;
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be in the range 0-100.");
        }

        Tool tool = tools.get(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code.");
        }

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);
        BigDecimal dailyCharge = BigDecimal.valueOf(tool.getDailyCharge());
        BigDecimal preDiscountCharge = dailyCharge.multiply(BigDecimal.valueOf(chargeDays))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount)
                .setScale(2, RoundingMode.HALF_UP);

        return new RentalAgreement(toolCode, tool.getType(), tool.getBrand(), rentalDays, checkoutDate, dueDate,
                tool.getDailyCharge(), chargeDays, preDiscountCharge.doubleValue(), discountPercent,
                discountAmount.doubleValue(), finalCharge.doubleValue());
    }

    private int calculateChargeDays(Tool tool, LocalDate startDate, LocalDate endDate) {
        int chargeDays = 0;

        for (LocalDate date = startDate.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
            boolean isHoliday = HolidayUtil.isHoliday(date);

            switch (tool.getType()) {
                case "Chainsaw" -> {
                    if (!isWeekend || (isHoliday && !isWeekend)) {
                        chargeDays++;
                    }
                }
                case "Ladder" -> {
                    if (!isHoliday) {
                        chargeDays++;
                    }
                }
                case "Jackhammer" -> {
                    if (!isWeekend && !isHoliday) {
                        chargeDays++;
                    }
                }
            }
        }

        return chargeDays;
    }

    public void addTool(ToolRequest toolRequest) {
        Tool tool = new Tool(
                toolRequest.getCode(),
                toolRequest.getType(),
                toolRequest.getBrand(),
                toolRequest.getDailyCharge(),
                toolRequest.isWeekdayCharge(),
                toolRequest.isWeekendCharge(),
                toolRequest.isHolidayCharge()
        );
        tools.put(tool.getCode(), tool);
    }
}
