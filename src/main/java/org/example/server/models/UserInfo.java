package org.example.server.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date registrationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date activationDate;

    @OneToOne(targetEntity = LanguageLevel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "language_level_id")
    private LanguageLevel languageLevel;
}
