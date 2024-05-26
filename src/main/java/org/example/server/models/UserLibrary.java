package org.example.server.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "libraries")
public class UserLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25, nullable = false)
    private String title;

    @Column(length = 32, unique = true, nullable = false)
    private String code;

//    @ManyToMany(mappedBy = "libraries")
//    private List<Card> cards;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userLibrary", cascade = CascadeType.ALL)
    private List<Card> cards;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "library_users",
//            joinColumns = @JoinColumn(name = "library_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "library", cascade = CascadeType.ALL)
    private List<Training> trainings;

    public UserLibrary() {
    }

    public UserLibrary(String title, String code, User user) {
        this.title = title;
        this.code = code;
        this.user = user;

    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
