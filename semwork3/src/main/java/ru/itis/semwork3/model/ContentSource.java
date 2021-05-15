package ru.itis.semwork3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@DynamicInsert
@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class ContentSource {
    @Id
    @Column(columnDefinition = "varchar(31) default generate_uid(11) not null unique")
    String id;

    @Column(columnDefinition = "varchar(63) not null")
    String name;

    @Column(columnDefinition = "varchar(511)")
    String about;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name = "user_source",
            joinColumns = {@JoinColumn(name = "source_id", referencedColumnName = "id", columnDefinition = "varchar(31)")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "varchar(31)")})
    List<User> members;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "source")
    List<Message> messages;

    @Transient
    Integer subsAmount;

    public int getSourceType() {
        throw new UnsupportedOperationException();
    }

    public int getTypeNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentSource)) return false;
        ContentSource that = (ContentSource) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
