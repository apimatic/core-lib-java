package io.apimatic.core_lib;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.apimatic.core_interfaces.compatibility.CompatibilityFactory;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.HttpMethod;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.type.FileWrapper;
import io.apimatic.core_interfaces.type.functional.Serializer;
import io.apimatic.core_lib.types.http.request.MultipartFileWrapper;
import io.apimatic.core_lib.types.http.request.MultipartWrapper;
import io.apimatic.core_lib.utilities.CoreHelper;

public class CoreRequest {
	private String server;
	private String Path;
	private HttpMethod httpMethod;
	private boolean requiresAuth;
	private Map<String, Object> queryParams;
	private Map<String, SimpleEntry<Object, Boolean>> templateParams;
	private HttpHeaders headerParams;
	private Map<String, Object> formParams;
	private Object body;
	private Serializer bodySerializer;

	/**
	 * @param server
	 * @param path
	 * @param httpMethod
	 * @param requiresAuth
	 * @param queryParams
	 * @param templateParams
	 * @param headerParams
	 * @param formParams
	 * @param body
	 * @param bodySerializer
	 */
	private CoreRequest(String server, String path, HttpMethod httpMethod, boolean requiresAuth,
			Map<String, Object> queryParams, Map<String, SimpleEntry<Object, Boolean>> templateParams,
			HttpHeaders headerParams, Map<String, Object> formParams, Object body, Serializer bodySerializer) {
		this.server = server;
		Path = path;
		this.httpMethod = httpMethod;
		this.requiresAuth = requiresAuth;
		this.queryParams = queryParams;
		this.templateParams = templateParams;
		this.headerParams = headerParams;
		this.formParams = formParams;
		this.body = body;
		this.bodySerializer = bodySerializer;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return Path;
	}

	/**
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @return the requiresAuth
	 */
	public boolean isRequiresAuth() {
		return requiresAuth;
	}

	/**
	 * @return the queryParams
	 */
	public Map<String, Object> getQueryParams() {
		return queryParams;
	}

	/**
	 * @return the templateParams
	 */
	public Map<String, SimpleEntry<Object, Boolean>> getTemplateParams() {
		return templateParams;
	}

	/**
	 * @return the headerParams
	 */
	public HttpHeaders getHeaderParams() {
		return headerParams;
	}

	/**
	 * @return the formParams
	 */
	public Map<String, Object> getFormParams() {
		return formParams;
	}

	/**
	 * @return the body
	 */
	public Object getBody() {
		return body;
	}

	/**
	 * @return the bodySerializer
	 */
	public Serializer getBodySerializer() {
		return bodySerializer;
	}

	public static class Builder {
		private String server;
		private String path;
		private HttpMethod httpMethod;
		private boolean requiresAuth;
		private Map<String, Object> queryParams = new HashMap<String, Object>();
		private Map<String, SimpleEntry<Object, Boolean>> templateParams = new HashMap<String, SimpleEntry<Object, Boolean>>();
		private HttpHeaders headerParams;
		private Map<String, Object> formParams = new HashMap<String, Object>();
		private Object body;
		private Serializer bodySerializer;
		private Map<String, Object> bodyParameters;

		public Builder(String server, String path, HttpHeaders headerParams) {
			this.headerParams = headerParams;
			this.server = server;
			this.path = path;
		}

