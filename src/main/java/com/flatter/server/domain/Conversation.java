package com.flatter.server.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User sender;

    @OneToOne
    @JoinColumn(unique = true)
    private User reciver;

    @OneToMany(mappedBy = "conversation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public Conversation sender(User user) {
        this.sender = user;
        return this;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    public User getReciver() {
        return reciver;
    }

    public Conversation reciver(User user) {
        this.reciver = user;
        return this;
    }

    public void setReciver(User user) {
        this.reciver = user;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Conversation messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public Conversation addMessage(Message message) {
        this.messages.add(message);
        message.setConversation(this);
        return this;
    }

    public Conversation removeMessage(Message message) {
        this.messages.remove(message);
        message.setConversation(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversation)) {
            return false;
        }
        return id != null && id.equals(((Conversation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + getId() +
            "}";
    }
}
