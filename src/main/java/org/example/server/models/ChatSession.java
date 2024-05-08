package org.example.server.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat_session")
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 30)
    private String title;

    @Column(nullable = false)
    private Boolean isPrivateChat;

    @Column(nullable = false)
    private Boolean isPersonalChat;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "chat_session_users",
            joinColumns = @JoinColumn(name = "chat_session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    public ChatSession() {
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

    public Boolean getPrivateChat() {
        return isPrivateChat;
    }

    public void setPrivateChat(Boolean group) {
        isPrivateChat = group;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Boolean getPersonalChat() {
        return isPersonalChat;
    }

    public void setPersonalChat(Boolean personalChat) {
        isPersonalChat = personalChat;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
