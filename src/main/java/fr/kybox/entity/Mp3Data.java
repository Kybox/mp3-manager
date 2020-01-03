package fr.kybox.entity;

import lombok.Data;

@Data
public class Mp3Data {
    String track;
    String artist;
    String title;
    String album;
    String year;
    int genre;
    String genreDesc;
    String composer;
    byte[] cover;
}
