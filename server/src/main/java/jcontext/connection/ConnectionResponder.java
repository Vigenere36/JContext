package jcontext.connection;

import jcontext.api.response.Response;

interface ConnectionResponder {
    void sendResponse(Response response);
}
