package recordshop.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
