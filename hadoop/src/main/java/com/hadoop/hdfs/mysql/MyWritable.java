package com.hadoop.hdfs.mysql;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyWritable implements Writable,DBWritable {

    private int id;
    private String name;
    private String txt;

    private String words;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(id);
        out.writeUTF(name);
        out.writeUTF(txt);
        out.writeUTF(words);
        out.writeInt(num);
    }

    public void readFields(DataInput in) throws IOException {
        id = in.readInt();
        name = in.readUTF();
        txt = in.readUTF();
        words = in.readUTF();
        num = in.readInt();
    }

    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1, words);
        statement.setInt(2,num);
    }

    public void readFields(ResultSet resultSet) throws SQLException {
        id = resultSet.getInt(1);
        name = resultSet.getString(2);
        txt = resultSet.getString(3);
    }
}
