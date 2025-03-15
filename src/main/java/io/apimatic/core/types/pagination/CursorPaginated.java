package io.apimatic.core.types.pagination;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonPointer;
import javax.json.JsonStructure;

import io.apimatic.core.ApiCall;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.ResponseHandler.Builder;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;

public class CursorPaginated<T, ExceptionType extends CoreApiException> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private Configuration configuration;

    private GlobalConfiguration globalConfig;

    private Response response;

    private Builder<CursorPaginated<T, ExceptionType>, ExceptionType> responseBuilder;

    private HttpRequest.Builder requestBuilder;

    public CursorPaginated(final T value, final Configuration configuration, final EndpointConfiguration endPointConfig,
            final Response response, final Builder<CursorPaginated<T, ExceptionType>, ExceptionType> responseBuilder) {
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

    public CursorPaginated<T, ExceptionType> next() throws ExceptionType, IOException {

        JsonPointer nextCursorPointerRes = Json.createPointer(configuration.nextPointerResponse);
        JsonStructure resStructure = CoreHelper.createJsonStructure(response.getBody());

        if (resStructure != null && nextCursorPointerRes.containsValue(resStructure)) {
            JsonValue value = nextCursorPointerRes.getValue(resStructure);

            return new ApiCall.Builder<CursorPaginated<T, ExceptionType>, ExceptionType>().globalConfig(globalConfig)
                    .requestBuilder(req -> req = requestBuilder)
                    .responseHandler(res -> res = responseBuilder).build().execute();
        }

        return null;
    }

    public static class Configuration {
        private String nextPointerResponse;
        private String nextPointerRequest;

        public Configuration(String nextPointerResponse, String nextPointerRequest) {
            this.nextPointerResponse = nextPointerResponse;
            this.nextPointerRequest = nextPointerRequest;
        }
    }
}
