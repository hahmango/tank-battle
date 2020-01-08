package com.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyClock{
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM:ss");
        while(true){
            try{
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                Thread.sleep(1000);
            } catch (Exception e){

            }
        }
    }
}
