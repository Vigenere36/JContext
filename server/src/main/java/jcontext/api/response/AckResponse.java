package jcontext.api.response;

public class AckResponse implements Response {
    @Override
    public ResponseType responseType() {
        return ResponseType.ACK;
    }
}
