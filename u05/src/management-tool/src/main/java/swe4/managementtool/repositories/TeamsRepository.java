package swe4.managementtool.repositories;

import swe4.managementtool.domain.Team;

import java.util.Collection;

public interface TeamsRepository {

    Collection<Team> findAllTeams();
}
