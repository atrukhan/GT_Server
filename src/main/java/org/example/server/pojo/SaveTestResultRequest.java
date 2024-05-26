package org.example.server.pojo;

import java.util.List;

public class SaveTestResultRequest {
    private List<Long> ids;
    private Integer mistakesCount;

    private Integer cardsCount;

    public Integer getCardsCount() {
        return cardsCount;
    }

    public Integer getMistakesCount() {
        return mistakesCount;
    }
    public List<Long> getIds() {
        return ids;
    }

}
