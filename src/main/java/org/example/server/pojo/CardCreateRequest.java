package org.example.server.pojo;

import javax.persistence.Column;

public class CardCreateRequest {
    private String value;
    private String transcription;

    private String translation;
    private String example;

    public String getValue() {
        return value;
    }

    public String getTranscription() {
        return transcription;
    }

    public String getTranslation() {
        return translation;
    }

    public String getExample() {
        return example;
    }
}
