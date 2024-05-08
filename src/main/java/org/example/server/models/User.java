package org.example.server.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 15, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActivated;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(targetEntity = UserInfo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
    @OneToMany(mappedBy = "user")
    private List<UserLibrary> libraries;

    @OneToMany(mappedBy = "user")
    private List<Test> tests;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private List<EntryDate> entryDates;
    public User() {
    }

    public User(String nickname, String email, String passwordHash, Boolean isActivated, Set<Role> roles) {
        this.nickname = nickname;
        this.email = email;
        this.password = passwordHash;
        this.isActivated = isActivated;
        this.roles = roles;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<EntryDate> getEntryDates() {
        return entryDates;
    }

    public void setEntryDates(List<EntryDate> entryDates) {
        this.entryDates = entryDates;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean enabled) {
        isActivated = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<UserLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<UserLibrary> libraries) {
        this.libraries = libraries;
    }
}
