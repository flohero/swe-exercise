package swe4.managementtool.domain;

import java.util.UUID;

public class Team {
    private final UUID id;
    private final String name;

    public Team(final String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
