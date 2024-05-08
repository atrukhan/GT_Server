package org.example.server.pojo;

import org.example.server.models.Card;
import org.example.server.models.UserLibrary;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class TrainingInfoResponse {
    private Long id;
    private Integer period;
    private Integer level;
    private Boolean available;
    private Date nextDate;
    private Date prevDate;

    public TrainingInfoResponse(Long id, Integer period, Integer level, Boolean available, Date prevDate, Date nextDate) {
        this.id = id;
        this.period = period;
        this.level = level;
        this.available = available;
        this.nextDate = nextDate;
        this.prevDate = prevDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public Date getPrevDate() {
        return prevDate;
    }

    public void setPrevDate(Date prevDate) {
        this.prevDate = prevDate;
    }
}
