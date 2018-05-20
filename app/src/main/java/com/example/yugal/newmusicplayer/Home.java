package com.example.yugal.newmusicplayer;

import android.Manifest;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends ListActivity {

    private final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ArrayList<SongInfo> songList;
    ArrayList<String> displayList;
    SongInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permission,Requesting..", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        //Toast.makeText(this, "Permission already Granted", Toast.LENGTH_SHORT).show();
        getSongList();

    }

    private void getSongList() {
        displayList=new ArrayList<>();
        songList = new ArrayList<>();
        String[] req={MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DATA};
        Uri uri=Uri.parse("content://media/external/audio/media");
        ContentResolver r=getContentResolver();
        Cursor cur=r.query(uri,req,null,null,MediaStore.Audio.Media.TITLE);
        Toast.makeText(this,"total "+cur.getCount(),Toast.LENGTH_SHORT).show();
        while(cur.moveToNext()){
            String name=cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String data=cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
            displayList.add(name+" - "+artist);
            info=new SongInfo(name,artist,data);
            songList.add(info);
        }
        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,displayList);
        super.setListAdapter(ad);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent play=new Intent(this,MusicPlayer.class);
        Bundle b=new Bundle();
        b.putParcelableArrayList("songs",songList);
        b.putInt("position",position);
        play.putExtras(b);
        startActivity(play);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                    getSongList();
                }
        }
    }
}