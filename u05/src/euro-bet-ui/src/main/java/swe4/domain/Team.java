package swe4.domain;

import java.util.Objects;
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team && Objects.equals(((Team) obj).name, this.name);
    }
}
