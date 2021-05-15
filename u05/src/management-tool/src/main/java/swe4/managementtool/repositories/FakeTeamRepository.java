package swe4.managementtool.repositories;

import swe4.managementtool.domain.Team;

import java.util.Collection;
import java.util.List;

public class FakeTeamRepository implements TeamRepository {
    @Override
    public Collection<Team> findAllTeams() {
        return List.of(
                new Team("Rapid"),
                new Team("Austria"),
                new Team("Salzburg"),
                new Team("Admira")
        );
    }
}
