package io.tglanz.chesslysis.backend;

import io.tglanz.chesslysis.backend.model.Account;
import io.tglanz.chesslysis.backend.model.ChessApp;
import org.hibernate.SessionFactory;

// Heavy duty. For simplicity for now.
public class Repository {

    private final SessionFactory sessionFactory;

    public Repository(SessionFactory sessionFactory) {
       this.sessionFactory = sessionFactory;
    }

    public Account getOrCreateAccount(ChessApp app, String playerId, String username) {
        String accountId = Account.conventionalId(app, playerId);

        return sessionFactory.fromTransaction(session -> {
            var account = session.get(Account.class, accountId);
            if (account == null) {
                account = new Account(app, playerId, username);
                session.persist(account);
            }

            return account;
        });
    }
}
