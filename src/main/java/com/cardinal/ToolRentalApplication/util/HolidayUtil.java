package com.cardinal.ToolRentalApplication.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class HolidayUtil {

    public static boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }

    private static boolean isIndependenceDay(LocalDate date) {
        if (date.getMonth() == Month.JULY) {
            if (date.getDayOfMonth() == 4) {
                return true;
            }
            if (date.getDayOfMonth() == 3 && date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                return true;
            }
            if (date.getDayOfMonth() == 5 && date.getDayOfWeek() == DayOfWeek.MONDAY) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLaborDay(LocalDate date) {
        return date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7;
    }
}
