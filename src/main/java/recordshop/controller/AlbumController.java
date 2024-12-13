package recordshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recordshop.model.Album;
import recordshop.service.AlbumService;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();

        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        return new ResponseEntity<>(albumService.addAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbumById(@PathVariable Long id, @RequestBody Album album) {
        return new ResponseEntity<>(albumService.updateAlbumById(id, album), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable Long id) {
        albumService.deleteAlbumById(id);

        return new ResponseEntity<>(
                String.format("Album with id '%s' successfully deleted", id),
                HttpStatus.OK);
    }
}
