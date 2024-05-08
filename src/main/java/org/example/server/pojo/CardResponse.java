package org.example.server.pojo;

import javax.persistence.Column;

public class CardResponse {
    private Long id;
    private String value;
    private String transcription;
    private String translation;
    private String example;


    public CardResponse(Long id, String value, String transcription, String translation, String example) {
        this.id = id;
        this.value = value;
        this.transcription = transcription;
        this.translation = translation;
        this.example = example;
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
}
