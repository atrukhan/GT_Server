package org.example.server.models;

import org.example.server.models.enums.ELanguageLevel;

import javax.persistence.*;

@Entity
@Table(name = "language_levels")
public class LanguageLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ELanguageLevel level;

    public LanguageLevel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ELanguageLevel getLevel() {
        return level;
    }

    public void setLevel(ELanguageLevel level) {
        this.level = level;
    }
}
