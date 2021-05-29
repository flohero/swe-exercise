package swe4.server.repositories;

import swe4.domain.entities.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeTeamRepository implements TeamRepository {
    private final List<Team> teams = new ArrayList<>();

    FakeTeamRepository() {}

    @Override
    public Collection<Team> findAllTeams() {
        return teams;
    }

    @Override
    public void insertTeam(Team team) {
        teams.add(team);
    }
}
