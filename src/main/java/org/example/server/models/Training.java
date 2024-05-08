package org.example.server.models;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Integer period;
    @Column(nullable = false)
    private Integer level;
    @Column(nullable = false)
    private Boolean available;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextDate;
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date prevDate;
    @ManyToOne
    @JoinColumn(name="library_id")
    private UserLibrary library;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "training")
    private List<Card> cards;

    public Training() {
    }

    public Training(Integer period, Integer level, Boolean available, Date prevDate, Date nextDate, UserLibrary library, List<Card> cards) {
        this.period = period;
        this.level = level;
        this.available = available;
        this.nextDate = nextDate;
        this.prevDate = prevDate;
        this.library = library;
        this.cards = cards;
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

    public UserLibrary getLibrary() {
        return library;
    }

    public void setLibrary(UserLibrary library) {
        this.library = library;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
