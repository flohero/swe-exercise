package swe4.dto;

import java.io.Serializable;

public class UserScore implements Serializable {
    private final String username;
    private final float highscore;

    public UserScore(String username, float highscore) {
        this.username = username;
        this.highscore = highscore;
    }

    public String getUsername() {
        return username;
    }

    public float getHighscore() {
        return highscore;
    }
}
