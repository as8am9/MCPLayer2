package com.example.abomaher.mcplayer;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class VideoAdapter extends ArrayAdapter<MusicList> {
    public VideoAdapter(Context context, ArrayList<MusicList> list) {
        super(context, 0, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_video, parent, false);
        }

        MusicList musicList = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.video_name);
        name.setText(musicList.getSongTitle());

        ImageView musicArt=(ImageView)view.findViewById(R.id.video_art);
        if (musicList.getMusicArt()!=null) {
            musicArt.setImageBitmap(musicList.getMusicArt());
        }else {
            musicArt.setImageResource(R.drawable.thumbnail);
        }
        return view;
    }
}
