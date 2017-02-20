package jcontext.database;

import jcontext.state.StateObject;

public interface StateDbHandler<T extends StateObject> {
    void insert(T state);

    void update(T state);

    void delete(T state);
}
