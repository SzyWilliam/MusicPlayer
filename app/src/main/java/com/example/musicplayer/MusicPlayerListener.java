package com.example.musicplayer;

public interface MusicPlayerListener {

    void onProgress(int progress);

    void onPaused();

    void onFinished();
}
