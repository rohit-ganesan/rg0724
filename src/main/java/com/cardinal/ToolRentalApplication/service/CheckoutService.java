package com.cardinal.ToolRentalApplication.service;

import com.cardinal.ToolRentalApplication.model.RentalAgreement;
import com.cardinal.ToolRentalApplication.model.Tool;
import com.cardinal.ToolRentalApplication.util.HolidayUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class CheckoutService {
    private Map<String, Tool> tools = new HashMap<>();

    public CheckoutService() {
        tools.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
        tools.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        tools.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        tools.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) throws IllegalArgumentException {
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
        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = preDiscountCharge * discountPercent / 100.0;
        double finalCharge = preDiscountCharge - discountAmount;

        return new RentalAgreement(toolCode, tool.getType(), tool.getBrand(), rentalDays, checkoutDate, dueDate,
                tool.getDailyCharge(), chargeDays, preDiscountCharge, discountPercent, discountAmount, finalCharge);
    }

    private int calculateChargeDays(Tool tool, LocalDate startDate, LocalDate endDate) {
        int chargeDays = 0;

        for (LocalDate date = startDate.plusDays(1); !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isWeekend = date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY;
            boolean isHoliday = HolidayUtil.isHoliday(date);

            if (tool.isWeekdayCharge() && !isWeekend && !isHoliday) {
                chargeDays++;
            } else if (tool.isWeekendCharge() && isWeekend && !isHoliday) {
                chargeDays++;
            } else if (tool.isHolidayCharge() && isHoliday) {
                chargeDays++;
            }
        }

        return chargeDays;
    }
}

