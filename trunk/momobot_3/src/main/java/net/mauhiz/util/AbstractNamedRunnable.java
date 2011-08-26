package net.mauhiz.util;

public abstract class AbstractNamedRunnable extends AbstractRunnable {

    private final String name;

    public AbstractNamedRunnable(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null ? super.getName() : name;
    }

}