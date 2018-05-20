package com.example.yugal.newmusicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    int min=0,sec=0,hrs=0;
    Thread play;
    ArrayList<SongInfo> songList;
    int min1=0,sec1=0;
    SongInfo info;
    Bundle b2;
//    ArrayList<String> str,str2;
    MediaPlayer player;
    boolean isPlaying=true;
    SeekBar seek;
    int size=0,pos=0;
    Button playBtn;
    TextView currTV,totalTV,infoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        currTV=findViewById(R.id.tvCurrent);
        totalTV=findViewById(R.id.tvTotal);
        infoTV=findViewById(R.id.info);
        seek=findViewById(R.id.playSeek);
        playBtn=findViewById(R.id.playbtn);
        player=new MediaPlayer();
        b2=getIntent().getExtras();
        songList=b2.getParcelableArrayList("songs");
        pos=b2.getInt("position");
        prepare();
        update();
        seek.setOnSeekBarChangeListener(this);
    }

    private void prepare() {
        size=songList.size();
        info=songList.get(pos);
        try {
            player.setDataSource(info.getData());
            player.prepare();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            infoTV.setText(info.getName());
            seek.setMax(player.getDuration());
            sec1 = (player.getDuration() / 1000) % 60;
            min1 = (player.getDuration() / 1000) / 60;
            final String tot = String.format("%02d:%02d", min1, sec1);
            totalTV.setText(tot);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    player.reset();
                    if (pos == size - 1)
                        pos = 0;
                    else
                        pos++;
                    resetAll();
                    prepare();
                    update();
                }
            });
        } catch(IOException ex){
            System.out.println(ex.getMessage());
            System.out.println("exception");
        }catch(IllegalStateException e){
            e.printStackTrace();
        }catch (NullPointerException exx){
            exx.printStackTrace();
        }

    }

    private void update() {
        play=new Thread(new Runnable() {
            @Override
            public void run() {
                while(player.isPlaying()){
                    ++sec;
                    if(sec>=60){
                        min=min+sec/60;
                        sec=sec%60;
                    }
                    final String str=String.format("%02d:%02d",min,sec);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currTV.setText(str);
                            seek.setProgress(player.getCurrentPosition());
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        play.start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            player.seekTo(progress);
            min=(progress/1000)/60;
            sec=(progress/1000)%60;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void resetAll() {
        play.interrupt();
        player.reset();
        seek.setProgress(0);
        min=0;
        sec=0;
    }

    public void play(View view) {
        if(!isPlaying){
            player.start();
            update();
            playBtn.setBackgroundResource(R.drawable.ic_pause);
            isPlaying=true;
        }
        else{
            player.pause();
            isPlaying=false;
            playBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    public void next(View view) {
        resetAll();
        if(pos==size-1) {
            pos=0;
        }
        else {
            pos++;
        }
        prepare();
        update();
    }

    public void previous(View view) {
        resetAll();
        if(pos==0) {
            pos=size-1;
        }
        else {
            pos--;
        }
        prepare();
        update();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.reset();
        player.release();
    }
}
