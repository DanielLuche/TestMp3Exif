package com.example.user.testmp3exif.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
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
    private SeekBar sb_mp3;
    private TextView tv_progress;
    private ImageView iv_back;
    private ImageView iv_play_pause;
    private ImageView iv_forward;
    //
    private Mp3Obj mp3Obj;
    private String absolutePath;
    private MediaPlayer mTrack;
    private Handler mHandler;
    private Runnable mRunnable;
    private Thread mThread;
    //saveInstance
    long mDuration = 0L;
    long mCurrentTime = 0L;
    boolean isPlaying = false;
    boolean onBackPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //
        if(savedInstanceState != null){
            mDuration = savedInstanceState.getLong(Constants.PARAM_MP3_DURATION);
            mCurrentTime = savedInstanceState.getLong(Constants.PARAM_MP3_CURRENT_TIME);
            isPlaying = savedInstanceState.getBoolean(Constants.PARAM_MP3_IS_PLAYING);
        }

        iniVars();
        //
        iniActions();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //
        outState.putLong(Constants.PARAM_MP3_DURATION, mDuration);
        outState.putLong(Constants.PARAM_MP3_CURRENT_TIME, mCurrentTime);
        outState.putBoolean(Constants.PARAM_MP3_IS_PLAYING,isPlaying);
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
        sb_mp3 = findViewById(R.id.player_act_sb_track);
        //
        tv_progress = findViewById(R.id.player_act_tv_progress);
        //
        iv_back = findViewById(R.id.player_act_iv_back_10s);
        //
        iv_play_pause = findViewById(R.id.player_act_iv_play_pause);
        //
        iv_forward = findViewById(R.id.player_act_iv_forward_10s);
        //
        mTrack = new MediaPlayer();
        //
        mHandler = new Handler();
        //
        setMp3Info();
    }


    private void recoverIntentInfo() {

        Bundle bundle = getIntent().getExtras();
        //
        if (bundle != null) {
            absolutePath = bundle.getString(Constants.PARAM_ABSOLUTE_PATH);
            //mp3Obj = (Mp3Obj) bundle.getSerializable(Constants.PARAM_MP3_OBJECT);
            mp3Obj = Toolbox.buildMp3FromPath(absolutePath);

            String a = mp3Obj.getTitle();
        } else {
            Log.d("Mp3Bundle", "Bundle Null\n");
        }
    }

    private void setMp3Info() {
        tv_title.setText(mp3Obj.getTitle());
        //
        iv_cover.setImageBitmap(mp3Obj.getCoverBitmap());
        //
        tv_progress.setText("");
        //
        sb_mp3.setProgress(0);
        sb_mp3.setMax((int) mp3Obj.getDuration());
        //
        try {
            mTrack.setDataSource(mp3Obj.getAbsolutePath());
            //
            mTrack.prepareAsync();

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

                if (mTrack.isPlaying()) {
                    mTrack.pause();
                    isPlaying = false;
                    //mHandler.removeCallbacks(mRunnable);
                    mThread.interrupt();
                    //
                    iv_play_pause.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));

                } else {
                    mTrack.start();
                    isPlaying = true;
                    //mHandler.post(mRunnable);
                    createThread(mTrack,true);
                    //
                    iv_play_pause.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
                }

            }
        });
        //
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb_mp3.setProgress(sb_mp3.getProgress() - 10);
                mTrack.seekTo(mTrack.getCurrentPosition() - 10000);
            }
        });
        //
        //
        iv_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb_mp3.setProgress(sb_mp3.getProgress() + 10);
                mTrack.seekTo(mTrack.getCurrentPosition() + 10000);
            }
        });

        mTrack.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {

                createThread(mTrack,false);

//                mRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        while (isPlaying) {
//                            updateTrackInfo(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                };
                //mHandler.postDelayed(mRunnable,1000);

                releasePlayerOptions(true);
                //
                if(isPlaying){
                    iv_play_pause.performClick();
                }
            }
        });
        mTrack.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isPlaying = mTrack.isPlaying();
                try {
                    if(mThread.isAlive()){
                        mThread.interrupt();
                    }

                    sb_mp3.setProgress(0);
                    tv_progress.setText(getCurrtimeVsDurationString());
                    resetPlayPause();

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        mTrack.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                    return false;
            }
        });

        sb_mp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mTrack.seekTo(progress);

                }
                //
                tv_progress.setText(getCurrtimeVsDurationString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void resetPlayPause() {
        iv_play_pause.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
    }

    private String getCurrtimeVsDurationString() {
        String s = "";
        s = Toolbox.convertMillisecondsToString(String.valueOf(mTrack.getCurrentPosition())) +" / "+Toolbox.convertMillisecondsToString(String.valueOf(mTrack.getDuration()));
        return s;

    }

    private void createThread(final MediaPlayer mTrack,boolean start) {

        mThread = new Thread(){
            public void run() {
                while (isPlaying && mThread.isAlive()) {

                    updateTrackInfo(mTrack.getDuration(), mTrack.getCurrentPosition());
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //
        if(start){
            mThread.start();

        }
    }

    private void updateTrackInfo(final int duration, final int currentPosition) {
        mDuration = duration;
        mCurrentTime = currentPosition;
        //
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sb_mp3.setProgress(currentPosition);
                //
                tv_progress.setText(getCurrtimeVsDurationString());
            }
        });

    }

    private void releasePlayerOptions(boolean release) {
        //
        sb_mp3.setEnabled(release);
        iv_back.setEnabled(release);
        iv_play_pause.setEnabled(release);
        iv_forward.setEnabled(release);
        //
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!onBackPressed) {
            mDuration = mTrack.getDuration();
            mCurrentTime = mTrack.getCurrentPosition();
            isPlaying = mTrack.isPlaying();
            //
            //
            if(mThread.isAlive()){
                mThread.interrupt();
            }
            mTrack.stop();
            mTrack.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //
        onBackPressed = true;
        //
        isPlaying = false;
        //
        if(mThread.isAlive()){
            mThread.interrupt();
        }
        mTrack.stop();
        mTrack.release();
    }
}
