package recordshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recordshop.model.Genre;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private Long id;
    private String name;
    private ArtistDTO artist;
    private Genre genre;
    private String coverArtUrl;
    private Integer releaseYear;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
