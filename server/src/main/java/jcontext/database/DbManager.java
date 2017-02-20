package jcontext.database;

import com.google.inject.Inject;
import jcontext.state.StateObject;
import lombok.Data;

import java.util.Map;

@Data
public class DbManager {
    @Inject Map<Class<? extends StateObject>, StateDbHandler> dbHandlersForState;

    public void insert(StateObject state) {
        dbHandlersForState.get(state.getClass()).insert(state);
    }

    public void update(StateObject state) {
        dbHandlersForState.get(state.getClass()).update(state);
    }

    public void delete(StateObject state) {
        dbHandlersForState.get(state.getClass()).delete(state);
    }
}
