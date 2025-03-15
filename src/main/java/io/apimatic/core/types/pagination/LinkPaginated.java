package io.apimatic.core.types.pagination;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonPointer;
import javax.json.JsonStructure;

import io.apimatic.core.ApiCall;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.ResponseHandler.Builder;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.response.Response;

public class LinkPaginated<T, ExceptionType extends CoreApiException> {

    /**
     * Private store for encapsulated object's value.
     */
    private T value;

    private Configuration configuration;

    private GlobalConfiguration globalConfig;

    private Builder<LinkPaginated<T, ExceptionType>, ExceptionType> responseBuilder;

    private JsonStructure jsonStructure;

    public LinkPaginated(final T value, final Configuration configuration,
            final GlobalConfiguration globalConfig, final Response response,
            final Builder<LinkPaginated<T, ExceptionType>, ExceptionType> responseBuilder) {
        this.value = value;
        this.configuration = configuration;
        this.globalConfig = globalConfig;
        this.responseBuilder = responseBuilder;
        this.jsonStructure = CoreHelper.createJsonStructure(response.getBody());
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

    public LinkPaginated<T, ExceptionType> first() throws ExceptionType, IOException {
        return executeForPointer(configuration.firstPointer);
    }

    public LinkPaginated<T, ExceptionType> last() throws ExceptionType, IOException {
        return executeForPointer(configuration.lastPointer);
    }

    public LinkPaginated<T, ExceptionType> previous() throws ExceptionType, IOException {
        return executeForPointer(configuration.prevPointer);
    }

    public LinkPaginated<T, ExceptionType> next() throws ExceptionType, IOException {
        return executeForPointer(configuration.nextPointer);
    }
    
    private LinkPaginated<T, ExceptionType> executeForPointer(String pointer) throws ExceptionType, IOException {
        
        JsonPointer jsonPointer = Json.createPointer(pointer);
        
        if (jsonStructure != null && jsonPointer.containsValue(jsonStructure)) {
            String pathValue = jsonPointer.getValue(jsonStructure).toString();
            
            return new ApiCall.Builder<LinkPaginated<T, ExceptionType>, ExceptionType>().globalConfig(globalConfig)
                    .requestBuilder(requestBuilder -> requestBuilder.path(pathValue).httpMethod(Method.GET))
                    .responseHandler(res -> res = responseBuilder).build().execute();
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
