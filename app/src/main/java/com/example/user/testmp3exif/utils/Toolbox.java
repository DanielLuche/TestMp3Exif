package com.example.user.testmp3exif.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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

    public static AlertDialog alertMsg(Context context, String title, String message, DialogInterface.OnClickListener positiveAction, boolean cancelable, boolean showNegative, DialogInterface.OnClickListener negativeAction){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog
                .setTitle(title)
                .setMessage(message);



        return alertDialog.show();
    }


}
