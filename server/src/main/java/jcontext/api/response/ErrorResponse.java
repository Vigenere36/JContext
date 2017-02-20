package jcontext.api.response;

import lombok.Data;

@Data
public class ErrorResponse implements Response{
    @Override
    public ResponseType responseType() {
        return ResponseType.ERROR;
    }
}
