package recordshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import recordshop.dto.AlbumDTO;
import recordshop.dto.ArtistDTO;
import recordshop.exception.ItemNotFoundException;
import recordshop.exception.MissingFieldException;
import recordshop.model.Album;
import recordshop.model.Artist;
import recordshop.model.Genre;
import recordshop.repository.AlbumRepository;
import recordshop.repository.ArtistRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AlbumServiceTest {

    @Mock
    private AlbumRepository mockAlbumRepository;

    @Mock
    private ArtistRepository mockArtistRepository;

    @InjectMocks
    private AlbumServiceImpl albumServiceImpl;

    private Album album;
    private AlbumDTO albumDTO;
    private Artist artist;
    private ArtistDTO artistDTO;

    @BeforeEach
    public void setUp() {
        artist = new Artist(1L, "artist_name", null, LocalDateTime.now(), LocalDateTime.now());
        artistDTO = new ArtistDTO(artist.getId(), artist.getName(), artist.getCreatedAt(), artist.getModifiedAt());

        album = new Album(1L,
                "album_name",
                artist,
                Genre.Rock,
                "https://example.com/cover-art.webp",
                2000,
                99,
                LocalDateTime.now(),
                LocalDateTime.now());

        albumDTO = new AlbumDTO(1L,
                album.getName(),
                artistDTO,
                album.getGenre(),
                album.getCoverArtUrl(),
                album.getReleaseYear(),
                album.getStockQuantity(),
                album.getCreatedAt(),
                album.getModifiedAt());
    }

    @Test
    @DisplayName("getAllAlbums: should return list of albums")
    public void testGetAllAlbumsReturnsListOfAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "album1", artist, Genre.Classical, "https://example.com/cover-art.webp", 2024, 1, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(2L, "album2", artist, Genre.Blues,"https://example.com/cover-art.webp", 1978, 2, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(3L, "album3", artist, Genre.Electronic,"https://example.com/cover-art.webp", 1997, 3, LocalDateTime.now(), LocalDateTime.now()));

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<AlbumDTO> actualResult = albumServiceImpl.getAllAlbums();
        AlbumDTO albumDTO1 = actualResult.getFirst();
        AlbumDTO albumDTO2 = actualResult.get(1);
        AlbumDTO albumDTO3 = actualResult.getLast();

        assertThat(actualResult).hasSize(3);
        assertThat(albumDTO1).hasFieldOrPropertyWithValue("name", "album1");
        assertThat(albumDTO2).hasFieldOrPropertyWithValue("name", "album2");
        assertThat(albumDTO3).hasFieldOrPropertyWithValue("name", "album3");

        verify(mockAlbumRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAlbumById: should return Album")
    public void testGetAlbumByIdReturnsAnAlbum() {
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album));

        AlbumDTO result = albumServiceImpl.getAlbumById(1L);

        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);

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
    @DisplayName("addAlbum: should return new album when artist already exists")
    public void testAddAlbumReturnsAlbumWhenArtistExists() {
        when(mockArtistRepository.findByName("artist_name")).thenReturn(artist);
        when(mockAlbumRepository.save(album)).thenReturn(album);

        AlbumDTO result = albumServiceImpl.addAlbum(albumDTO);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);

        verify(mockArtistRepository, times(1)).findByName("artist_name");
        verify(mockArtistRepository, never()).save(artist);
        verify(mockAlbumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("addAlbum: should return new album and save artist when artist does not exist")
    public void testAddAlbumReturnsAlbumWhenArtistDoesNotExist() {
        when(mockArtistRepository.findByName("artist_name")).thenReturn(null);
        when(mockArtistRepository.save(artist)).thenReturn(artist);
        when(mockAlbumRepository.save(album)).thenReturn(album);

        AlbumDTO result = albumServiceImpl.addAlbum(albumDTO);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);

        verify(mockArtistRepository, times(1)).findByName("artist_name");
        verify(mockArtistRepository, times(1)).save(artist);
        verify(mockAlbumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("addAlbum: should throw MissingFieldException when attempting to add a Album with missing/null fields")
    public void testAddAlbumThrowsWhenMissingFields() {
        Album invalidAlbum = new Album();
        invalidAlbum.setGenre(Genre.Blues);

        AlbumDTO invalidAlbumDTO = new AlbumDTO();
        invalidAlbumDTO.setGenre(invalidAlbum.getGenre());

        when(mockAlbumRepository.save(invalidAlbum))
                .thenThrow(new MissingFieldException("Missing field(s) in request body"));

        assertThrows(MissingFieldException.class, () -> albumServiceImpl.addAlbum(invalidAlbumDTO));

        verify(mockAlbumRepository, never()).save(invalidAlbum);
    }

    @Test
    @DisplayName("updateAlbumById: should update album")
    public void testUpdateAlbumByIdReturnsUpdatedAlbum() {
        when(mockArtistRepository.findByName("artist_name")).thenReturn(artist);
        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(mockAlbumRepository.save(any(Album.class))).thenReturn(album);

        AlbumDTO result = albumServiceImpl.updateAlbumById(1L, albumDTO);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);
        assertThat(result).hasFieldOrPropertyWithValue("createdAt", album.getCreatedAt());
        assertThat(result).hasFieldOrPropertyWithValue("modifiedAt", album.getModifiedAt());

        verify(mockArtistRepository, times(1)).findByName("artist_name");
        verify(mockAlbumRepository, times(1)).findById(1L);
        verify(mockAlbumRepository, times(1)).save(any(Album.class));
    }

    @Test
    @DisplayName("deleteById: should delete album if present")
    public void testDeleteByIdDeletesAlbum() {
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

    @Test
    @DisplayName("mapToDTO(Album): should return album as a DTO")
    public void testMapToDTOReturnsAlbumDTO() {
        AlbumDTO result = albumServiceImpl.mapToDTO(album);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result.getArtist()).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);
    }

    @Test
    @DisplayName("mapToEntity(AlbumDTO): should return DTO as an album")
    public void AlbumService_MapToEntity_ReturnsMappedAlbum() {
        Album result = albumServiceImpl.mapToEntity(albumDTO);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "album_name");
        assertThat(result.getArtist()).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("artist.name", "artist_name");
        assertThat(result).hasFieldOrPropertyWithValue("genre", Genre.Rock);
        assertThat(result).hasFieldOrPropertyWithValue("coverArtUrl", "https://example.com/cover-art.webp");
        assertThat(result).hasFieldOrPropertyWithValue("releaseYear", 2000);
        assertThat(result).hasFieldOrPropertyWithValue("stockQuantity", 99);
    }

    @Test
    @DisplayName("mapToDTO(Artist): should return artist as a DTO")
    public void testMapToDTOReturnsArtistDTO() {
        ArtistDTO result = albumServiceImpl.mapToDTO(artist);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "artist_name");
    }

    @Test
    @DisplayName("mapToEntity(ArtistDTO): should return DTO as an artist")
    public void AlbumService_MapToEntity_ReturnsMappedDTOArtist() {
        Artist result = albumServiceImpl.mapToEntity(artistDTO);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(result).hasFieldOrPropertyWithValue("name", "artist_name");
    }
}
