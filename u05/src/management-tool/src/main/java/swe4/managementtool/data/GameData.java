package swe4.managementtool.data;

import javafx.beans.property.*;
import swe4.managementtool.domain.Team;

public class GameData {
    private final ObjectProperty<Team> team1 = new SimpleObjectProperty<>();
    private final ObjectProperty<Team> team2 = new SimpleObjectProperty<>();
    private final IntegerProperty scoreTeam1 = new SimpleIntegerProperty();
    private final IntegerProperty scoreTeam2 = new SimpleIntegerProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final StringProperty time = new SimpleStringProperty();

    public GameData(Team team1, Team team2,
                    int scoreTeam1, int scoreTeam2,
                    String date, String time) {
        setTeam1(team1);
        setTeam2(team2);
        setScoreTeam1(scoreTeam1);
        setScoreTeam2(scoreTeam2);
        setDate(date);
        setTime(time);
    }

    public Team getTeam1() {
        return team1.get();
    }

    public ObjectProperty<Team> team1Property() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1.set(team1);
    }

    public Team getTeam2() {
        return team2.get();
    }

    public ObjectProperty<Team> team2Property() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2.set(team2);
    }

    public int getScoreTeam1() {
        return scoreTeam1.get();
    }

    public IntegerProperty scoreTeam1Property() {
        return scoreTeam1;
    }

    public void setScoreTeam1(int scoreTeam1) {
        this.scoreTeam1.set(scoreTeam1);
    }

    public int getScoreTeam2() {
        return scoreTeam2.get();
    }

    public IntegerProperty scoreTeam2Property() {
        return scoreTeam2;
    }

    public void setScoreTeam2(int scoreTeam2) {
        this.scoreTeam2.set(scoreTeam2);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}
