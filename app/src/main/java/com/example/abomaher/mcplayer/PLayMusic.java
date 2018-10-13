package com.example.abomaher.mcplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class PLayMusic extends Fragment {

    private static final int MY_PERMISSION_REQUEST = 1;
    View rootView;
    ListView lv;
    ArrayList<MusicList> arrayList;
    TextView song_name, song_artist;
    ImageView musicArt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.music_play, container, false);
        //decleare songTitle and songArtist TextView
        song_name = (TextView) rootView.findViewById(R.id.song_name);
        song_artist = (TextView) rootView.findViewById(R.id.song_artist);
        musicArt = (ImageView) rootView.findViewById(R.id.music_art);

        //To request a permission for reading from external storage
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST);
            }
        } else {
            arrayList = new ArrayList<MusicList>();
            Adapter adapter = new Adapter(getActivity().getApplicationContext(), arrayList);
            lv = (ListView) rootView.findViewById(R.id.list_view_music);
            lv.setAdapter(adapter);
            //To get the music file from the arraylist
            getMusic();
//            setData(0);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PlayingMusic.class);
                    Bundle b = new Bundle();
                    b.putString("fullpath", arrayList.get(i).songPath);
                    b.putString("title", arrayList.get(i).songTitle);
                    b.putString("artist", arrayList.get(i).songArtist);
                    //get music art string
                    String art = compreseArt(i);
                    if (art == null) {
                        b.putString("art", "No Art");
                    } else {
                        b.putString("art", art);
                    }

                    b.putInt("position", i);
                    intent.putExtras(b);
                    setData(i);
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }

    //This method for getting all music file from external storage and add to the arraylist
    public void getMusic() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            //getting data from external storage
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int path = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            //for music art
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String fullPath = songCursor.getString(path);
                /*
                this function for getting music art and add it to array list
                 */
                mmr.setDataSource(fullPath);
                byte[] data = null;
                try {
                    data = mmr.getEmbeddedPicture();
                } catch (Exception e) {
                }
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    arrayList.add(new MusicList(bitmap, fullPath, currentTitle.replace(".mp3", ""), currentArtist));
                } else {
                    arrayList.add(new MusicList(null, fullPath, currentTitle.replace(".mp3", ""), currentArtist));
                }
            } while (songCursor.moveToNext());
        }
    }

    //This method for the request result if the permission granted or not
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), "Permission Granted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No Permission Granted.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void setData(int index) {
        song_name.setText(arrayList.get(index).getSongTitle());
        song_artist.setText(arrayList.get(index).getSongArtist());

        if (arrayList.get(index).getMusicArt() != null) {
            musicArt.setImageBitmap(arrayList.get(index).getMusicArt());
        } else {
            musicArt.setImageResource(R.drawable.clipart);
        }
    }

    //this method to compressed music art to send throught the intent
    public String compreseArt(int index) {
        String musicArt = null;
        try {
            Bitmap bitmap = arrayList.get(index).getMusicArt();
            //Write file
            musicArt = "bitmap.png";
            FileOutputStream stream = getActivity().openFileOutput(musicArt, getContext().MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //Cleanup
            stream.close();
            //bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return musicArt;
    }
}