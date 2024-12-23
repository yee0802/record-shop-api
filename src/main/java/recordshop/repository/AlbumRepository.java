package recordshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recordshop.model.Album;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findAllByGenre(String genre);
    List<Album> findAllByReleaseYear(Integer releaseYear);
    List<Album> findAllByArtistName(String name);
}
