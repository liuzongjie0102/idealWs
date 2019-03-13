package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ComboKey implements WritableComparable<ComboKey> {
    private int year;
    private int temp;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int compareTo(ComboKey o) {
        if (this.year != o.year){
            return this.year - o.year;
        }else{
            return o.temp - this.temp;
        }
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(year);
        out.writeInt(temp);
    }

    public void readFields(DataInput in) throws IOException {
        this.year = in.readInt();
        this.temp = in.readInt();
    }
}
