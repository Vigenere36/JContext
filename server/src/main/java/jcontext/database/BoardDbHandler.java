package jcontext.database;

import jcontext.state.Board;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

@Slf4j
class BoardDbHandler implements StateDbHandler<Board> {
    @Override
    public void insert(Board state, Connection connection) {
        log.info("Inserting board: {}", state);
    }

    @Override
    public void update(Board state, Connection connection) {

    }

    @Override
    public void delete(Board state, Connection connection) {

    }
}
