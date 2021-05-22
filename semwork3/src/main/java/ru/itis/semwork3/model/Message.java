package ru.itis.semwork3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Builder.Default
    @Column(nullable = false)
    Timestamp created = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "source_id", columnDefinition = "varchar(32) not null")
    ContentSource source;

    @ManyToOne
    @JoinColumn(name = "author_id", columnDefinition = "varchar(31)")
    @Fetch(FetchMode.JOIN)
    ContentSource author;

    @ManyToOne
    @JoinColumn(name = "from_id", columnDefinition = "varchar(31)")
    @Fetch(FetchMode.JOIN)
    ContentSource from;

    @Column(length = 1024, nullable = false)
    String text;
}
