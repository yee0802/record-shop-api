package recordshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import recordshop.dto.AlbumDTO;
import recordshop.dto.ArtistDTO;
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
    public List<AlbumDTO> getAllAlbums(String genre, Integer releaseYear, String artistName) {
        List<Album> albums = new ArrayList<>();

        if (genre != null) {
            albums.addAll(albumRepository.findAllByGenre(genre));
        } else if (releaseYear != null) {
            albums.addAll(albumRepository.findAllByReleaseYear(releaseYear));
        } else if (artistName != null) {
            albums.addAll(albumRepository.findAllByArtistName(artistName));
        } else {
            albumRepository.findAll().forEach(albums::add);
        }

        return albums.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "albums", key = "#id")
    public AlbumDTO getAlbumById(Long id) {
        Album foundAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Album with id '%s' could not be found", id)));

        return mapToDTO(foundAlbum);
    }

    @Override
    public AlbumDTO addAlbum(AlbumDTO albumDTO) {
        boolean hasValidFields = requestBodyHasValidFields(albumDTO);

        if (!hasValidFields) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        Artist artist = artistRepository.findByName(albumDTO.getArtist().getName());

        if (artist == null) {
            artist = artistRepository.save(mapToEntity(albumDTO.getArtist()));
        }

        Album album = mapToEntity(albumDTO);
        album.setArtist(artist);

        Album savedAlbum = albumRepository.save(album);

        return mapToDTO(savedAlbum);
    }

    @Override
    @CachePut(cacheNames = "albums", key = "#id")
    public AlbumDTO updateAlbumById(Long id, AlbumDTO albumDTO) {
        Album foundAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Album with id '%s' could not be found", id)));

        boolean hasValidFields = requestBodyHasValidFields(albumDTO);

        if (!hasValidFields) {
            throw new MissingFieldException("Missing field(s) in request body");
        }

        Artist artist = artistRepository.findByName(albumDTO.getArtist().getName());

        if (artist == null) {
            throw new ItemNotFoundException(String.format("Artist with id '%s' could not be found", id));
        }

        foundAlbum.setName(albumDTO.getName());
        foundAlbum.setArtist(artist);
        foundAlbum.setGenre(albumDTO.getGenre());

        if (albumDTO.getCoverArtUrl() != null) {
            foundAlbum.setCoverArtUrl(albumDTO.getCoverArtUrl());
        }

        foundAlbum.setReleaseYear(albumDTO.getReleaseYear());
        foundAlbum.setStockQuantity(albumDTO.getStockQuantity());

        Album updatedAlbum = albumRepository.save(foundAlbum);

        return mapToDTO(updatedAlbum);
    }

    @Override
    @CacheEvict(cacheNames = "albums", key = "#id")
    public void deleteAlbumById(Long id) {
        albumRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Album with id '%s' could not be found", id)));

        albumRepository.deleteById(id);
    }

    private boolean requestBodyHasValidFields(AlbumDTO albumDTO) {
        return albumDTO.getName() != null
                && albumDTO.getGenre() != null
                && albumDTO.getReleaseYear() != null
                && albumDTO.getStockQuantity() != null
                && albumDTO.getArtist() != null
                && albumDTO.getArtist().getName() != null;
    }

    AlbumDTO mapToDTO(Album album){
        AlbumDTO albumDTO = new AlbumDTO();

        albumDTO.setId(album.getId());
        albumDTO.setName(album.getName());
        albumDTO.setGenre(album.getGenre());
        albumDTO.setArtist(mapToDTO(album.getArtist()));
        albumDTO.setCoverArtUrl(album.getCoverArtUrl());
        albumDTO.setReleaseYear(album.getReleaseYear());
        albumDTO.setStockQuantity(album.getStockQuantity());
        albumDTO.setCreatedAt(album.getCreatedAt());
        albumDTO.setModifiedAt(album.getModifiedAt());

        return albumDTO;
    }

    ArtistDTO mapToDTO(Artist artist){
        ArtistDTO artistDTO = new ArtistDTO();

        artistDTO.setId(artist.getId());
        artistDTO.setName(artist.getName());
        artistDTO.setCreatedAt(artist.getCreatedAt());
        artistDTO.setModifiedAt(artist.getModifiedAt());

        return  artistDTO;
    }

    Album mapToEntity(AlbumDTO albumDTO) {
        Album album = new Album();

        album.setId(albumDTO.getId());
        album.setName(albumDTO.getName());
        album.setArtist(mapToEntity(albumDTO.getArtist()));
        album.setGenre(albumDTO.getGenre());
        album.setCoverArtUrl(albumDTO.getCoverArtUrl());
        album.setReleaseYear(albumDTO.getReleaseYear());
        album.setStockQuantity(albumDTO.getStockQuantity());
        album.setCreatedAt(albumDTO.getCreatedAt());
        album.setModifiedAt(albumDTO.getModifiedAt());

        return album;
    }

    Artist mapToEntity(ArtistDTO artistDTO) {
        Artist artist = new Artist();

        artist.setId(artistDTO.getId());
        artist.setName(artistDTO.getName());
        artist.setCreatedAt(artistDTO.getCreatedAt());
        artist.setModifiedAt(artistDTO.getModifiedAt());

        return artist;
    }
}
