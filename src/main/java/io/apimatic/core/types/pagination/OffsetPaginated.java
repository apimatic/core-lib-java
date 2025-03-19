package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

public class OffsetPaginated<T> extends PaginatedData<T> {
    private Deserializer<List<T>> deserializer;

    private Configuration configuration;

    private boolean isRequestBuilderUpdated;

    private OffsetPaginated(final Deserializer<List<T>> deserializer, final Configuration configuration,
            final EndpointConfiguration endPointConfig, final Response response) throws IOException {
        super(deserializer.apply(response.getBody()), response, endPointConfig);
        this.deserializer = deserializer;
        this.configuration = configuration;
        isRequestBuilderUpdated = updateRequestBuilderForNextCall(endPointConfig, getDataSize());
    }

    public static <T> OffsetPaginated<T> Create(Deserializer<List<T>> deserializer, Configuration configuration,
            EndpointConfiguration endPointConfig, Response response) throws IOException {
        return new OffsetPaginated<T>(deserializer, configuration, endPointConfig, response);
    }

    @Override
    protected PaginatedData<T> fetchData() {
        if (!isRequestBuilderUpdated) {
            return null;
        }

        EndpointConfiguration endpointConfig = getLastEndpointConfiguration();
        try {
            return new ApiCall.Builder<PaginatedData<T>, CoreApiException>()
                    .endpointConfiguration(
                            endpointConfig)
                    .globalConfig(endpointConfig.getGlobalConfiguration())
                    .requestBuilder(
                            endpointConfig.getRequestBuilder())
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

    @Override
    protected void updateData(PaginatedData<T> newData) {
        super.updateData(newData);
        isRequestBuilderUpdated = ((OffsetPaginated<T>) newData).isRequestBuilderUpdated;
    }

    private boolean updateRequestBuilderForNextCall(EndpointConfiguration lastEndpointConfig, int lastDataSize) {
        if (configuration.pageParamName == null && configuration.offsetParamName == null) {
            return false;
        }
        
        boolean isUpdated = false;

        try {
            HttpRequest.Builder lastRequest = lastEndpointConfig.getRequestBuilder();
            Map<String, Object> reqQuery = lastRequest.build(lastEndpointConfig.getGlobalConfiguration())
                    .getQueryParameters();

            if (configuration.pageParamName != null && reqQuery.containsKey(configuration.pageParamName)) {
                Integer newPageValue = Integer.parseInt((String) reqQuery.get(configuration.pageParamName)) + 1;
                lastRequest.queryParam(q -> q.key(configuration.pageParamName).value(newPageValue));
                isUpdated = true;
            }

            if (configuration.offsetParamName != null && reqQuery.containsKey(configuration.offsetParamName)) {
                Integer nextOffsetValue = Integer.parseInt("" + reqQuery.get(configuration.offsetParamName))
                        + lastDataSize;
                lastRequest.queryParam(q -> q.key(configuration.offsetParamName).value(nextOffsetValue));
                isUpdated = true;
            }
        } catch (Exception e) {
        }
        
        return isUpdated;
    }

    public static class Configuration {
        private String pageParamName;
        private String offsetParamName;

        public Configuration pageParamName(String pageParamName) {
            this.pageParamName = pageParamName;
            return this;
        }

        public Configuration offsetParamName(String offsetParamName) {
            this.offsetParamName = offsetParamName;
            return this;
        }
    }
}
