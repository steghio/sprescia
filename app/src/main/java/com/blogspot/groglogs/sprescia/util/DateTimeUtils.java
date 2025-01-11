package com.blogspot.groglogs.sprescia.util;

import java.time.LocalDate;

public class DateTimeUtils {

    public static LocalDate fromString(String s){
        String[] split = s.split("-");
        return LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static String stringFrom(int year, int month, int day){
        return LocalDate.of(year, month, day).toString();
    }

    public static String timeStringFrom(int hours, int minutes){
        return hours + ":" + minutes;
    }

    public static int hoursFromString(String s){
        return Integer.parseInt(s.split(":")[0]);
    }

    public static int minutesFromString(String s){
        return Integer.parseInt(s.split(":")[1]);
    }
}
