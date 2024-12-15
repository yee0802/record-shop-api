package recordshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recordshop.exception.ItemNotFoundException;
import recordshop.exception.MissingFieldException;
import recordshop.model.Album;
import recordshop.model.Artist;
import recordshop.repository.AlbumRepository;
import recordshop.repository.ArtistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

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
        boolean hasValidFields = requestBodyHasValidFields(album);

        if (!hasValidFields) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        Artist artist = artistRepository.findByName(album.getArtist().getName());

        if (artist == null) {
            artist = artistRepository.save(album.getArtist());
        }

        Album newAlbum = new Album();
        newAlbum.setName(album.getName());
        newAlbum.setArtist(artist);
        newAlbum.setGenre(album.getGenre());
        newAlbum.setReleaseYear(album.getReleaseYear());
        newAlbum.setStockQuantity(album.getStockQuantity());

        return albumRepository.save(album);
    }

    @Override
    public Album updateAlbumById(Long id, Album album) {
        Album foundAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item with id '%s' could not be found", id)));

        boolean hasValidFields = requestBodyHasValidFields(album);

        if (!hasValidFields) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        Artist artist = artistRepository.findByName(album.getArtist().getName());

        if (artist == null) {
            artist = artistRepository.save(album.getArtist());
        }

        foundAlbum.setName(album.getName());
        foundAlbum.setArtist(artist);
        foundAlbum.setGenre(album.getGenre());
        foundAlbum.setReleaseYear(album.getReleaseYear());
        foundAlbum.setStockQuantity(album.getStockQuantity());

        return albumRepository.save(foundAlbum);
    }

    @Override
    public void deleteAlbumById(Long id) {
        albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Item with id '%s' could not be found", id)));

        albumRepository.deleteById(id);
    }

    private boolean requestBodyHasValidFields(Album album) {
        return album.getName() != null
                && album.getGenre() != null
                && album.getReleaseYear() != null
                && album.getStockQuantity() != null
                && album.getArtist() != null
                && album.getArtist().getName() != null;
    }
}
