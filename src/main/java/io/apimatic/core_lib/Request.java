package io.apimatic.core_lib;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import io.apimatic.core_interfaces.http.HttpHeaders;
import io.apimatic.core_interfaces.http.HttpMethod;
import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_interfaces.type.functional.Serializer;

public class Request {
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
	private Request(String server, String path, HttpMethod httpMethod, boolean requiresAuth,
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
		private Map<String, SimpleEntry<Object, Boolean>> templateParams = new HashMap<String, SimpleEntry<Object,Boolean>>();
		private HttpHeaders headerParams;
		private Map<String, Object> formParams = new HashMap<String, Object>();
		private Object body;
		private Serializer bodySerializer;
		
		public Builder(String server, String path) {
			this.server = server;
			this.path = path;
		}
		
		
		/**
		 * Setter for httpMethod
		 * @param httpMethod HttpMethod value for httpMethod
		 * @return Builder
		 */
		public Builder httpMethod(HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}
		
		/**
		 * Setter for requiresAuth
		 * @param requiresAuth boolean value for requiresAuth
		 * @return Builder
		 */
		public Builder requiresAuth(boolean requiresAuth) {
			this.requiresAuth = requiresAuth;
			return this;
		}
		
		/**
		 * Key value pair for queryParams
		 * @param key String value for key
		 * @param param Object value for param
		 * @return Builder
		 */
		public Builder queryParams(String key, Object param) {
			if (key == null || key.isEmpty()) {
				throw new IllegalArgumentException("The specified key value is null or empty");
			}
			this.queryParams.put(key, param);
			return this;
		}
		
		/**
		 * Key value pair for templateParams
		 * @param key String value for key
		 * @param param SimpleEntry<Object, Boolean> value for param
		 * @return Builder
		 */
		public Builder templateParams(String key, SimpleEntry<Object, Boolean> param) {
			if (key == null || key.isEmpty()) {
				throw new IllegalArgumentException("The specified key value is null or empty");
			}
			this.templateParams.put(key, param);
			return this;
		}
		
		/**
		 * Setter for httpHeaders
		 * @param headerParams HttpHeaders for headerParams
		 * @return Builder
		 */
		public Builder headerParams(HttpHeaders headerParams) {
			this.headerParams = headerParams;
			return this;
		}
		
		/**
		 * Key value pair for formParams
		 * @param key String value for key
		 * @param param Object value for param
		 * @return Builder
		 */
		public Builder formParams(String key, Object param) {
			if (key == null || key.isEmpty()) {
				throw new IllegalArgumentException("The specified key value is null or empty");
			}
			this.formParams.put(key, param);
			return this;
		}
		
		/**
		 * Setter for body
		 * @param body Object value for body
		 * @return Builder
		 */
		public Builder body(Object body) {
			this.body = body;
			return this;
		}
		
		/**
		 * Setter for bodySerializer
		 * @param bodySerializer Function value for bodySerializer
		 * @return Builder
		 */
		public Builder bodySerializer(Serializer bodySerializer) {
			this.bodySerializer = bodySerializer;
			return this;
		}
		
		public HttpRequest build(CoreConfig coreConfig) throws IOException {
		    if(bodySerializer != null) {
		        System.out.println(bodySerializer.apply(body));
		    }
			// 
			//return coreConfig.getCompatibilityFactory().createHttpRequest(httpMethod, server, headerParams, queryParams, formParams);
			return null;
		}
		
	}
	
}
