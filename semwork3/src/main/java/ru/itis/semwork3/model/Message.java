package ru.itis.semwork3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "source_id", nullable = false)
    ContentSource source;

    @ManyToOne
    @JoinColumn(name = "author_id")
    ContentSource author;

    @ManyToOne
    @JoinColumn(name = "from_id")
    ContentSource from;

    @Column(length = 1024, nullable = false)
    String text;
}
