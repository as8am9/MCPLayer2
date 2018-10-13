package com.example.abomaher.mcplayer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import java.io.Serializable;

public  class MusicList  {
    String songTitle ;
    String songArtist ;
    String songPath;
    Bitmap musicArt;
    public MusicList (Bitmap MusicArt,String SongPath ,String SongTitle, String SongArtist)
    {
        this.musicArt=MusicArt;
        this.songPath = SongPath;
        this.songArtist=SongArtist;
        this.songTitle=SongTitle;
    }

    protected MusicList(Parcel in) {
        songTitle = in.readString();
        songArtist = in.readString();
        songPath = in.readString();
        musicArt = in.readParcelable(Bitmap.class.getClassLoader());
    }
    public String getSongPath() {
        return songPath;
    }
    public String getSongArtist() {
        return songArtist;
    }
    public String getSongTitle() {
        return songTitle;
    }
    public Bitmap getMusicArt() {
        return musicArt;
    }
}
