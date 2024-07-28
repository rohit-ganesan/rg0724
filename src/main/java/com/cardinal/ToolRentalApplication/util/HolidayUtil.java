package com.cardinal.ToolRentalApplication.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class HolidayUtil {

    public static boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }

    private static boolean isIndependenceDay(LocalDate date) {
        if (date.getMonth() == Month.JULY && date.getDayOfMonth() == 4) {
            return true;
        }
        if (date.getMonth() == Month.JULY && date.getDayOfWeek() == DayOfWeek.FRIDAY && date.getDayOfMonth() == 3) {
            return true;
        }
        if (date.getMonth() == Month.JULY && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() == 5) {
            return true;
        }
        return false;
    }

    private static boolean isLaborDay(LocalDate date) {
        return date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7;
    }
}

