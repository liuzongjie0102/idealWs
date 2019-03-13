package com.jdbc;


import java.sql.*;

public class TestJDBC {
    
    
    public static void main(String[] args){
        try {
            main2(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main1(String[] args) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.230.1:3306/mydb", "centos", "123456");
            PreparedStatement pps = connection.prepareStatement("SELECT * FROM words");
            ResultSet rs = pps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                System.out.println(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) throws  Exception {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.230.110:10000/mydb2");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select id , name ,age from t1");
        while(rs.next()){
            System.out.println(rs.getInt(1) + "," + rs.getString(2)) ;
        }
        rs.close();
        st.close();
        conn.close();
    }
}
