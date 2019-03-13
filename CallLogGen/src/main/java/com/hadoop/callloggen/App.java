package com.hadoop.callloggen;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    private static List<String> numList = new ArrayList<String>();
    static {
        numList.add("15810092493");
        numList.add("18000696806");
        numList.add("15151889601");
        numList.add("13269361119");
        numList.add("15032293356");
        numList.add("17731088562");
        numList.add("15338595369");
        numList.add("15733218050");
        numList.add("15614201525");
        numList.add("15778423030");
        numList.add("18641241020");
        numList.add("15732648446");
        numList.add("13341109505");
        numList.add("13560190665");
        numList.add("18301589432");
        numList.add("13520404983");
        numList.add("18332562075");
        numList.add("18620192711");
    }


    public static void main(String[] args){
        if(args == null || args.length == 0){
            System.out.println("ERR:args = 0");
            return;
        }
        try {
            callLogGen(args[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void callLogGen(String path)  throws Exception{

        Random r = new Random();
        FileWriter fw = new FileWriter(path,true);
        int size = numList.size();
        int count = 0;
        while (true){

            String caller = numList.get(r.nextInt(size));
            String callee = numList.get(r.nextInt(size));
            if (callee.equals(caller)){
                continue;
            }
            int year = r.nextInt(18) + 2000;
            int month = r.nextInt(12);
            int day = r.nextInt(29) + 1;
            int hour = r. nextInt(24);
            int min = r.nextInt(60);
            int sec = r.nextInt(60);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);
            c.set(Calendar.SECOND, sec);

            Date date = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
            String dateStr = sdf.format(date);

            int duration = r.nextInt(500);
            DecimalFormat df = new DecimalFormat("000");
            String durStr = df.format(duration);


            //String log = caller + "," + callee + "," + dateStr + "," + durStr  ;
            String log = caller + "," + callee + "," + dateStr + "," + durStr  ;

            fw.write(log + "\r\n");
            fw.flush();
            count ++;
            if (count % 50 == 0){
                System.out.println("generation " + count + " logs.");
            }
            Thread.sleep(100);
        }

    }


}
