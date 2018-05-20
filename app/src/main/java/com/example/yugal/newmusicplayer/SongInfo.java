package com.example.yugal.newmusicplayer;

import android.os.Parcel;
import android.os.Parcelable;

public class SongInfo implements Parcelable {

    private String name;
    private String artist;
    private String data;

    public SongInfo(String name, String artist, String data) {
        this.name = name;
        this.artist = artist;
        this.data = data;
    }

    protected SongInfo(Parcel in) {
        name = in.readString();
        artist = in.readString();
        data = in.readString();
    }

    public static final Creator<SongInfo> CREATOR = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(data);
    }
}
