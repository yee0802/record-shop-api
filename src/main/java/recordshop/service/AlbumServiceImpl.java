package recordshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recordshop.exception.ItemNotFoundException;
import recordshop.model.Album;
import recordshop.repository.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if (foundAlbum.isEmpty()){
            throw new ItemNotFoundException(String.format("Item with id '%s' could not be found", id));
        }

        return foundAlbum.get();
     }
}
