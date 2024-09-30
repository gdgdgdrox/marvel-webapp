package server.marvel.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="character_info")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MarvelCharacter {
    @Id
    private Integer id;

    private String name;

    private String description;

    @Column(name="image")
    private String imageURL;
}
