package com.example.user.testmp3exif.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

/**
 * Created by User on 02/11/2017.
 */

public class Mp3Obj implements Serializable {

    private static final long serialVersionUID = 6553573892863018623L;

    private String title;
    private String author;
    private String durationFormated;
    private long duration;
    private byte[] cover;
    private String absolutePath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDurationFormated() {
        return durationFormated;
    }

    public void setDurationFormated(String durationFormated) {
        this.durationFormated = durationFormated;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }


    public Bitmap getCoverBitmap() {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
