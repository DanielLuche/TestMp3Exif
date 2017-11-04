package com.example.user.testmp3exif.utils;

import android.os.Environment;

/**
 * Created by User on 02/11/2017.
 */

public class Constants {

    public static final String MP3_PATH = Environment
            .getExternalStorageDirectory() +
            "/mp3Test";

    public static final String PARAM_ABSOLUTE_PATH = "PARAM_ABSOLUTE_PATH";
    public static final String PARAM_MP3_OBJECT = "PARAM_MP3_OBJECT";
    public static final String PARAM_MP3_DURATION = "PARAM_MP3_DURATION";
    public static final String PARAM_MP3_CURRENT_TIME = "PARAM_MP3_CURRENT_TIME";
    public static final String PARAM_MP3_IS_PLAYING = "PARAM_MP3_IS_PLAYING";



}
