package com.example.user.testmp3exif.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.testmp3exif.R;
import com.example.user.testmp3exif.model.Mp3Obj;
import com.example.user.testmp3exif.utils.Constants;
import com.example.user.testmp3exif.utils.Toolbox;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity {

    private Context context;
    private TextView tv_title;
    private ImageView iv_cover;
    private ProgressBar pb_mp3;
    private ImageView iv_back;
    private ImageView iv_play_pause;
    private ImageView iv_forward;
    //
    private Mp3Obj mp3Obj;
    private String absolutePath;
    private MediaPlayer mTrack;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        iniVars();
        //
        iniActions();


    }

    private void iniVars() {

        recoverIntentInfo();
        //
        context = this;
        //
        tv_title = findViewById(R.id.player_act_tv_ttl);
        //
        iv_cover = findViewById(R.id.player_act_iv_cover);
        //
        pb_mp3 = findViewById(R.id.player_act_pb_track);
        //
        iv_back = findViewById(R.id.player_act_iv_back_10s);
        //
        iv_play_pause = findViewById(R.id.player_act_iv_play_pause);
        //
        iv_forward = findViewById(R.id.player_act_iv_forward_10s);
        //
        mTrack = new MediaPlayer();
        //
        isPlaying = false;
        //
        setMp3Info();
    }


    private void recoverIntentInfo() {

        Bundle bundle = getIntent().getExtras();
        //
        if(bundle != null){
            absolutePath = bundle.getString(Constants.PARAM_ABSOLUTE_PATH);
            //mp3Obj = (Mp3Obj) bundle.getSerializable(Constants.PARAM_MP3_OBJECT);
            mp3Obj = Toolbox.buildMp3FromPath(absolutePath);

            String a = mp3Obj.getTitle();
        }else{
            Log.d("Mp3Bundle","Bundle Null\n");
        }
    }

    private void setMp3Info() {
        tv_title.setText(mp3Obj.getTitle());
        //
        iv_cover.setImageBitmap(mp3Obj.getCover());
        //
        pb_mp3.setMax(1000);
        //
        try {
            mTrack.setDataSource(mp3Obj.getAbsolutePath());
            //
            mTrack.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void iniActions() {

        iv_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(mp3Obj.getAbsolutePath());
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                startActivity(intent);*/

               if(isPlaying){
                   mTrack.pause();
                   //
                   iv_play_pause.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
                   //
                   isPlaying = false;
               }else{
                   mTrack.start();
                   //
                   iv_play_pause.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                   //
                   isPlaying = true;
               }

            }
        });

    }
}
