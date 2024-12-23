package recordshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import recordshop.model.Album;
import recordshop.model.Artist;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private Album album1;
    private Album album2;

    @BeforeEach
    public void setUp() {
        Artist artistToSave = new Artist(null, "John Doe", null, LocalDateTime.now(), LocalDateTime.now());
        Artist artist = artistRepository.save(artistToSave);

        Album albumToSave1 = new Album(null,
            "album_name1",
            artist,
            "Electronic",
            "https://example.com/cover-art1.webp",
            1999,
            12,
            LocalDateTime.now(),
            LocalDateTime.now());

        Album albumToSave2 = new Album(null,
                "album_name2",
                artist,
                "Rock",
                "https://example.com/cover-art2.webp",
                2020,
                88,
                LocalDateTime.now(),
                LocalDateTime.now());

        album1 = albumRepository.save(albumToSave1);
        album2 = albumRepository.save(albumToSave2);
    }

    @Test
    @DisplayName("findAllByGenre: should find all albums by a given genre")
    public void testFindAllByGenre() {
        List<Album> foundAlbums = albumRepository.findAllByGenre("Rock");

        assertThat(foundAlbums).isNotNull();
        assertThat(foundAlbums).hasSize(1);
        assertThat(foundAlbums).contains(album2);
    }

    @Test
    @DisplayName("findAllByReleaseYear: should find all albums by a given release year")
    public void testFindAllByReleaseYear() {
        List<Album> foundAlbums = albumRepository.findAllByReleaseYear(1999);

        assertThat(foundAlbums).isNotNull();
        assertThat(foundAlbums).hasSize(1);
        assertThat(foundAlbums).contains(album1);
    }

    @Test
    @DisplayName("findAllByArtistName: should find all albums by a given artist's name")
    public void testFindAllByArtistName() {
        List<Album> foundAlbums = albumRepository.findAllByArtistName("John Doe");

        assertThat(foundAlbums).isNotNull();
        assertThat(foundAlbums).hasSize(2);
        assertThat(foundAlbums).extracting(Album::getName)
                .containsExactlyInAnyOrder("album_name1", "album_name2");
    }
}
