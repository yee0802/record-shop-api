package recordshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import recordshop.dto.AlbumDTO;
import recordshop.dto.ArtistDTO;
import recordshop.model.Album;
import recordshop.model.Artist;
import recordshop.model.Genre;
import recordshop.service.AlbumServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class AlbumControllerTest {
    @Mock
    private AlbumServiceImpl mockAlbumServiceImpl;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvcController;

    private AlbumDTO albumDTO;
    private ArtistDTO artistDTO;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(albumController).build();

        var artist = new Artist(1L, "artist_name", null, LocalDateTime.now(), LocalDateTime.now());
        var album = new Album(1L,
                "album_name",
                artist,
                Genre.Electronic,
                "https://example.com/cover-art.webp",
                2020,
                88,
                LocalDateTime.now(),
                LocalDateTime.now());

        artistDTO = new ArtistDTO(artist.getId(),
                artist.getName(),
                artist.getCreatedAt(),
                artist.getModifiedAt());

        albumDTO = new AlbumDTO(album.getId(),
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
    @DisplayName("GET /albums - returns all albums")
    public void testGetAllAlbumsReturnsAlbums() throws Exception {
        List<AlbumDTO> albumDTOList = new ArrayList<>();
        albumDTOList.add(new AlbumDTO(1L, "album1", artistDTO, Genre.Classical, "https://example.com/cover-art.webp", 2024, 1, LocalDateTime.now(), LocalDateTime.now()));
        albumDTOList.add(new AlbumDTO(2L, "album2", artistDTO, Genre.Blues,"https://example.com/cover-art.webp", 1978, 2, LocalDateTime.now(), LocalDateTime.now()));
        albumDTOList.add(new AlbumDTO(3L, "album3", artistDTO, Genre.Electronic,"https://example.com/cover-art.webp", 1997, 3, LocalDateTime.now(), LocalDateTime.now()));

        when(mockAlbumServiceImpl.getAllAlbums()).thenReturn(albumDTOList);

        this.mockMvcController.perform(get("/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("album1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("album2"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("album3"));
    }

    @Test
    @DisplayName("GET /albums/:id - returns album")
    public void testGetAlbumByIdReturnsAlbum() throws Exception {
        when(mockAlbumServiceImpl.getAlbumById(1L)).thenReturn(albumDTO);

        this.mockMvcController.perform(get("/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("album_name"))
                .andExpect(jsonPath("$.genre").value(String.valueOf(Genre.Electronic)))
                .andExpect(jsonPath("$.coverArtUrl").value("https://example.com/cover-art.webp"))
                .andExpect(jsonPath("$.releaseYear").value(2020))
                .andExpect(jsonPath("$.stockQuantity").value(88));
    }

    @Test
    @DisplayName("POST /albums - should persist & return new album")
    public void testAddAlbumReturnsNewAlbum() throws Exception {
        when(mockAlbumServiceImpl.addAlbum(albumDTO)).thenReturn(albumDTO);

        this.mockMvcController.perform(post("/albums")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJSON(albumDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("album_name"))
                .andExpect(jsonPath("$.genre").value(String.valueOf(Genre.Electronic)))
                .andExpect(jsonPath("$.coverArtUrl").value("https://example.com/cover-art.webp"))
                .andExpect(jsonPath("$.releaseYear").value(2020))
                .andExpect(jsonPath("$.stockQuantity").value(88));
    }

    @Test
    @DisplayName("PUT /albums/:id - should return updated album")
    public void testUpdateAlbumByIdReturnsUpdatedAlbum() throws Exception {
        when(mockAlbumServiceImpl.updateAlbumById(eq(1L), any(AlbumDTO.class))).thenReturn(albumDTO);

        this.mockMvcController.perform(put("/albums/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJSON(albumDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("album_name"))
                .andExpect(jsonPath("$.genre").value(String.valueOf(Genre.Electronic)))
                .andExpect(jsonPath("$.coverArtUrl").value("https://example.com/cover-art.webp"))
                .andExpect(jsonPath("$.releaseYear").value(2020))
                .andExpect(jsonPath("$.stockQuantity").value(88));
    }

    @Test
    @DisplayName("DELETE /albums/:id - should return string stating album was deleted")
    public void testDeleteAlbumById() throws Exception {
        doNothing().when(mockAlbumServiceImpl).deleteAlbumById(1L);

        this.mockMvcController.perform(delete("/albums/1"))
                .andExpect(status().isNoContent());
    }

    private String toJSON(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
