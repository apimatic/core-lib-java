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

public class LinkPaginated<T> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private Configuration configuration;

    private GlobalConfiguration globalConfig;
    
    private Response response;

    private Builder<LinkPaginated<T>, CoreApiException> responseBuilder;

    private HttpRequest.Builder requestBuilder;

    public LinkPaginated(final T value, final Configuration configuration, final EndpointConfiguration endPointConfig,
            final Response response, final Builder<LinkPaginated<T>, CoreApiException> builder) {
        this.value = value;
        this.configuration = configuration;
        this.globalConfig = endPointConfig.getGlobalConfiguration();
        this.response = response;
        this.responseBuilder = builder;
        this.requestBuilder = endPointConfig.getRequestBuilder();
    }

    /**
     * Converts this LinkPaginated into string format.
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

    public LinkPaginated<T> first() {
        return executeForPointer(configuration.firstPointer);
    }

    public LinkPaginated<T> last() {
        return executeForPointer(configuration.lastPointer);
    }

    public LinkPaginated<T> previous() {
        return executeForPointer(configuration.prevPointer);
    }

    public LinkPaginated<T> next() {
        return executeForPointer(configuration.nextPointer);
    }

    private LinkPaginated<T> executeForPointer(String pointer) {
        String linkValue = CoreHelper.getValueFromJson(pointer, response.getBody());
        
        if (linkValue == null) {
            return null;
        }

        try {
            return new ApiCall.Builder<LinkPaginated<T>, CoreApiException>().globalConfig(globalConfig)
                    .requestBuilder(requestBuilder.queryParam(CoreHelper.getQueryParameters(linkValue)))
                    .responseHandler(responseBuilder).build().execute();
        } catch (IOException | CoreApiException e) {
            // Ignore exceptions
        }

        return null;
    }

    public static class Configuration {
        private String firstPointer;
        private String lastPointer;
        private String prevPointer;
        private String nextPointer;

        public Configuration(String firstPointer, String lastPointer, String prevPointer, String nextPointer) {
            this.firstPointer = firstPointer;
            this.lastPointer = lastPointer;
            this.prevPointer = prevPointer;
            this.nextPointer = nextPointer;
        }
    }
}
