package org.example.server.pojo;

import java.util.Date;

public class EntryDateResponse {
    private Long id;
    private Date date;

    public EntryDateResponse(Long id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
