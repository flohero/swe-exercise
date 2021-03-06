package swe4.domain;

import java.io.Serializable;
import java.util.Objects;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
