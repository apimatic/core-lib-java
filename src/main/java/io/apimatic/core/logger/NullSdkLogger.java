package io.apimatic.core.logger;

import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;

public class NullSdkLogger implements ApiLogger {

    @Override
    public void logRequest(Request request) {
        // Nothing to log
    }

    @Override
    public void logResponse(Response response) {
        // Nothing to log
    }

}
