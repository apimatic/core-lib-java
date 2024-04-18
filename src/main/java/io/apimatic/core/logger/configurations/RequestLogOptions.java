package io.apimatic.core.logger.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogging;

/**
 * Represents options for logging requests.
 */
public class RequestLogOptions extends LogBaseOptions implements ReadonlyRequestLogging {

	/**
	 * Stores the value for flag to include query parameters.
	 */
	private boolean includeQueryInPath;

	/**
	 * Constructs a new RequestLogOptions instance with default values.
	 */
	private RequestLogOptions(Builder builder) {
		super();
		this.setLogBody(builder.logBody);
		this.setLogHeaders(builder.logHeaders);
		this.excludeHeaders(builder.excludeHeaders.toArray(new String[0]));
		this.includeHeaders(builder.includeHeaders.toArray(new String[0]));
		this.includeQueryInPath = builder.includeQueryInPath;
	}

	/**
	 * Checks if query parameters are included in the request path.
	 * 
	 * @return True if query parameters are included in the path, otherwise false.
	 */
	public boolean shouldIncludeQueryInPath() {
		return includeQueryInPath;
	}

	/**
	 * Builds a new {@link RequestLogOptions.Builder} object. Creates the
	 * instance with the current state.
	 * 
	 * @return a new {@link RequestLogOptions.Builder} object.
	 */
	public Builder newBuilder() {
		return new Builder().logBody(shouldLogBody()).logHeaders(shouldLogHeaders())
				.excludeHeaders(getHeadersToExclude().toArray(new String[0]))
				.includeHeaders(getHeadersToInclude().toArray(new String[0]))
				.includeQueryInPath(shouldIncludeQueryInPath());
	}

	/**
	 * Builder class for RequestLogOptions.
	 */
	public static class Builder {
		private boolean logBody = false;
		private boolean logHeaders = false;
		private boolean includeQueryInPath = true;
		private List<String> excludeHeaders = new ArrayList<>();
		private List<String> includeHeaders = new ArrayList<>();

		/**
		 * Sets whether to log the body of the request.
		 * 
		 * @param logBody True to log the body, otherwise false.
		 * @return The builder instance.
		 */
		public Builder logBody(boolean logBody) {
			this.logBody = logBody;
			return this;
		}

		/**
		 * Sets whether to log the headers of the request.
		 * 
		 * @param logHeaders True to log the headers, otherwise false.
		 * @return The builder instance.
		 */
		public Builder logHeaders(boolean logHeaders) {
			this.logHeaders = logHeaders;
			return this;
		}

		/**
		 * Sets the headers to be excluded from logging.
		 * 
		 * @param excludeHeaders The headers to exclude.
		 * @return The builder instance.
		 */
		public Builder excludeHeaders(String... excludeHeaders) {
			this.excludeHeaders = Arrays.stream(excludeHeaders).map(String::toLowerCase).collect(Collectors.toList());
			return this;
		}

		/**
		 * Sets the headers to be included in logging.
		 * 
		 * @param includeHeaders The headers to include.
		 * @return The builder instance.
		 */
		public Builder includeHeaders(String... includeHeaders) {
			this.includeHeaders = Arrays.stream(includeHeaders).map(String::toLowerCase).collect(Collectors.toList());
			return this;
		}

		/**
		 * Sets whether to include query parameters in the request path.
		 * 
		 * @param includeQueryInPath True to include query parameters in the path,
		 *                           otherwise false.
		 * @return The builder instance.
		 */
		public Builder includeQueryInPath(boolean includeQueryInPath) {
			this.includeQueryInPath = includeQueryInPath;
			return this;
		}

		/**
		 * Constructs a RequestLogOptions object with the set values.
		 * 
		 * @return The constructed RequestOptions object.
		 */
		public RequestLogOptions build() {
			return new RequestLogOptions(this);
		}
	}
}