		/**
		 * Setter for httpMethod
		 * 
		 * @param httpMethod HttpMethod value for httpMethod
		 * @return Builder
		 */
		public Builder httpMethod(HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		/**
		 * Setter for requiresAuth
		 * 
		 * @param requiresAuth boolean value for requiresAuth
		 * @return Builder
		 */
		public Builder requiresAuth(boolean requiresAuth) {
			this.requiresAuth = requiresAuth;
			return this;
		}

		/**
		 * Key value pair for queryParams
		 * 
		 * @param key   String value for key
		 * @param param Object value for param
		 * @return Builder
		 */
		public Builder queryParam(Parameter queryParameter) {
			queryParameter.validate();
			this.queryParams.put(queryParameter.getKey(), queryParameter.getValue());
			return this;
		}

		/**
		 * Key value pair for queryParams
		 * 
		 * @param key   String value for key
		 * @param param Object value for param
		 * @return Builder
		 */
		public Builder queryParam(Map<String, Object> queryParameters) {
			this.queryParams.putAll(queryParameters);
			return this;
		}

		/**
		 * Key value pair for templateParams
		 * 
		 * @param key   String value for key
		 * @param param SimpleEntry<Object, Boolean> value for param
		 * @return Builder
		 */
		public Builder templateParam(Parameter templateParameter) {
			templateParameter.validate();
			SimpleEntry<Object, Boolean> templateEntry = new SimpleEntry<Object, Boolean>(templateParameter.getValue(),
					templateParameter.isEncodeAllow());
			this.templateParams.put(templateParameter.getKey(), templateEntry);
			return this;
		}

		/**
		 * Setter for httpHeaders
		 * 
		 * @param headerParams HttpHeaders for headerParams
		 * @return Builder
		 */
		public Builder headerParam(Parameter httpHeaderParameter) {
			httpHeaderParameter.validate();
			if (this.headerParams != null) {
				this.headerParams.add(httpHeaderParameter.getKey(), httpHeaderParameter.getValue().toString());
			}

			return this;
		}

		/**
		 * Key value pair for formParams
		 * 
		 * @param key   String value for key
		 * @param param Object value for param
		 * @return Builder
		 */
		public Builder formParams(Parameter formParameter) {
			formParameter.validate();
			MultiPartRequest multiPartRequest = formParameter.getMultiPartRequest();
			if (multiPartRequest != null) {
				buildMultiRequest(formParameter, multiPartRequest, formParams);
			} else {
				this.formParams.put(formParameter.getKey(), formParameter.getValue());
			}
			return this;
		}

		private void buildMultiRequest(Parameter parameter, MultiPartRequest multiPartRequest,
				Map<String, Object> params) {
			String key  = parameter.getKey();
			switch (multiPartRequest.getMultiPartRequestType()) {
			case MULTI_PART_FILE:
				MultipartFileWrapper fileWrapper = new MultipartFileWrapper((FileWrapper) parameter.getValue(),
						multiPartRequest.getHeaders());
				params.put(key, fileWrapper);
				break;
			case MULTI_PART:
				MultipartWrapper multipartWrapper = new MultipartWrapper(parameter.getValue().toString(),
						multiPartRequest.getHeaders());
				params.put(key, multipartWrapper);
				break;
			default:
				break;
			}
		}

		/**
		 * Setter for body
		 * 
		 * @param body Object value for body
		 * @return Builder
		 */
		public Builder body(Parameter bodyParam) {
			bodyParam.validate();
			if (bodyParam.getKey() != null && !bodyParam.getKey().isEmpty()) {
				if (bodyParameters == null) {
					bodyParameters = new HashMap<String, Object>();
				}
				bodyParameters.put(bodyParam.getKey(), bodyParam.getValue());
			} else {
				this.body = bodyParam.getValue();
			}

			return this;
		}

		/**
		 * Setter for bodySerializer
		 * 
		 * @param bodySerializer Function value for bodySerializer
		 * @return Builder
		 */

		public Builder bodySerializer(Serializer bodySerializer) {
			this.bodySerializer = bodySerializer;
			return this;
		}

		public HttpRequest build(CoreConfig coreConfig) throws IOException {
			CompatibilityFactory compatibilityFactory = coreConfig.getCompatibilityFactory();
			final StringBuilder urlBuilder = getStringBuilder(coreConfig);

			processTemplateParams(urlBuilder);

			addGlobalHeader(coreConfig);

			HttpRequest request = buildRequest(compatibilityFactory, urlBuilder);

			// Invoke the callback before request if its not null
			if (coreConfig.getHttpCallback() != null) {
				coreConfig.getHttpCallback().onBeforeRequest(request);
			}

			return request;

		}

		private HttpRequest buildRequest(CompatibilityFactory compatibilityFactory, StringBuilder urlBuilder)
				throws IOException {
			HttpRequest request = null;
			if (body != null || bodyParameters != null) {

				request = compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams, queryParams,
						buildBodyString(urlBuilder));

			} else {
				List<SimpleEntry<String, Object>> formFields = generateFormFields();
				request = compatibilityFactory.createHttpRequest(httpMethod, urlBuilder, headerParams, queryParams,
						formFields);

			}

			return request;
		}

		/**
		 * @return
		 */
		private List<SimpleEntry<String, Object>> generateFormFields() {
			List<SimpleEntry<String, Object>> formFields = null;

			if (!formParams.isEmpty()) {
				formFields = CoreHelper.prepareFormFields(formParams);

			}
			return formFields;
		}

		private StringBuilder getStringBuilder(CoreConfig coreConfig) {
			return new StringBuilder(coreConfig.getBaseUri().apply(server) + path);
		}

		private void processTemplateParams(StringBuilder urlBuilder) {
			if (!templateParams.isEmpty()) {
				CoreHelper.appendUrlWithTemplateParameters(urlBuilder, templateParams);
			}
		}

		private void addGlobalHeader(CoreConfig coreConfig) {
			if (coreConfig.getGlobalHeaders() != null) {
				headerParams.addAll(coreConfig.getGlobalHeaders());
			}
		}

		private String buildBodyString(StringBuilder urlBuilder) throws IOException {
			String serializedBody = null;
			if (body != null) {
				if (bodySerializer != null) {
					serializedBody = bodySerializer.apply(body);
				} else {
					if (body instanceof String) {
						serializedBody = body.toString();
					} else {
						serializedBody = CoreHelper.serialize(body);
					}
				}
			}

			if (bodyParameters != null) {
				CoreHelper.removeNullValues(bodyParameters);
				serializedBody = CoreHelper.serialize(bodyParameters);
			}
			return serializedBody;

		}
	}

}
