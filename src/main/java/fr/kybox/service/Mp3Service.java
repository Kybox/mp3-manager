package fr.kybox.service;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import fr.kybox.entity.Mp3Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface Mp3Service {

    Mp3Data getTags();
    byte[] getCover() throws InvalidDataException, IOException, UnsupportedTagException;
    void setData(MultipartFile file, String tags) throws IOException, InvalidDataException, UnsupportedTagException, NotSupportedException;
}
