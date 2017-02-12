package jcontext.api.response;

import java.io.Serializable;

public interface Response extends Serializable {
    ResponseType responseType();
}
