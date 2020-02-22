package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

public class MusicTask extends AsyncTask<Void, Integer, Integer> {


    private MediaPlayer mediaPlayer;
    private MusicPlayerListener listener; //communications between task and service
    private boolean suspended; // pause
    private boolean finished;  // stop
    private int duration;      // in millisecond
    private int currentTime;   // in millisecond
    private int currentProgress;

    public MusicTask(MediaPlayer mediaPlayer, MusicPlayerListener listener){
        this.mediaPlayer = mediaPlayer;
        this.listener = listener;
        suspended = false;
        duration = mediaPlayer.getDuration();
        currentTime = 0;
        currentProgress = 0;
        finished = false;

    }

    @Override
    protected Integer doInBackground(Void... voids) {
        mediaPlayer.start();   // begin the compose
        currentTime = mediaPlayer.getCurrentPosition();

        try{
            while(currentTime < duration){
                if(finished){  // task is completed
                    mediaPlayer.stop();
                    break;
                }

                if(suspended){ // temporarily stopped
                    mediaPlayer.pause();
                    Thread.sleep(25);
                    continue;
                }

                // else, normally executing
                Thread.sleep(1000);
                currentTime = mediaPlayer.getCurrentPosition();
                currentProgress = (int)((double)currentTime * 100 / duration);
                publishProgress(currentProgress);

            }
        }catch (InterruptedException e){
            e.printStackTrace();
            Log.e("MusicTask", "doInBackground ", e);
        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        listener.onFinished();
    }

    public void pause(){
        suspended = true;
    }

    public void resume(){
        suspended = false;
    }

    public void stop(){
        finished = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

}
