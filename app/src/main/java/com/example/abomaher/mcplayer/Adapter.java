package com.example.abomaher.mcplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<MusicList> {
    public Adapter(Context context, ArrayList<MusicList> list) {
        super(context, 0, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_music, parent, false);
        }

        MusicList musicList = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.Song_Name);
        name.setText(musicList.getSongTitle());

        TextView det = (TextView) view.findViewById(R.id.Song_Det);
        det.setText(musicList.getSongArtist());
        ImageView musicArt=(ImageView)view.findViewById(R.id.art);

        if (musicList.getMusicArt()!=null)
        {
           musicArt.setImageBitmap(musicList.getMusicArt());
        }else{
            musicArt.setImageResource(R.drawable.art);
        }
        return view;
    }
}
