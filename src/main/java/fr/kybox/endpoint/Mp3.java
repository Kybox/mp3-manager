package fr.kybox.endpoint;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import fr.kybox.entity.Mp3Data;
import fr.kybox.service.Mp3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class Mp3 {

    private final Mp3Service mp3Service;

    public Mp3(Mp3Service mp3Service) {
        this.mp3Service = mp3Service;
    }

    @GetMapping(value = "/loadTags", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mp3Data loadTags() {
        return this.mp3Service.getTags();
    }

    @GetMapping(value = "loadCover")
    public void loadCover(HttpServletResponse response) {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try {
            response.getOutputStream().write(this.mp3Service.getCover());
        } catch (IOException | InvalidDataException | UnsupportedTagException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/mp3")
    public void mp3(HttpServletResponse response) throws IOException {
        Path path = new File(this.getClass().getResource("/assets/Comp7.mp3").getPath()).toPath();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLength((int) Files.size(path));
        Files.copy(path, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping(value = "/genre")
    public void getGenreList(HttpServletResponse response) throws IOException {
        Path path = new File(this.getClass().getResource("/assets/genre.txt").getPath()).toPath();
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setContentLength((int) Files.size(path));
        Files.copy(path, response.getOutputStream());
        response.flushBuffer();
    }

    @PostMapping(value = "/update")
    public void update(@RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam("tags") String tags) {
        try {
            this.mp3Service.setData(file, tags);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        try {
            Path path = Path.of("Comp7-updated.mp3");
            response.setContentType(MediaType.ALL_VALUE);
            response.setContentLength((int) Files.size(path));
            response.setHeader("Content-disposition", "attachment; filename=Kybox-MP3-Manager-demo.mp3");
            Files.copy(path, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
