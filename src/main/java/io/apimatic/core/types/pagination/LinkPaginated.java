package io.apimatic.core.types.pagination;

import java.io.IOException;
import java.util.Collections;

import io.apimatic.core.ApiCall;
import io.apimatic.core.ErrorCase;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.Deserializer;

public class LinkPaginated<T> extends PaginatedData<T> {
    private Deserializer<T> deserializer;

    private Configuration configuration;

    private LinkPaginated(final Deserializer<T> deserializer, final Configuration configuration,
            final EndpointConfiguration endPointConfig, final Response response) throws IOException {
        super(deserializer.apply(response.getBody()), response, endPointConfig, configuration.resultPointer);
        this.deserializer = deserializer;
        this.configuration = configuration;
    }

    public static <T> LinkPaginated<T> Create(Deserializer<T> deserializer, Configuration configuration,
            EndpointConfiguration endPointConfig, Response response) throws IOException {
        return new LinkPaginated<T>(deserializer, configuration, endPointConfig, response);
    }

    @Override
    protected PaginatedData<T> fetchData() {
        String linkValue = CoreHelper.getValueFromJson(configuration.nextPointer, getLastResponse().getBody());
        EndpointConfiguration endpointConfig = getLastEndpointConfiguration();

        if (linkValue == null) {
            return null;
        }

        try {
            return new ApiCall.Builder<PaginatedData<T>, CoreApiException>()
                    .endpointConfiguration(endpointConfig)
                    .globalConfig(
                            endpointConfig.getGlobalConfiguration())
                    .requestBuilder(endpointConfig.getRequestBuilder()
                            .queryParam(CoreHelper.getQueryParameters(linkValue)))
                    .responseHandler(res -> res
                            .globalErrorCase(Collections.singletonMap(ErrorCase.DEFAULT,
                                    ErrorCase.setReason(null,
                                            (reason, context) -> new CoreApiException(reason, context))))
                            .linkPaginatedDeserializer(deserializer, configuration))
                    .build().execute();
        } catch (Exception e) {
            // Ignore exceptions
        }
        
        return null;
    }

    public static class Configuration {
        private String nextPointer;
        private String resultPointer;

        public Configuration nextPointer(String nextPointer) {
            this.nextPointer = nextPointer;
            return this;
        }

        public Configuration resultPointer(String resultPointer) {
            this.resultPointer = resultPointer;
            return this;
        }
    }
}
