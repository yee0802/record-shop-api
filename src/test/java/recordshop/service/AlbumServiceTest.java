package recordshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import recordshop.exception.ItemNotFoundException;
import recordshop.model.Album;
import recordshop.model.Genre;
import recordshop.repository.AlbumRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class AlbumServiceTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @InjectMocks
    private AlbumServiceImpl albumServiceImpl;

    @Test
    @DisplayName("getAllAlbums: should return list of albums")
    public void testGetAllAlbumsReturnsListOfAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "album1", "John", Genre.Classical, 2024, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(2L, "album2", "Jane", Genre.Blues, 1978, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(3L, "album3", "Patrick", Genre.Electronic, 1997, LocalDateTime.now(), LocalDateTime.now()));

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<Album> actualResult = albumServiceImpl.getAllAlbums();

        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(albumList);

        verify(mockAlbumRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAlbumById: should return Album")
    public void testGetAlbumByIdReturnsAnAlbum() {
        Album expectedAlbum = new Album(1L, "album", "album maker", Genre.Rock, 2000, LocalDateTime.now(), LocalDateTime.now());

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(expectedAlbum));

        Album result = albumServiceImpl.getAlbumById(1L);

        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album");
        assertThat(result).hasFieldOrPropertyWithValue("artist", "album maker");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);

        verify(mockAlbumRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getAlbumById: should throw ItemNotFoundException when trying to find an unavailable album")
    public void testGetAlbumByIdThrowsWhenNotFound() {
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> albumServiceImpl.getAlbumById(1L));

        verify(mockAlbumRepository, times(1)).findById(1L);
    }
}
