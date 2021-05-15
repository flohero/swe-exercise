package swe4.managementtool.domain;

public class User {
    private final String username;
    private final byte[] hash;

    public User(String username, byte[] password) {
        this.username = username;
        this.hash = password;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getHash() {
        return this.hash;
    }

}
