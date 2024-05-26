package org.example.server.models;

import javax.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 25)
    private String value;

    @Column(length = 25)
    private String transcription;

    @Column(nullable = false, length = 50)
    private String translation;
    @Column(length = 50)
    private String example;

    @ManyToOne
    @JoinColumn(name="training_id")
    private Training training;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "card_default_libraries",
//            joinColumns = @JoinColumn(name = "card_id"),
//            inverseJoinColumns = @JoinColumn(name = "default_library_id"))
//    private List<DefaultLibrary> defaultLibraries;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "card_libraries",
//            joinColumns = @JoinColumn(name = "card_id"),
//            inverseJoinColumns = @JoinColumn(name = "library_id"))
//    private List<Library> libraries;

    @ManyToOne
    @JoinColumn(name="library_id")
    private UserLibrary userLibrary;

    @ManyToOne
    @JoinColumn(name="default_library_id")
    private DefaultLibrary defaultLibrary;
    public Card() {
    }

    public Card(String value, String transcription, String translation, String example, UserLibrary userLibrary, DefaultLibrary defaultLibrary, Training training) {
        this.value = value;
        this.transcription = transcription;
        this.translation = translation;
        this.example = example;
        this.defaultLibrary = defaultLibrary;
        this.userLibrary = userLibrary;
        this.training = training;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public UserLibrary getUserLibrary() {
        return userLibrary;
    }

    public void setUserLibrary(UserLibrary userLibrary) {
        this.userLibrary = userLibrary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public UserLibrary getLibrary() {
        return userLibrary;
    }

    public void setLibrary(UserLibrary userLibrary) {
        this.userLibrary = userLibrary;
    }

    public DefaultLibrary getDefaultLibrary() {
        return defaultLibrary;
    }

    public void setDefaultLibrary(DefaultLibrary defaultLibrary) {
        this.defaultLibrary = defaultLibrary;
    }
}
