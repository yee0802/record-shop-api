package recordshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import recordshop.model.Album;
import recordshop.model.Genre;
import recordshop.service.AlbumServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(albumController).build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /albums returns all albums")
    public void testGetAllAlbumsReturnsAlbums() throws Exception {
        List<Album> albumList = new ArrayList<>();
        albumList.add(new Album(1L, "album1", "John", Genre.Classical, 2024, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(2L, "album2", "Jane", Genre.Blues, 1978, LocalDateTime.now(), LocalDateTime.now()));
        albumList.add(new Album(3L, "album3", "Patrick", Genre.Electronic, 1997, LocalDateTime.now(), LocalDateTime.now()));

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
    @DisplayName("GET /albums/:id returns album")
    public void testGetAlbumByIdReturnsAlbum() throws Exception {
        Album expectedAlbum = new Album(1L, "album", "John", Genre.Classical, 2024, LocalDateTime.now(), LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(formatter);

        when(mockAlbumServiceImpl.getAlbumById(1L)).thenReturn(expectedAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("album"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(String.valueOf(Genre.Classical)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2024))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(formattedTime))
                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt").value(formattedTime));
    }
}
