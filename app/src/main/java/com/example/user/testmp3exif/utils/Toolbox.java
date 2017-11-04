package com.example.user.testmp3exif.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaMetadataRetriever;
import android.support.annotation.Nullable;

import com.example.user.testmp3exif.R;
import com.example.user.testmp3exif.model.Mp3Obj;

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

    public static AlertDialog alertMsg(Context context, String title, String message, DialogInterface.OnClickListener positiveAction, boolean cancelable){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.generic_yes,positiveAction)
                .setCancelable(cancelable)
        ;

        return alertDialog.show();
    }

    public static AlertDialog alertMsg(Context context, String title, String message,@Nullable DialogInterface.OnClickListener positiveAction, boolean cancelable, boolean showNegative,@Nullable DialogInterface.OnClickListener negativeAction){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.generic_yes,positiveAction)
                .setCancelable(cancelable)
        ;

        if(showNegative){
            alertDialog.setNegativeButton("Não",negativeAction);
        }

        return alertDialog.show();
    }

    public static Mp3Obj buildMp3FromPath(String absolutePath){
        Mp3Obj mp3Obj = null;

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(absolutePath);

        if(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null) {
            mp3Obj = new Mp3Obj();
            mp3Obj.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            mp3Obj.setDuration(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            mp3Obj.setDurationFormated(convertMillisecondsToString(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
            mp3Obj.setAuthor(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            mp3Obj.setCover(mmr.getEmbeddedPicture());
            mp3Obj.setAbsolutePath(absolutePath);
            //
        }

        return  mp3Obj;
    }

    public static String convertMillisecondsToString(String duration_string){
        String durationFinal = null;
        if(duration_string != null) {
            String formatHMS = "%s:%s:%s";
            //String formatHMS = "%tH:%tM:%tS";
            String formatMS = "%s:%s";
            //String formatMS = "%tM:%tS";
            long durationMs = Long.parseLong(duration_string);
            //
            long duration = durationMs / 1000;
            long h = duration / 3600;
            long m = (duration - h * 3600) / 60;
            long s = duration - (h * 3600 + m * 60);

            String hs = /*(String.valueOf(h).length() == 1 ) ? "0"+(String.valueOf(h)):*/(String.valueOf(h)) ;
            String ms = (String.valueOf(m).length() == 1 ) ? "0"+(String.valueOf(m)):(String.valueOf(m)) ;
            String ss = (String.valueOf(s).length() == 1 ) ? "0"+(String.valueOf(s)):(String.valueOf(s)) ;

            if(h > 0){
                durationFinal = String.format(formatHMS,hs,ms,ss);
            }else{
                durationFinal = String.format(formatMS,ms,ss);
            }

        }
        return durationFinal;

    }


}
