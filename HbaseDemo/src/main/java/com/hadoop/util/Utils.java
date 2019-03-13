package com.hadoop.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utils {
    private static Robot robot;

    static{
        try {
            robot = new Robot();
        } catch (Exception e) {

        }
    }

    public static int Bytes2Int(byte[] array, int i){
        int i0 = array[0 + i] & 0xFF;
        int i1 = (array[1 + i] & 0xFF) << 8;
        int i2 = (array[2 + i] & 0xFF) << 16;
        int i3 = (array[3 + i] & 0xFF) << 24;

        return i3 | i2 | i1 | i0;
    }

    public static byte[] Int2Btyes(int i){
        byte[] array = new byte[4];
        array[0] = (byte) i;
        array[1] = (byte)(i >> 8);
        array[2] = (byte)(i >> 16);
        array[3] = (byte)(i >> 24);
        return array;
    }
    /**
     *  TODO 位运算将byte转成int导致位移操作出错，需强转为龙
     */
    public static long bytes2Long(byte[] bytes){
        long l0 = (long)bytes[0] & 0xFF;
        long l1 = (long)(bytes[1] & 0xFF) << 8;
        long l2 = (long)(bytes[2] & 0xFF) << 16;
        long l3 = (long)(bytes[3] & 0xFF) << 24;
        long l4 = (long)(bytes[4] & 0xFF) << 32;
        long l5 = (long)(bytes[5] & 0xFF) << 40;
        long l6 = (long)(bytes[6] & 0xFF) << 48;
        long l7 = (long)(bytes[7] & 0xFF) << 56;

        return l7 | l6 | l5 | l4 | l3 | l2 | l1 | l0;
    }

    public static byte[] long2Btyes(long l){
        byte[] array = new byte[8];
        array[0] = (byte) l;
        array[1] = (byte)(l >> 8);
        array[2] = (byte)(l >> 16);
        array[3] = (byte)(l >> 24);
        array[4] = (byte)(l >> 32);
        array[5] = (byte)(l >> 40);
        array[6] = (byte)(l >> 48);
        array[7] = (byte)(l >> 56);
        return array;
    }

    public static byte[] captureScreen(){

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage bImage = robot.createScreenCapture(new Rectangle(0, 0, 1366, 768));
            ImageIO.write(bImage, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {

        }
        return null;
    }

    public static byte[] ZipData(byte[] data){
		/*
		 * TODO 这里用try-with-resource报错
		 */
        try{
            //
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            zos.putNextEntry(new ZipEntry("one"));
            zos.write(data);
            zos.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
//		try (
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				ZipOutputStream zos = new ZipOutputStream(baos)
//				){
//			zos.putNextEntry(new ZipEntry("one"));
//			zos.write(data);
//			return baos.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return null;
    }
    public static byte[] unZipData(byte[] data){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ZipInputStream zis = new ZipInputStream(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            zis.getNextEntry();
            while ((len = zis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] serializeObject( Serializable o)  {
        try{
            ByteArrayOutputStream baos  = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Serializable deserializeObject(byte[] data){

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Serializable o = (Serializable) ois.readObject();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        DecimalFormat format = new DecimalFormat("00");
        
        System.out.println(format.format(1));
    }

    private  static DecimalFormat df = new DecimalFormat();
    public static String hashCode(String caller, String date){
        df.applyPattern("00");
        int hash = (caller + date).hashCode() & Integer.MAX_VALUE;
        return df.format(hash % 100);
    }
}
