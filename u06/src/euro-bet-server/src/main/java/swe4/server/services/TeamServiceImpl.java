package swe4.server.services;

import swe4.domain.entities.Team;
import swe4.server.repositories.RepositoryFactory;
import swe4.server.repositories.TeamRepository;

import java.util.Collection;

public class TeamServiceImpl implements TeamService{

    TeamRepository teamRepository = RepositoryFactory.teamRepositoryInstance();

    @Override
    public Collection<Team> findAllTeams() {
        return teamRepository.findAllTeams();
    }
}
