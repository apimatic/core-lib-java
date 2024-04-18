package io.apimatic.core.logger.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogOptions;

/**
 * Represents options for logging responses.
 */
public class ResponseLogOptions extends LogBaseOptions implements ReadonlyResponseLogOptions {

	/**
	 * Constructs a new ResponseLogOptions instance with default values.
	 */
	private ResponseLogOptions(Builder builder) {
		super();
		this.setLogBody(builder.logBody);
		this.setLogHeaders(builder.logHeaders);
		this.excludeHeaders(builder.excludeHeaders.toArray(new String[0]));
		this.includeHeaders(builder.includeHeaders.toArray(new String[0]));
	}

	/**
	 * Builds a new {@link ResponseLogOptions.Builder} object. Creates the
	 * instance with the current state.
	 * 
	 * @return a new {@link ResponseLogOptions.Builder} object.
	 */
	public Builder newBuilder() {
		return new Builder().logBody(shouldLogBody()).logHeaders(shouldLogHeaders())
				.excludeHeaders(getHeadersToExclude().toArray(new String[0]))
				.includeHeaders(getHeadersToInclude().toArray(new String[0]));
	}

	/**
	 * Builder class for ResponseLogOptions.
	 */
	public static class Builder {
		private boolean logBody = false;
		private boolean logHeaders = false;
		private List<String> excludeHeaders = new ArrayList<>();
		private List<String> includeHeaders = new ArrayList<>();

		/**
		 * Sets whether to log the response body.
		 * 
		 * @param logBody True to enable logging of response body, otherwise false.
		 * @return The builder instance.
		 */
		public Builder logBody(boolean logBody) {
			this.logBody = logBody;
			return this;
		}

		/**
		 * Sets whether to log the response headers.
		 * 
		 * @param logHeaders True to enable logging of response headers, otherwise
		 *                   false.
		 * @return The builder instance.
		 */
		public Builder logHeaders(boolean logHeaders) {
			this.logHeaders = logHeaders;
			return this;
		}

		/**
		 * Sets the headers to be excluded from logging.
		 * 
		 * @param excludeHeaders The headers to exclude from logging.
		 * @return The builder instance.
		 */
		public Builder excludeHeaders(String... excludeHeaders) {
			this.excludeHeaders = Arrays.stream(excludeHeaders).map(String::toLowerCase).collect(Collectors.toList());
			return this;
		}

		/**
		 * Sets the headers to be included in logging.
		 * 
		 * @param includeHeaders The headers to include in logging.
		 * @return The builder instance.
		 */
		public Builder includeHeaders(String... includeHeaders) {
			this.includeHeaders = Arrays.stream(includeHeaders).map(String::toLowerCase).collect(Collectors.toList());
			return this;
		}

		/**
		 * Constructs a ResponseLogOptions object with the set values.
		 * 
		 * @return The constructed ResponseLogOptions object.
		 */
		public ResponseLogOptions build() {
			return new ResponseLogOptions(this);
		}
	}
}
