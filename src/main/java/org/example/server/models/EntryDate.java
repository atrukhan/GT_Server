package org.example.server.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "entry_dates")
public class EntryDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public EntryDate() {
    }

    public EntryDate(Date date, User user) {
        this.date = date;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
