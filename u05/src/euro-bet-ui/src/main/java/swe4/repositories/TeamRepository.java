package swe4.repositories;

import swe4.domain.Team;

import java.util.Collection;

public interface TeamRepository {

    Collection<Team> findAllTeams();

    void insertTeam(Team team);
}
