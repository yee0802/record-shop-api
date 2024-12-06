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
import java.util.Optional;
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
        Optional<Album> foundAlbum = albumRepository.findById(id);

        if (foundAlbum.isEmpty()) {
            throw new ItemNotFoundException(String.format("Item with id '%s' could not be found", id));
        }

        return foundAlbum.get();
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
}
