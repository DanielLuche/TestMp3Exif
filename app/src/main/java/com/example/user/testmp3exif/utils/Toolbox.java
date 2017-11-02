package com.example.user.testmp3exif.utils;

import java.io.File;

/**
 * Created by User on 02/11/2017.
 */

public class Toolbox {

    public static void buildInf(){

        File dir = new File(Constants.MP3_PATH);
        //
        boolean result = false;
        if(!dir.exists()){
            result =  dir.mkdir();
        }
        int i = result ? 1: 0;

    }


}
