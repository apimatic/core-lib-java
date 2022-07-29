package io.apimatic.core_lib;

import java.io.IOException;
import java.util.List;

import io.apimatic.core_interfaces.http.HttpContext;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.response.HttpResponse;
import io.apimatic.core_interfaces.type.functional.Deserializer;
import io.apimatic.core_lib.types.ApiException;

public class ResponseHandler<T> {

    private List<ErrorCase> errorCases;
    private Deserializer<T> deserializer;
    
    /**
     * 
     */
    public ResponseHandler() {
        
    }
    
    /**
     * @param errorCase
     * @param deserializerType
     */
    public ResponseHandler(Deserializer<T> deserializer) {
        this.deserializer = deserializer;
    }

    /**
     * @return the errorCase
     */
    public List<ErrorCase> getErrorCases() {
        return this.errorCases;
    }
    
    public Deserializer<T> getDeserializer() {
        return this.deserializer;
    }

    public T handle(HttpRequest request, HttpResponse response, CoreConfig coreConfig) throws IOException {
        HttpContext httpContext =
                coreConfig.getCompatibilityFactory().createHttpContext(request, response);
        // invoke the callback after response if its not null
        if (coreConfig.getHttpCallback() != null) {
            coreConfig.getHttpCallback().onAfterResponse(httpContext);
        }

        // Error handling using HTTP status codes
        int responseCode = 200;

        // return null on 404
        if (responseCode == 404) {
            return null;
        }
        // handle errors defined at the API level
        validateResponse(response, httpContext, coreConfig);

        // extract result from the http response
        return deserializer.apply(response.getBody());
    }

    public T handle(HttpContext context) {
      return null;
    
    }
    private void validateResponse(HttpResponse response, HttpContext httpContext, CoreConfig coreConfig) {
        // TODO Auto-generated method stub
    	int statusCode = response.getStatusCode();
		this.errorCases.stream().forEach(errorCase -> {
			if(errorCase.getStatusCode() == statusCode) {
				coreConfig.getCompatibilityFactory()
				.createApiException(errorCase.getException().getMessage(), httpContext);
			}
		});
    }
}
