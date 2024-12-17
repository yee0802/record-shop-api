package recordshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}