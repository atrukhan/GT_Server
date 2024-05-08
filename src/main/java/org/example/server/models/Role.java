package org.example.server.models;

import org.example.server.models.enums.ERole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ERole value;

    public Role() {
    }

    public Role(ERole value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getValue() {
        return value;
    }

    public void setValue(ERole value) {
        this.value = value;
    }
}
