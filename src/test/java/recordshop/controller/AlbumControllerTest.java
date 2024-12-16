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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import recordshop.model.Album;
import recordshop.model.Artist;
import recordshop.model.Genre;
import recordshop.service.AlbumServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
public class AlbumControllerTest {
    @Mock
    private AlbumServiceImpl mockAlbumServiceImpl;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvcController;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(albumController).build();
    }

    @Test
    @DisplayName("GET /albums - returns all albums")
    public void testGetAllAlbumsReturnsAlbums() throws Exception {
        Artist artist = new Artist(1L, "artist", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "album1", artist, Genre.Classical, 2024, 1, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(2L, "album2", artist, Genre.Blues, 1978, 2, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(3L, "album3", artist, Genre.Electronic, 1997, 3, LocalDateTime.now(), LocalDateTime.now()));

        when(mockAlbumServiceImpl.getAllAlbums()).thenReturn(albumList);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("album1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("album2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id  ").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("album3"));
    }

    @Test
    @DisplayName("GET /albums/:id - returns album")
    public void testGetAlbumByIdReturnsAlbum() throws Exception {
        Album album = new Album(1L, "album", new Artist(), Genre.Classical, 2024, 99, null, null);

        when(mockAlbumServiceImpl.getAlbumById(1L)).thenReturn(album);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("album"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(String.valueOf(Genre.Classical)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(99));
    }

    @Test
    @DisplayName("POST /albums - should persist & return new album")
    public void testAddAlbumReturnsNewAlbum() throws Exception {
        Album album = new Album(1L, "album", null, Genre.Classical, 2024, 99, null, null);

        when(mockAlbumServiceImpl.addAlbum(album)).thenReturn(album);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/albums")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJSON(album)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("album"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(String.valueOf(Genre.Classical)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(99));
    }

    @Test
    @DisplayName("PUT /albums/:id - should return updated album")
    public void testUpdateAlbumByIdReturnsUpdatedAlbum() throws Exception {
        Long albumId = 1L;
        Album updatedAlbum = new Album(
                albumId,
                "updated album name",
                new Artist(),
                Genre.Classical,
                2025,
                99,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );

        when(mockAlbumServiceImpl.updateAlbumById(eq(albumId), any(Album.class))).thenReturn(updatedAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/albums/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJSON(updatedAlbum)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(albumId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("updated album name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Classical"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2025))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stockQuantity").value(99));
    }

    @Test
    @DisplayName("DELETE /albums/:id - should return string stating album was deleted")
    public void testDeleteAlbumById() throws Exception {
        doNothing().when(mockAlbumServiceImpl).deleteAlbumById(1L);

        this.mockMvcController.perform(MockMvcRequestBuilders.delete("/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Album with id '1' successfully deleted"));
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
