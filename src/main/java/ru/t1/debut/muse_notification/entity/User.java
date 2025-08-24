package ru.t1.debut.muse_notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
