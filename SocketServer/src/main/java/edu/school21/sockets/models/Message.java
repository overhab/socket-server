package edu.school21.sockets.models;

import java.sql.Timestamp;

public class Message {
    private Long id;
    private User sender;
    private String m_text;
    private Timestamp date;


    public Message(Long id, User sender, String message, Timestamp date) {
        this.id = id;
        this.sender = sender;
        this.m_text = message;
        this.date = date;
    }

    public Message(User sender, String message, Timestamp date) {
        this.sender = sender;
        this.m_text = message;
        this.date = date;
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return m_text;
    }

    public void setText(String text) {
        this.m_text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + m_text + '\'' +
                ", date=" + date +
                ", sender=" + sender +
                '}';
    }
}
