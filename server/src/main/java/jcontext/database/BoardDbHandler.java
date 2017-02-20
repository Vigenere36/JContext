package jcontext.database;

import jcontext.state.Board;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class BoardDbHandler implements StateDbHandler<Board> {
    @Override
    public void insert(Board state) {
        log.info("Inserting board: {}", state);
    }

    @Override
    public void update(Board state) {

    }

    @Override
    public void delete(Board state) {

    }
}
