package net.mauhiz.util;

public abstract class NamedRunnable extends AbstractRunnable {

    private String name;

    public NamedRunnable(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null ? super.getName() : name;
    }

}