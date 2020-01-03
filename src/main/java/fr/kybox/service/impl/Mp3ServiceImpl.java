package fr.kybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpatric.mp3agic.*;
import fr.kybox.entity.Mp3Data;
import fr.kybox.service.Mp3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class Mp3ServiceImpl implements Mp3Service {

    private final ObjectMapper objectMapper;

    public Mp3ServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mp3File load() throws InvalidDataException, IOException, UnsupportedTagException {
        return new Mp3File("src/main/resources/assets/Comp7.mp3");
    }

    @Override
    public Mp3Data getTags() {

        try {
            Mp3File mp3File = this.load();
            ID3v2 mp3Tags = mp3File.getId3v2Tag();
            Mp3Data mp3Data = new Mp3Data();

            mp3Data.setAlbum(mp3Tags.getAlbum());
            mp3Data.setArtist(mp3Tags.getArtist());
            mp3Data.setTitle(mp3Tags.getTitle());
            mp3Data.setComposer(mp3Tags.getComposer());
            mp3Data.setGenre(mp3Tags.getGenre());
            mp3Data.setGenreDesc(mp3Tags.getGenreDescription());
            mp3Data.setTrack(mp3Tags.getTrack());
            mp3Data.setYear(mp3Tags.getYear());
            mp3Data.setCover(mp3Tags.getAlbumImage());

            return mp3Data;

        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] getCover() throws InvalidDataException, IOException, UnsupportedTagException {
        return load().getId3v2Tag().getAlbumImage();
    }

    @Override
    public void setData(MultipartFile file, String tags)
            throws IOException, InvalidDataException, UnsupportedTagException, NotSupportedException {

        Mp3Data mp3Data = this.objectMapper.readValue(tags, Mp3Data.class);
        Mp3File mp3File = this.load();
        ID3v2 mp3Tags = mp3File.getId3v2Tag();

        mp3Tags.setAlbum(mp3Data.getAlbum());
        mp3Tags.setTrack(mp3Data.getTrack());
        mp3Tags.setTitle(mp3Data.getTitle());
        mp3Tags.setArtist(mp3Data.getArtist());
        mp3Tags.setComposer(mp3Data.getComposer());
        mp3Tags.setYear(mp3Data.getYear());
        mp3Tags.setGenre(mp3Data.getGenre());
        mp3Tags.setCopyright("yann@kybox.fr");
        mp3Tags.setEncoder("Mp3agic");
        mp3Tags.setUrl("https://kybox.fr");
        if(file != null)
            mp3Tags.setAlbumImage(file.getBytes(), "Cover");
        mp3File.save("./src/main/resources/assets/Comp7-updated.mp3");
    }
}
