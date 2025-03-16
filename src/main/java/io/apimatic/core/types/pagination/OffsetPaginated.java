package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import io.apimatic.core.ApiCall;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.ResponseHandler.Builder;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;

public class OffsetPaginated<T> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private Configuration configuration;

    private GlobalConfiguration globalConfig;

    private Response response;

    private Builder<OffsetPaginated<T>, CoreApiException> responseBuilder;

    private HttpRequest.Builder requestBuilder;

    public OffsetPaginated(final T value, final Configuration configuration, final EndpointConfiguration endPointConfig,
            final Response response, final Builder<OffsetPaginated<T>, CoreApiException> responseBuilder) {
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

    public OffsetPaginated<T> next() {
        try {
            return new ApiCall.Builder<OffsetPaginated<T>, CoreApiException>().globalConfig(globalConfig)
                    .requestBuilder(configuration.getNextPageRequest(requestBuilder, globalConfig, response.getBody()))
                    .responseHandler(responseBuilder).build().execute();
        } catch (IOException | CoreApiException e) {
            // Ignore exceptions
        }

        return null;
    }

    public static class Configuration {
        private String pageParamName;
        private String offsetParamName;
        private String resultPointer;

        public Configuration(String pageParamName, String offsetParamName, String resultPointer) {
            this.pageParamName = pageParamName;
            this.offsetParamName = offsetParamName;
            this.resultPointer = resultPointer;
        }

        private HttpRequest.Builder getNextPageRequest(HttpRequest.Builder builder, GlobalConfiguration config,
                String response) throws IOException {
            if (pageParamName == null && offsetParamName == null) {
                return builder;
            }

            Request req = builder.build(config);

            if (pageParamName != null) {
                Integer newPageValue = Integer.parseInt((String) req.getQueryParameters().get(pageParamName)) + 1;
                return builder.queryParam(q -> q.key(pageParamName).value(newPageValue));
            }

            if (offsetParamName != null) {
                String resultArray = CoreHelper.getValueFromJson(resultPointer, response);
                List<?> result = CoreHelper.deserialize(resultArray, new TypeReference<List<?>>() {
                });
                Integer nextOffsetValue = Integer.parseInt("" + req.getQueryParameters().get(offsetParamName))
                        + (result == null ? 0 : result.size());
                return builder.queryParam(q -> q.key(offsetParamName).value(nextOffsetValue));
            }

            return builder;
        }
    }
}
