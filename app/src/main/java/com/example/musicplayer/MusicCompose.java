package com.example.musicplayer;

public class MusicCompose {

    private String title;
    private String author;

    private String imageName;
    private String mp3Name;

    public MusicCompose(String title, String author, String imageName, String mp3Name){
        this.title = title;
        this.author = author;
        this.imageName = imageName;
        this.mp3Name = mp3Name;
    }

    public MusicCompose(String title, String author){
        this(title, author, Utility.IMAGE_ROOT, Utility.MUSIC_ROOT);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getFullImagePath(){
        return Utility.IMAGE_ROOT + "/" + imageName;
    }

    public String getFullMp3FileName(){
        return Utility.MUSIC_ROOT + "/" + mp3Name;
    }
}
