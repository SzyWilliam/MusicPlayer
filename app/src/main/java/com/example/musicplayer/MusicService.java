package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {

    MusicTask musicTask;
    MediaPlayer mediaPlayer;


    //this class is used to communicate with the AsncTask
    private MusicPlayerListener listener = new MusicPlayerListener() {
        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onFinished() {

        }
    };

    //this class is used to communication with the main thread
    class MusicPlayerBinder extends Binder {

        /* the media player should be already prepared*/
        public void startPlaying(MediaPlayer mediaPlayer){

        }

        public void pausePlaying(){

        }

        public void resumePlaying(){

        }

        public void stopPlaying(){

        }

        public int getProcess(){
            return 0;
        }
    }





    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
