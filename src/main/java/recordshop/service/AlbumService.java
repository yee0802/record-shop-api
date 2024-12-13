package recordshop.service;

import recordshop.model.Album;

import java.util.List;

public interface AlbumService {
    List<Album> getAllAlbums();
    Album getAlbumById(Long id);
    Album addAlbum(Album album);
    Album updateAlbumById(Long id, Album album);
}
