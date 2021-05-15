package swe4.managementtool.domain;

import java.util.UUID;

public class User {
    private final UUID id;
    private String firstname;
    private String lastname;
    private String username;
    private byte[] hash;

    public User(String firstname, String lastname, String username, byte[] password) {
        this.id = UUID.randomUUID();
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.hash = password;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User &&  ((User) obj).id.equals(this.id);
    }

    public UUID getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return new String(this.hash);
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
