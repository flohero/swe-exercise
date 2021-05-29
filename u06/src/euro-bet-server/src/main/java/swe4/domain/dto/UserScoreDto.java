package swe4.domain.dto;

import java.io.Serializable;

public class UserScoreDto implements Serializable {
    private final String username;
    private final float highscore;

    public UserScoreDto(String username, float highscore) {
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
