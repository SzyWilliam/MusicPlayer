package com.example.musicplayer;

import java.util.ArrayList;

public class Utility {
    public static final String MUSIC_ROOT = "/data/data/com.example.musicplayer/cache/songs";
    public static final String IMAGE_ROOT = "/data/data/com.example.musicplayer/cache/images";

    public static ArrayList<MusicCompose> composes;

    public static void initialize(){
        composes = new ArrayList<>();
        composes.add(new MusicCompose("Red", "Taylor Swift", "red.jpg", "Taylor Swift-Red.mp3"));
        composes.add(new MusicCompose("Safe and Sound", "Taylor Swift", "safe_and_sound.jpg", "Taylor Swift-Safe & Sound.mp3"));
        composes.add(new MusicCompose("Reputation", "Taylor Swift", "love_story.jpg", "Taylor Swift-Love Story.mp3"));
    }

}
