package com.hadoop.util;

import java.text.DecimalFormat;

public class CallLogUtils {

    private  static DecimalFormat df = new DecimalFormat();
    public static String hashCode(String caller, String date){
        df.applyPattern("00");
        int hash = (caller + date).hashCode() & Integer.MAX_VALUE;
        return df.format(hash % 100);
    }
}
