package jcontext.api.response;

import lombok.Data;

@Data
public class AckResponse implements Response {
    @Override
    public ResponseType responseType() {
        return ResponseType.ACK;
    }
}
