package org.example.server.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "default_libraries")
public class DefaultLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 50, unique = true, nullable = false)
    private String title;

    @Column(length = 32, unique = true, nullable = false)
    private String code;

//    @ManyToMany(mappedBy = "defaultLibraries")
//    private List<Card> cards;
    @OneToMany(mappedBy = "defaultLibrary")
    private List<Card> cards;

    public DefaultLibrary() {
    }

    public DefaultLibrary(String title, String code) {
        this.title = title;
        this.code = code;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
