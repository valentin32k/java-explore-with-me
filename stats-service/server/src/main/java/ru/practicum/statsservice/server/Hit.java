package ru.practicum.statsservice.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "hit", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "The field app can not be blanc")
    @Size(max = 255, message = "The app length must be shorter than 255 characters")
    private String app;
    @NotBlank(message = "The field uri can not be blanc")
    @Size(max = 255, message = "The uri length must be shorter than 255 characters")
    private String uri;
    @NotBlank(message = "The field ip can not be blanc")
    @Size(max = 15, message = "The ip length must be shorter than 15 characters")
    private String ip;
    @NotNull(message = "The field Timestamp can not be null")
    @PastOrPresent(message = "The field timestamp can not be on the future")
    private Timestamp timestamp;
    @Transient
    long hits;

    public Hit(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
