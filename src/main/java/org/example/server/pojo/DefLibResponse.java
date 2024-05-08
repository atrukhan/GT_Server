package org.example.server.pojo;

public class DefLibResponse {

    private Long id;
    private String title;
    private Double learnedPercentage;

    private boolean isDef;

    private Integer cardsCount;
    public DefLibResponse(Long id, String title, Double learnedPercentage, Integer cardsCount) {
        this.id = id;
        this.title = title;
        this.learnedPercentage = learnedPercentage;
        this.cardsCount = cardsCount;
        this.isDef = true;
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

    public Double getLearnedPercentage() {
        return learnedPercentage;
    }

    public void setLearnedPercentage(Double learnedPercentage) {
        this.learnedPercentage = learnedPercentage;
    }

    public Integer getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(Integer cardsCount) {
        this.cardsCount = cardsCount;
    }

    public boolean isDef() {
        return isDef;
    }

    public void setDef(boolean def) {
        isDef = def;
    }
}
