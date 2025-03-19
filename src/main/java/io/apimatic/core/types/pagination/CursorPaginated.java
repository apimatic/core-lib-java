package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

public class CursorPaginated<T> extends PaginatedData<T> {
    private Deserializer<List<T>> deserializer;

    private Configuration configuration;

    private CursorPaginated(final Deserializer<List<T>> deserializer, final Configuration configuration,
            final EndpointConfiguration endPointConfig, final Response response) throws IOException {
        super(deserializer.apply(response.getBody()), response, endPointConfig);
        this.deserializer = deserializer;
        this.configuration = configuration;
    }

    public static <T> CursorPaginated<T> Create(Deserializer<List<T>> deserializer, Configuration configuration,
            EndpointConfiguration endPointConfig, Response response) throws IOException {
        return new CursorPaginated<T>(deserializer, configuration, endPointConfig, response);
    }

    @Override
    protected PaginatedData<T> fetchData() {
        String cursorValue = CoreHelper.getValueFromJson(configuration.nextCursor,
                getLastResponse().getBody());
        EndpointConfiguration endpointConfig = getLastEndpointConfiguration();

        if (cursorValue == null) {
            return null;
        }

        try {
            return new ApiCall.Builder<PaginatedData<T>, CoreApiException>().endpointConfiguration(endpointConfig)
                    .globalConfig(endpointConfig.getGlobalConfiguration())
                    .requestBuilder(
                            endpointConfig
                                    .getRequestBuilder().queryParam(q -> q.key(configuration.cursorQueryParamName)
                                            .value(cursorValue)))
                    .responseHandler(res -> res
                            .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                    ErrorCase.setReason(null,
                                            (reason, context) -> new CoreApiException(reason, context))))
                            .cursorPaginatedDeserializer(deserializer, configuration))
                    .build().execute();
        } catch (Exception e) {
            // Ignore exceptions
        }

        return null;
    }

    public static class Configuration {
        private String nextCursor;
        private String cursorQueryParamName;

        public Configuration nextCursor(String nextCursor) {
            this.nextCursor = nextCursor;
            return this;
        }

        public Configuration cursorQueryParamName(String cursorQueryParamName) {
            this.cursorQueryParamName = cursorQueryParamName;
            return this;
        }
    }
}
