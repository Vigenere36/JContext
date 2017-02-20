package jcontext.database;

import com.google.inject.Inject;
import jcontext.state.StateObject;
import lombok.Data;

import java.util.Map;

@Data
public class DbManager {
    @Inject
    Map<Class<? extends StateObject>, StateDbHandler> dbHandlersForState;
}
