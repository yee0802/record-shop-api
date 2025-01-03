package recordshop.service;

import recordshop.dto.AlbumDTO;

import java.util.List;

public interface AlbumService {
    List<AlbumDTO> getAllAlbums(String genre, Integer releaseYear, String artistName);
    AlbumDTO getAlbumById(Long id);
    AlbumDTO addAlbum(AlbumDTO albumDTO);
    AlbumDTO updateAlbumById(Long id, AlbumDTO albumDTO);
    void deleteAlbumById(Long id);
}
