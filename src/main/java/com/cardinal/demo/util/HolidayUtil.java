package com.cardinal.demo.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class HolidayUtil {

    public static boolean isHoliday(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = LocalDate.of(year, 7, 4);
        LocalDate laborDay = calculateLaborDay(year);
        LocalDate observeIndependenceDay = independenceDay;

        // Handle observed holidays
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            observeIndependenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            observeIndependenceDay = independenceDay.plusDays(1);
        }

        return date.equals(independenceDay) || date.equals(observeIndependenceDay) || date.equals(laborDay);
    }

    private static LocalDate calculateLaborDay(int year) {
        LocalDate date = LocalDate.of(year, 9, 1);
        while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            date = date.plusDays(1);
        }
        return date;
    }
}



