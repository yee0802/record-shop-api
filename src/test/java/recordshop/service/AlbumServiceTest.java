package recordshop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import recordshop.exception.ItemNotFoundException;
import recordshop.exception.MissingFieldException;
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

    @Test
    @DisplayName("addAlbum: should return new album")
    public void testAddAlbumReturnsAlbum() {
        Album album = new Album(1L, "album", "album maker", Genre.Rock, 2000, LocalDateTime.now(), LocalDateTime.now());

        when(mockAlbumRepository.save(album)).thenReturn(album);

        Album result = albumServiceImpl.addAlbum(album);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(album);

        verify(mockAlbumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("addAlbum: should throw MissingFieldException when attempting to add a Album with missing/null fields")
    public void testAddAlbumThrowsWhenMissingFields() {
        Album invalidAlbum = new Album();
        invalidAlbum.setName(null);
        invalidAlbum.setGenre(Genre.Blues);

        when(mockAlbumRepository.save(invalidAlbum))
                .thenThrow(new MissingFieldException("Missing field(s) in request body"));

        assertThrows(MissingFieldException.class, () -> albumServiceImpl.addAlbum(invalidAlbum));
    }

    @Test
    @DisplayName("updateAlbumById: should update album")
    public void testUpdateAlbumByIdReturnsUpdatedAlbum() {
        Long albumId = 1L;
        Album existingAlbum = new Album(albumId, "Old Album", "Old Artist", Genre.Rock, 2000,
                LocalDateTime.now().minusYears(1), LocalDateTime.now().minusYears(1));

        Album updatedAlbum = new Album(albumId, "New Album", "New Artist", Genre.Classical, 2099,
                existingAlbum.getCreatedAt(), LocalDateTime.now());

        when(mockAlbumRepository.findById(albumId)).thenReturn(Optional.of(existingAlbum));
        when(mockAlbumRepository.save(any(Album.class))).thenReturn(updatedAlbum);

        Album result = albumServiceImpl.updateAlbumById(albumId, updatedAlbum);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "New Album");
        assertThat(result).hasFieldOrPropertyWithValue("artist", "New Artist");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Classical);
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2099);
        assertThat(result).hasFieldOrPropertyWithValue("createdAt", existingAlbum.getCreatedAt());
        assertThat(result).hasFieldOrPropertyWithValue("modifiedAt", updatedAlbum.getModifiedAt());

        verify(mockAlbumRepository, times(1)).findById(albumId);
        verify(mockAlbumRepository, times(1)).save(any(Album.class));
    }

    @Test
    @DisplayName("deleteById: should delete album if present")
    public void testDeleteByIdDeletesAlbum() {
        Album album = new Album(1L, "album", "album maker", Genre.Rock, 2000, LocalDateTime.now(), LocalDateTime.now());

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album));

        albumServiceImpl.deleteAlbumById(1L);

        verify(mockAlbumRepository, times(1)).findById(1L);
        verify(mockAlbumRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById: should throw ItemNotFoundException if no album is present")
    public void testDeleteByIdThrowsException() {
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> albumServiceImpl.deleteAlbumById(1L));

        verify(mockAlbumRepository, times(1)).findById(1L);
    }
}
