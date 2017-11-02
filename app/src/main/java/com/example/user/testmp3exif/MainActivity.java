package com.example.user.testmp3exif;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.testmp3exif.adapters.Mp3Adapter;
import com.example.user.testmp3exif.model.Mp3Obj;
import com.example.user.testmp3exif.utils.Constants;
import com.example.user.testmp3exif.utils.Toolbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private TextView tv_label;
    private ListView lv_files;
    private Mp3Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniVars();

        iniActions();


    }

    private void iniVars() {
        //
        context = this;
        //
        tv_label = findViewById(R.id.main_tv_lbl);
        //
        lv_files = findViewById(R.id.main_lv_files);

    }

    private void iniActions() {
        Toolbox.buildInf();
        //
        setMp3Adapter();

    }

    private void setMp3Adapter() {
        mAdapter = new Mp3Adapter(
                context,
                R.layout.mp3_cell,
                loadFilesList()
        );
        //
        lv_files.setAdapter(mAdapter);
    }

    private List<Mp3Obj> loadFilesList() {
        File[] files = new File(Constants.MP3_PATH).listFiles();
        List<Mp3Obj> mp3ObjList = new ArrayList<>();

        for (File file:files) {
            Mp3Obj mp3Obj = new Mp3Obj();
//            String title = "";
//            String duration = "";
//            Bitmap cover =  null;
            String ext = "";

            try {
                //ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                //Log.d("Exif",exifInterface.toString());
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(file.getAbsolutePath());

                if(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null) {
                    mp3Obj.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                    mp3Obj.setDuration(convertMillisecondsToString(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                    mp3Obj.setAuthor(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    mp3Obj.setCover(BitmapFactory.decodeByteArray(mmr.getEmbeddedPicture(), 0, mmr.getEmbeddedPicture().length));
                    //
                    ext = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                    //Log.d("MMR_lUCHE", mmr.toString());

                    mp3ObjList.add(mp3Obj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return mp3ObjList;
    }

    private String convertMillisecondsToString(String duration_string){
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
