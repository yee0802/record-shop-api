package recordshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recordshop.dto.AlbumDTO;
import recordshop.service.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        List<AlbumDTO> albums = albumService.getAllAlbums();

        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AlbumDTO> addAlbum(@RequestBody AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.addAlbum(albumDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> updateAlbumById(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        return new ResponseEntity<>(albumService.updateAlbumById(id, albumDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable Long id) {
        albumService.deleteAlbumById(id);

        return new ResponseEntity<>(
                String.format("Album with id '%s' successfully deleted", id),
                HttpStatus.OK);
    }
}
