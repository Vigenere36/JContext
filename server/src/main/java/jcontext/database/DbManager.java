package jcontext.database;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jcontext.state.State;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class DbManager {
    private final Map<Class<? extends State>, StateDbHandler> dbHandlersForState;
    private final Connection connection;

    @Inject DbManager(Map<Class<? extends State>, StateDbHandler> dbHandlersForState,
                      @Named("dbConnection") String dbConnection,
                      DbMigration dbMigration) throws SQLException, IOException {
        this.dbHandlersForState = dbHandlersForState;
        this.connection = DriverManager.getConnection(dbConnection);

        log.info("Initiated db connection with {}", dbConnection);

        dbMigration.migrateIfNeeded(connection);
    }

    public void insert(State state) {
        dbHandlersForState.get(state.getClass()).insert(state, connection);
    }

    public void update(State state) {
        dbHandlersForState.get(state.getClass()).update(state, connection);
    }

    public void delete(State state) {
        dbHandlersForState.get(state.getClass()).delete(state, connection);
    }
}
