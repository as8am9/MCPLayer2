package com.example.abomaher.mcplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayVideo extends Fragment {

    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<MusicList> arrayList;
    GridView lv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.video_play, container, false);

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
            VideoAdapter adapter = new VideoAdapter(getActivity().getApplicationContext(), arrayList);
            lv = (GridView) rootView.findViewById(R.id.list_view_video);
            lv.setAdapter(adapter);
            getVideo();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //making an intent to jumb to playing activity
                    Intent intent = new Intent(getActivity().getApplicationContext(), PlayingVideo.class);
                    Bundle b = new Bundle();
                    b.putString("fullpath",arrayList.get(i).songPath);
                    b.putInt("position", i);
                    intent.putExtras(b);
                    startActivity(intent);

                }
            });
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //This method for getting all music file from external storage and add to the arraylist
    public void getVideo() {
        ContentResolver contentResolver = getContext().getContentResolver();
        Uri songUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            //getting data from external storage
            int songTitle = songCursor.getColumnIndex(MediaStore.Video.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Video.Media.ARTIST);
            int path = songCursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int id = songCursor.getColumnIndex(MediaStore.Video.Media._ID);
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String fullPath = songCursor.getString(path);
                /*
                this function for getting video thumbnail and add it to array list
                 */
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(fullPath, MediaStore.Video.Thumbnails.MINI_KIND);

                arrayList.add(new MusicList(thumb, fullPath, currentTitle.replace(".mp3", ""), currentArtist));

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
}
