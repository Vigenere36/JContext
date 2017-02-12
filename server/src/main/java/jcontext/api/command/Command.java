package jcontext.api.command;

import java.io.Serializable;

public interface Command extends Serializable {
    CommandType commandType();
}
