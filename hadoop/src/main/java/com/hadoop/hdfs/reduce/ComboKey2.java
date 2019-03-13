package com.hadoop.hdfs.reduce;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ComboKey2 implements WritableComparable<ComboKey2>{
    private int type; // 0-customer 1-order
    private int cid;
    private int oid;
    private String customerInfo = "";
    private String orderInfo = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }


    public int compareTo(ComboKey2 o) {
        if (o.cid == cid){
            if (type == o.type){
                return oid - o.oid;
            }else{
                return  type - o.type;
            }
        }else {
            return cid - o.cid;
        }
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(type);
        out.writeInt(cid);
        out.writeInt(oid);
        out.writeUTF(customerInfo);
        out.writeUTF(orderInfo);
    }

    public void readFields(DataInput in) throws IOException {
        type = in.readInt();
        cid = in.readInt();
        oid = in.readInt();
        customerInfo = in.readUTF();
        orderInfo = in.readUTF();
    }
}
