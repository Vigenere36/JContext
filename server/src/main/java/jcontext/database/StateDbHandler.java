package jcontext.database;

import jcontext.state.State;

import java.sql.Connection;

interface StateDbHandler<T extends State> {
    void insert(T state, Connection connection);

    void update(T state, Connection connection);

    void delete(T state, Connection connection);
}
