package recordshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import recordshop.model.Artist;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setUp() {
        Artist artistToSave = new Artist(null, "John Doe", null, LocalDateTime.now(), LocalDateTime.now());
        artistRepository.save(artistToSave);
    }

    @Test
    @DisplayName("findByName: should return artist by given name")
    public void testFindByName() {
        Artist foundArtist = artistRepository.findByName("John Doe");

        assertThat(foundArtist).isNotNull();
        assertThat(foundArtist).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(foundArtist).hasFieldOrPropertyWithValue("name",  "John Doe");
        assertThat(foundArtist).hasFieldOrPropertyWithValue("albums", null);
    }
}
