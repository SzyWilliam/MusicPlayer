package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    public ConstraintLayout layout; //the overall constraint layout
    public CircleImageView centerImage;
    public TextView titleText;
    public TextView authorText;
    public ImageView previousSong;
    public ImageView nextSong;
    public ImageView status;

    public ProgressBar progressBar;
    public TextView curTimeText;
    public TextView durationText;
    public Thread curThread;
    public boolean curThreadPause = false;

    public MediaPlayer mediaPlayer = new MediaPlayer();
    public boolean musicPlaying = true;
    public int listIndex = 0;

    @Override
    @TargetApi(16)
    protected void onCreate(Bundle savedInstanceState) {
        Utility.initialize();
        super.onCreate(savedInstanceState);

        // to make the action bars blending into the environment
        if (Build.VERSION.SDK_INT >= 21){
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // check for permission of writing external storage
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }


        setContentView(R.layout.main_song_layout);
        layout = (ConstraintLayout) findViewById(R.id.main_song_layout);
        centerImage = (CircleImageView) findViewById(R.id.song_image);
        titleText = (TextView) findViewById(R.id.song_title);
        authorText = (TextView) findViewById(R.id.song_author);
        previousSong = (ImageView) findViewById(R.id.previous_song_view);
        nextSong = (ImageView) findViewById(R.id.next_song_view);
        status = (ImageView) findViewById(R.id.status_view);
        progressBar = (ProgressBar) findViewById(R.id.duration_processbar);
        curTimeText = (TextView) findViewById(R.id.text_time_cur);
        durationText = (TextView)findViewById(R.id.text_time_duration);


        registerFlowControllers();


        changeSong(Utility.composes.get(0));



        layout.setFitsSystemWindows(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this, "Permission denied. Unable to Use", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @TargetApi(16)
    public void changeSong(MusicCompose compose){
        /* 1 change the background color*/
        Bitmap  bitmap = null;
        try{
            bitmap = BitmapFactory.decodeStream(new FileInputStream(new File( compose.getFullImagePath())));
        } catch (Exception e){
            e.printStackTrace();
        }
        Bitmap bluredBitmap = BlurUtil.doBlur(bitmap,10,5);
        layout.setBackground(new BitmapDrawable(bluredBitmap));

        /* 2 change the center image */
        centerImage.setImageBitmap(bitmap);

        /* 3 change the song title and author */
        titleText.setText(compose.getTitle());
        authorText.setText(compose.getAuthor());

        /* 4 change the media player and be prepared*/
        try{
            File musicFile = new File(compose.getFullMp3FileName());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicFile.getPath());
            mediaPlayer.prepare();
            //mediaPlayer.start();
            if(!musicPlaying) status.callOnClick();
            musicPlaying = true;
        } catch (IOException e){
            e.printStackTrace();
        }

        /* 5 change the time display text and progressbar*/
        int duration = mediaPlayer.getDuration()/1000;
        int min = duration / 60;
        int sec = duration % 60;
        String secsep = sec >= 10 ? "" : "0";
        curTimeText.setText(new String("00:00"));
        durationText.setText(new String("0" + min + ":"+ secsep + sec));
        progressBar.setProgress(0);

        /* prepare the thread */
        if(curThread != null) curThread.interrupt();
        curThread = TimeCountThread(duration);
        curThread.start();
        mediaPlayer.start();
        curThreadPause = false;




    }

    public void registerFlowControllers(){
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexDecr();
                changeSong(Utility.composes.get(listIndex));
            }
        });

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexIncr();
                changeSong(Utility.composes.get(listIndex));
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicPlaying){
                    musicPlaying = false;
                    mediaPlayer.pause();
                    status.setImageResource(R.drawable.play);
                    curThreadPause = true;

                }else{
                    musicPlaying = true;
                    mediaPlayer.start();
                    status.setImageResource(R.drawable.suspended);
                    curThreadPause = false;

                }
            }
        });
    }

    private void indexIncr() {listIndex++; listIndex = (listIndex + Utility.composes.size()) % Utility.composes.size();}
    private void indexDecr() {listIndex--; listIndex = (listIndex + Utility.composes.size()) % Utility.composes.size();}

    Thread TimeCountThread(final int seconds){

        return new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                all:while(count < seconds){
                    while(curThreadPause){
                        try {
                            Thread.sleep(2);
                        }catch (InterruptedException r){
                            break all;
                        }

                    }
                    try{
                        Thread.sleep(1000);
                        count++;
                    }catch (InterruptedException e){
                        break;
                    }
                    Message message = new Message();
                    message.arg1 = count;
                    message.arg2 = seconds;
                    timeTickHandler.sendMessage(message);

                    if(Thread.interrupted()){
                        break;
                    }
                }
            }
        });
    }

    public Handler timeTickHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int currentSec = msg.arg1;
            int allSec = msg.arg2;
            int min = currentSec / 60;
            int sec = currentSec % 60;
            String secsep = sec >= 10 ? "" : "0";
            String curTime = "0" + min + ":" + secsep +sec;

            curTimeText.setText(curTime);
            progressBar.setProgress(100 * currentSec / allSec);
        }
    };

}
