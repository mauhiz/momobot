package net.mauhiz.util;

import java.util.Objects;

public abstract class AbstractNamedRunnable extends AbstractRunnable {

    private final String name;

    public AbstractNamedRunnable(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return Objects.toString(name, super.getName());
    }

}