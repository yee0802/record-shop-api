package recordshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recordshop.exception.ItemNotFoundException;
import recordshop.exception.MissingFieldException;
import recordshop.model.Album;
import recordshop.repository.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    AlbumRepository albumRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();

        albumRepository.findAll().forEach(albums::add);

        return albums;
    }

    @Override
    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item with id '%s' could not be found", id)));
    }

    @Override
    public Album addAlbum(Album album) {
        boolean isValid = Stream.of(album.getName(), album.getArtist(), album.getGenre(), album.getReleaseYear())
                .allMatch(Objects::nonNull);

        if (!isValid) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        Album newAlbum = new Album();
        newAlbum.setName(album.getName());
        newAlbum.setArtist(album.getArtist());
        newAlbum.setGenre(album.getGenre());
        newAlbum.setReleaseYear(album.getReleaseYear());

        return albumRepository.save(album);
    }

    @Override
    public Album updateAlbumById(Long id, Album album) {
        Album foundAlbum = albumRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(String.format("Item with id '%s' could not be found", id)));

        boolean isValid = Stream.of(album.getName(),
                        album.getArtist(),
                        album.getGenre(),
                        album.getReleaseYear())
                .allMatch(Objects::nonNull);

        if (!isValid) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        foundAlbum.setName(album.getName());
        foundAlbum.setArtist(album.getArtist());
        foundAlbum.setGenre(album.getGenre());
        foundAlbum.setReleaseYear(album.getReleaseYear());

        return albumRepository.save(foundAlbum);
    }
}
