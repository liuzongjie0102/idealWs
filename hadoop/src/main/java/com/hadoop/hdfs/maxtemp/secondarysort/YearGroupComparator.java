package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class YearGroupComparator extends WritableComparator {
    public YearGroupComparator() {
        super(ComboKey.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        ComboKey k1 = (ComboKey)a ;
        ComboKey k2 = (ComboKey)b ;
        return k1.getYear() - k2.getYear() ;
    }
}
