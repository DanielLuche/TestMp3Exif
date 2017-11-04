package com.example.user.testmp3exif;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.testmp3exif.adapters.Mp3Adapter;
import com.example.user.testmp3exif.model.Mp3Obj;
import com.example.user.testmp3exif.ui.PlayerActivity;
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
        //
        lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Mp3Obj item = (Mp3Obj) adapterView.getItemAtPosition(i);

                Toolbox.alertMsg(
                        context,
                        "Tocar Mp3",
                        "Deseja iniciar esse mp3?",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(context,PlayerActivity.class);
                                //intent.setAction(android.content.Intent.ACTION_VIEW);
                                Bundle bundle = new Bundle();
                                //bundle.putSerializable(Constants.PARAM_MP3_OBJECT,item);
                                bundle.putSerializable(Constants.PARAM_ABSOLUTE_PATH,item.getAbsolutePath());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        },
                        false,
                        true,
                        null
                );


            }
        });

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
            Mp3Obj mp3Obj = Toolbox.buildMp3FromPath(file.getAbsolutePath());
            //
            if(mp3Obj != null){
                mp3ObjList.add(mp3Obj);
            }
        }

        return mp3ObjList;
    }

}
