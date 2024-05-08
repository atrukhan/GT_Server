package org.example.server.pojo;

public class LibResponse {

    private Long id;
    private String title;
    private Integer cardsCount;
    private String code;


    public LibResponse(Long id, String title, String code, Integer cardsCount) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.cardsCount = cardsCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public Integer getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(Integer cardsCount) {
        this.cardsCount = cardsCount;
    }

}
