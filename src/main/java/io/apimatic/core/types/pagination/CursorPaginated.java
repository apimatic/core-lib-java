package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.ApiCall;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.ResponseHandler.Builder;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public class CursorPaginated<T> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private Configuration configuration;

    private GlobalConfiguration globalConfig;

    private Response response;

    private Builder<CursorPaginated<T>, CoreApiException> responseBuilder;

    private HttpRequest.Builder requestBuilder;

    public CursorPaginated(final T value, final Configuration configuration, final EndpointConfiguration endPointConfig,
            final Response response, final Builder<CursorPaginated<T>, CoreApiException> responseBuilder) {
        this.value = value;
        this.configuration = configuration;
        this.globalConfig = endPointConfig.getGlobalConfiguration();
        this.response = response;
        this.responseBuilder = responseBuilder;
        this.requestBuilder = endPointConfig.getRequestBuilder();
    }

    /**
     * Converts this CursorPaginated into string format.
     * 
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "" + value;
    }

    public T value() {
        return value;
    }

    public CursorPaginated<T> next() {
        String cursorValue = CoreHelper.getValueFromJson(configuration.nextPointerResponse, response.getBody());
        
        if (cursorValue == null) {
            return null;
        }

        try {
            return new ApiCall.Builder<CursorPaginated<T>, CoreApiException>().globalConfig(globalConfig)
                    .requestBuilder(requestBuilder.queryParam(q -> q.key(configuration.cursorQueryParamName).value(cursorValue)))
                    .responseHandler(responseBuilder).build().execute();
        } catch (IOException | CoreApiException e) {
            // Ignore exceptions
        }

        return null;
    }

    public static class Configuration {
        private String nextPointerResponse;
        private String cursorQueryParamName;

        public Configuration(String nextPointerResponse, String cursorQueryParamName) {
            this.nextPointerResponse = nextPointerResponse;
            this.cursorQueryParamName = cursorQueryParamName;
        }
    }
}
