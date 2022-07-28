package io.apimatic.core_lib;

import java.io.IOException;
import java.util.List;

import io.apimatic.core_interfaces.http.HttpContext;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.response.HttpResponse;
import io.apimatic.core_lib.utilities.ApiHelper;

public class ResponseHandler<T> {

	private List<ErrorCase> errorCase;
	private Class<T> deserializationClass;
	
	public T handle(HttpRequest request, HttpResponse response, CoreConfig coreConfig) throws IOException {
		HttpContext httpContext = coreConfig.getCompatibilityFactory().createHttpContext(request, response);
        //invoke the callback after response if its not null
        if (coreConfig.getHttpCallback()!= null) {
        	coreConfig.getHttpCallback().onAfterResponse(httpContext);
        }

        //Error handling using HTTP status codes
        int responseCode = response.getStatusCode();

        //return null on 404
        if (responseCode == 404) {
            return null;
        }
        //handle errors defined at the API level
        validateResponse(response, httpContext);

        //extract result from the http response
        return ApiHelper.deserialize(response.getBody(), deserializationClass);
	}

	private void validateResponse(HttpResponse response, HttpContext httpContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the errorCase
	 */
	public List<ErrorCase> getErrorCase() {
		return errorCase;
	}

	/**
	 * @return the deserializationClass
	 */
	public Class<T> getDeserializationClass() {
		return deserializationClass;
	}
	
	
	
}
