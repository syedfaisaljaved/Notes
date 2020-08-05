package com.notepad.text.notes.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getCurrentTimeStamp(){

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
