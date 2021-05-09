package ru.itis.semwork3.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@DynamicInsert
@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "\"user\"")
public class User extends ContentSource{
    @Column(columnDefinition = "bpchar(60) not null")
    String password;

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int2 default 0 not null")
    State state = State.NOT_CONFIRMED;

    @Column(columnDefinition = "varchar(255) unique not null")
    String email;

    @Column(columnDefinition = "varchar(63) not null")
    String surname;

    @Column(columnDefinition = "bpchar(11) not null unique")
    String phone;

    @Column(columnDefinition = "char(36) unique")
    String confirm;

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int2 default 0 not null")
    Role role = Role.USER;

    @ManyToMany(mappedBy = "members")
    List<ContentSource> sources;

    @Override
    public int getSourceType() {
        return 2;
    }

    @Override
    public int getTypeNumber() {
        return 0;
    }

    public enum State {
        NOT_CONFIRMED,
        CONFIRMED,
        BANNED
    }

    public enum Role {
        USER,
        ADMIN
    }

    public User() {
    }
}
