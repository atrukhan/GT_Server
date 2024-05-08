package org.example.server.models;

import org.example.server.models.enums.ELanguage;

import javax.persistence.*;

@Entity
@Table(name = "translations")
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 50)
    private String value;
    @Column(nullable = false, length = 255)
    private String translation;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ELanguage sourceLanguage;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ELanguage destinationLanguage;


    public Translation(String value, String translation, ELanguage sourceLanguage, ELanguage destinationLanguage) {
        this.value = value;
        this.translation = translation;
        this.sourceLanguage = sourceLanguage;
        this.destinationLanguage = destinationLanguage;
    }

    public ELanguage getDestinationLanguage() {
        return destinationLanguage;
    }

    public void setDestinationLanguage(ELanguage destinationLanguage) {
        this.destinationLanguage = destinationLanguage;
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

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public ELanguage getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(ELanguage sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }
}
