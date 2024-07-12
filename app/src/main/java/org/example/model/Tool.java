package org.example.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class Tool {
    private final String toolCode;
    private final String toolType;
    private final String brand;
    private final float dailyCharge;
    private final boolean weekdayCharge, weekendCharge, holidayCharge;

    public Tool(String toolCode, String toolType, String brand, float dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand  = brand;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }
    public String getToolCode() {

        return toolCode;
    }

    public String getToolType() {

        return toolType;
    }

    public String getBrand() {

        return brand;
    }

    public float getDailyCharge() {

        return dailyCharge;
    }

    public boolean isWeekdayCharge() {

        return weekdayCharge;
    }

    public boolean isWeekendCharge() {

        return weekendCharge;
    }

    public boolean isHolidayCharge() {

        return holidayCharge;
    }
    public int calculateChargeDays(LocalDate date, int duration) {
        int days = 0;
        for (int i = 0; i < duration; i++) {
            var day = date.getDayOfWeek();
            var isHolidayToday = isHoliday(date);
            if ((isHolidayToday && isHolidayCharge()) ||
                    (!isHolidayToday && isWeekday(day) && isWeekdayCharge()) ||
                    (!isHolidayToday && !isWeekday(day) && isWeekendCharge())) {
                days++;
            }
            date = date.plusDays(1);
        }
        return days;
    }

    public float calculateCharge(LocalDate date, int duration) {
        return Math.round(this.dailyCharge * this.calculateChargeDays(date, duration) * 100.0F) / 100.0F;
    }

    public float calculateCharge(int chargeDays) {

        return Math.round(this.dailyCharge * chargeDays * 100.0F) / 100.0F;
    }

    private boolean isWeekday(DayOfWeek day) {

        return day.getValue() < DayOfWeek.SATURDAY.getValue();
    }

    //Check whether the given day is holiday or not
    private boolean isHoliday(LocalDate date) {
        switch (date.getMonth()) {
            case JULY:
                return date.equals(weekEndToWeekday(
                        LocalDate.of(date.getYear(), Month.JULY, 4)));
            case SEPTEMBER:
                var firstOfMonth = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1);
                var firstMonday = firstOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
                return date.equals(firstMonday);
            default:
                return false;
        }
    }

    //turning local date from weekend to weekday.
    private LocalDate weekEndToWeekday(LocalDate date) {
        var day = date.getDayOfWeek();
        if (isWeekday(day)) {
            return date;
        }
        if (day == DayOfWeek.SATURDAY) {
            date = date.minusDays(1);
        }else {
            date = date.plusDays(1);
        }
        return date;
    }
}
