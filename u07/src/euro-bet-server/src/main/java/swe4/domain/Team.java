package swe4.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Team implements Serializable {
    private int id;
    private final String name;

    public Team(final String name) {
        this.name = name;
    }

    public Team(final int id, final String name) {
        this.id = id;
        this.name = name;
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
