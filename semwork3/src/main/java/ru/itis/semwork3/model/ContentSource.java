package ru.itis.semwork3.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@DynamicInsert
@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ContentSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "varchar(31) default generate_uid(11) not null unique")
    String stringId;

    @Column(columnDefinition = "varchar(63) not null")
    String name;

    @Column(columnDefinition = "varchar(511)")
    String about;

    @ManyToMany(mappedBy = "sources")
    List<User> members;

    @OneToMany(mappedBy = "source")
    List<Message> messages;

    public abstract int getSourceType();

    public abstract int getTypeNumber();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentSource)) return false;
        ContentSource that = (ContentSource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
