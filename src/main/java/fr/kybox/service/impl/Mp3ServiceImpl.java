package fr.kybox.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpatric.mp3agic.*;
import fr.kybox.entity.Mp3Data;
import fr.kybox.service.Mp3Service;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class Mp3ServiceImpl implements Mp3Service {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public Mp3ServiceImpl(ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public Mp3File load() throws InvalidDataException, IOException, UnsupportedTagException {
        return new Mp3File(new File(this.getClass().getResource("/assets/Comp7.mp3").getPath()).getPath());
    }

    @Override
    public Mp3Data getTags() {

        try {
            Mp3File mp3File = this.load();
            ID3v2 mp3Tags = mp3File.getId3v2Tag();
            Mp3Data mp3Data = new Mp3Data();

            this.modelMapper.map(mp3Tags, mp3Data);

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

        this.modelMapper.map(mp3Data, mp3Tags);

        mp3Tags.setCopyright("Open source");
        mp3Tags.setEncoder("Mp3agic");
        mp3Tags.setUrl("https://kybox.fr");
        if (file != null)
            mp3Tags.setAlbumImage(file.getBytes(), "Cover");

        mp3File.save("./Comp7-updated.mp3");
    }
}
