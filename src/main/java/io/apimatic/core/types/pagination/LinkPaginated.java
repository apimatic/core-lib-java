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

public class LinkPaginated<T> extends PaginatedData<T> {
    private Deserializer<List<T>> deserializer;

    private Configuration configuration;

    private LinkPaginated(final Deserializer<List<T>> deserializer, final Configuration configuration,
            final EndpointConfiguration endPointConfig, final Response response) throws IOException {
        super(deserializer.apply(response.getBody()), response, endPointConfig);
        this.deserializer = deserializer;
        this.configuration = configuration;
    }

    public static <T> LinkPaginated<T> Create(Deserializer<List<T>> deserializer, Configuration configuration,
            EndpointConfiguration endPointConfig, Response response) throws IOException {
        return new LinkPaginated<T>(deserializer, configuration, endPointConfig, response);
    }

    @Override
    protected PaginatedData<T> fetchData() {
        String linkValue = CoreHelper.getValueFromJson(configuration.nextPointer, getLastResponse().getBody());

        if (linkValue == null) {
            return null;
        }

        EndpointConfiguration endpointConfig = getLastEndpointConfiguration();

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

        public Configuration nextPointer(String nextPointer) {
            this.nextPointer = nextPointer;
            return this;
        }
    }
}
