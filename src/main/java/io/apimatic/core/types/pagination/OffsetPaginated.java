package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

public class OffsetPaginated<T> extends PaginatedData<T> {
    private Deserializer<T> deserializer;

    private Configuration configuration;

    private OffsetPaginated(final Deserializer<T> deserializer, final Configuration configuration,
            final EndpointConfiguration endPointConfig, final Response response) throws IOException {
        super(deserializer.apply(response.getBody()), response, endPointConfig, configuration.resultPointer);
        this.deserializer = deserializer;
        this.configuration = configuration;
    }

    public static <T> OffsetPaginated<T> Create(Deserializer<T> deserializer, Configuration configuration,
            EndpointConfiguration endPointConfig, Response response) throws IOException {
        return new OffsetPaginated<T>(deserializer, configuration, endPointConfig, response);
    }

    @Override
    protected PaginatedData<T> fetchData() {
        EndpointConfiguration endpointConfig = getLastEndpointConfiguration();
        try {
            return new ApiCall.Builder<PaginatedData<T>, CoreApiException>().endpointConfiguration(endpointConfig)
                    .globalConfig(endpointConfig.getGlobalConfiguration())
                    .requestBuilder(
                            configuration
                                    .getNextPageRequest(
                                            endpointConfig.getRequestBuilder(), endpointConfig.getGlobalConfiguration(),
                                            getLastResponse().getBody()))
                    .responseHandler(res -> res
                            .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                    ErrorCase.setReason(null,
                                            (reason, context) -> new CoreApiException(reason, context))))
                            .offsetPaginatedDeserializer(deserializer, configuration))
                    .build().execute();
        } catch (Exception e) {
            // Ignore exceptions
        }

        return null;
    }

    public static class Configuration {
        private String pageParamName;
        private String offsetParamName;
        private String resultPointer;

        public Configuration pageParamName(String pageParamName) {
            this.pageParamName = pageParamName;
            return this;
        }

        public Configuration offsetParamName(String offsetParamName) {
            this.offsetParamName = offsetParamName;
            return this;
        }

        public Configuration resultPointer(String resultPointer) {
            this.resultPointer = resultPointer;
            return this;
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
