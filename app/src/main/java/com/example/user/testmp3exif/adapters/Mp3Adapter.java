package com.example.user.testmp3exif.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.testmp3exif.R;
import com.example.user.testmp3exif.model.Mp3Obj;

import java.util.List;

/**
 * Created by User on 02/11/2017.
 */

public class Mp3Adapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<Mp3Obj> source;


    public Mp3Adapter(Context context, int resource, List<Mp3Obj> source) {
        this.context = context;
        this.resource = resource;
        this.source = source;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            //
            view = inflater.inflate(resource, parent, false);
        }
        //
        Mp3Obj item = source.get(position);
        //
        ImageView iv_cover = view.findViewById(R.id.mp3_cell_iv_cover);
        //
        TextView tv_title = view.findViewById(R.id.mp3_cell_tv_title);
        TextView tv_duration = view.findViewById(R.id.mp3_cell_tv_duration);
        TextView tv_author  = view.findViewById(R.id.mp3_cell_tv_author);
        //
        iv_cover.setImageBitmap(item.getCoverBitmap());
        //
        tv_title.setText(item.getTitle());
        tv_duration.setText(item.getDurationFormated());
        tv_author.setText(item.getAuthor());

        return view;
    }
}
