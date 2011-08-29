package net.mauhiz.irc.bouncer;

import java.util.ArrayList;
import java.util.List;

public class AccountStore {

    protected static final List<Account> ACCOUNTS = new ArrayList<>();

    protected void clear() {
        synchronized (ACCOUNTS) {
            ACCOUNTS.clear();
        }

    }

    public Iterable<Account> getAccounts() {
        synchronized (ACCOUNTS) {
            return new ArrayList<>(ACCOUNTS); // return a copy
        }
    }

    public Account getFor(String login, String password) {
        synchronized (ACCOUNTS) {
            for (Account acc : ACCOUNTS) {
                if (acc.getUsername().equals(login) && acc.getPassword().equals(password)) {
                    return acc;
                }
            }

            return null;
        }
    }

    protected void load() {
        // TODO implement storage strategy
    }

    public void reload() {
        synchronized (ACCOUNTS) {
            clear();
            load();
        }
    }
}
