package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class ComboKeyComparator extends WritableComparator {
    public ComboKeyComparator() {
        super(ComboKey.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        ComboKey k1 = (ComboKey)a ;
        ComboKey k2 = (ComboKey)b ;
        return k1.compareTo(k2) ;
    }
}
