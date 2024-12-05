package recordshop.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import recordshop.model.Album;
import recordshop.model.Genre;
import recordshop.repository.AlbumRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class AlbumServiceTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @InjectMocks
    private AlbumServiceImpl albumServiceImpl;

    @Test
    public void testGetAllAlbumsReturnsAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "album1", "John", Genre.Classical, 2024, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(2L, "album2", "Jane", Genre.Blues, 1978, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(3L, "album3", "Patrick", Genre.Electronic, 1997, LocalDateTime.now(), LocalDateTime.now()));

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<Album> actualResult = albumServiceImpl.getAllAlbums();

        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(albumList);
    }
}
