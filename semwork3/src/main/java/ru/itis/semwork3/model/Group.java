package ru.itis.semwork3.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@DynamicInsert
@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"group\"")
public class Group extends ContentSource {
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int2 default 0 not null")
    State state = State.ACTIVE;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int2 default 0 not null")
    Type type;

    @ManyToOne
    @JoinColumn(name = "admin_id", columnDefinition = "varchar(31)")
    User admin;

    @Override
    public int getSourceType() {
        return 0;
    }

    @Override
    public int getTypeNumber() {
        return type.num;
    }

    public enum State {
        ACTIVE,
        BANNED,
        FROZEN
    }

    public enum Type {
        PUBLIC(0),
        PRIVATE(1);

        byte num;

        Type(int num) {
            this.num = (byte) num;
        }
    }
}
