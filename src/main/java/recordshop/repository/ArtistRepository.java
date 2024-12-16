package recordshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recordshop.model.Artist;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Artist findByName(String name);
}
