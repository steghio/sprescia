package com.blogspot.groglogs.sprescia.util;

import java.text.DecimalFormat;

public class StringUtils {
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    public static String decimal2String2Precision(double d){
        return df2.format(d);
    }

    public static boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }
}
