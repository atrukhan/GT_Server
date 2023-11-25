package org.example.server.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(length = 5)
    private String code;

    @Column(name = "word_card")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "library_words",
            joinColumns = @JoinColumn(name = "library_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<WordCard> cards;

    public Library() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<WordCard> getCards() {
        return cards;
    }

    public void setCards(List<WordCard> cards) {
        this.cards = cards;
    }
}
