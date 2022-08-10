package io.apimatic.core_lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.apimatic.core_interfaces.http.HttpContext;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.http.response.HttpResponse;
import io.apimatic.core_interfaces.type.functional.Deserializer;
import io.apimatic.core_interfaces.type.functional.ObjectCreator;
import io.apimatic.core_lib.types.ApiException;

public class CoreResponseHandler<ResponseType, ExceptionType extends ApiException> {

	private Map<String, ErrorCase<ExceptionType>> localErrorCases  = null;
	private Map<String, ErrorCase<ExceptionType>> globalErrorCases = null;
	private Deserializer<ResponseType> deserializer;
	private ObjectCreator<ResponseType> objectCreator;
	private boolean isNullify404Enabled = false;

	/**
	 * @param errorCase
	 * @param deserializerType
	 */
	public CoreResponseHandler() {
	}

	/**
	 * Getter for the errorCases
	 * 
	 * @return the errorCase
	 */
	public Map<String, ErrorCase<ExceptionType>> getLocalErrorCases() {
		return this.localErrorCases;
	}

	/**
	 * Getter for the deserializer
	 * 
	 * @return the deserializer function
	 */
	public Deserializer<ResponseType> getDeserializer() {
		return this.deserializer;
	}

	/**
	 * @return the globalErrorCases
	 */
	public Map<String, ErrorCase<ExceptionType>> getGlobalErrorCases() {
		return globalErrorCases;
	}
	
	
	public CoreResponseHandler<ResponseType, ExceptionType> globalErrorCase(Map<String, ErrorCase<ExceptionType>> globalErrorCases){
		this.globalErrorCases = globalErrorCases;
		return this;
	}
	

	public CoreResponseHandler<ResponseType, ExceptionType> localErrorCase(String statusCode, ErrorCase<ExceptionType> errorCase){
		if (this.localErrorCases == null) {
			this.localErrorCases = new HashMap<String, ErrorCase<ExceptionType>>();
		}
		
		this.localErrorCases.put(statusCode, errorCase);
		return this;
	}

	public CoreResponseHandler<ResponseType, ExceptionType> deserializer(Deserializer<ResponseType> deserializer){
		this.deserializer = deserializer;
		return this;
	}
	
	public CoreResponseHandler<ResponseType, ExceptionType> objectCreator(ObjectCreator<ResponseType> objectCreator){
		this.objectCreator = objectCreator;
		return this;
	}

	public CoreResponseHandler<ResponseType, ExceptionType> nullify404(boolean isNullify404Enabled){
		this.isNullify404Enabled = isNullify404Enabled; 
		return this;
	}
	
	public ResponseType handle(HttpRequest httpRequest, HttpResponse httpResponse, CoreConfig coreConfig)
			throws IOException, ExceptionType {
		HttpContext httpContext = coreConfig.getCompatibilityFactory().createHttpContext(httpRequest, httpResponse);
		// invoke the callback after response if its not null
		if (coreConfig.getHttpCallback() != null) {
			coreConfig.getHttpCallback().onAfterResponse(httpContext);
		}

		if (isNullify404Enabled) {
			int responseCode = httpContext.getResponse().getStatusCode();
			// return null on 404
			if (responseCode == 404) {
				return null;
			}
		}
	
		// handle errors defined at the API level
		validateResponse(httpContext);

		if (deserializer != null) {
			// extract result from the http response
			return deserializer.apply(httpResponse.getBody());
		}
		
		return objectCreator.apply(httpResponse);
		
	}

	private void validateResponse(HttpContext httpContext) throws ExceptionType {
		// TODO Auto-generated method stub
		HttpResponse response = httpContext.getResponse();
		String statusCode = String.valueOf(response.getStatusCode());
		if (localErrorCases != null && localErrorCases.containsKey(statusCode)) {
			localErrorCases.get(statusCode).throwException(httpContext);
		}
		
		if (globalErrorCases != null && globalErrorCases.containsKey(statusCode)) {
			globalErrorCases.get(statusCode).throwException(httpContext);
		}
	}



}
