package com.blogspot.groglogs.sprescia.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class StringUtils {
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    public static String decimal2String2Precision(double d){
        return df2.format(d);
    }

    public static String formatIntegerWithThousandSeparator(int i){
        return String.format(Locale.getDefault(), "%,d", i);
    }
}
