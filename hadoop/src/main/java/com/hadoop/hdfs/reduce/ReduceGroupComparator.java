package com.hadoop.hdfs.reduce;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class ReduceGroupComparator extends WritableComparator {
    public ReduceGroupComparator(){
        super(ComboKey2.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        ComboKey2 k1 = (ComboKey2) a;
        ComboKey2 k2 = (ComboKey2) b;
        return k1.getCid() - k2.getCid();
    }
}
