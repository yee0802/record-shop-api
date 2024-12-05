package recordshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recordshop.model.Album;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
}
