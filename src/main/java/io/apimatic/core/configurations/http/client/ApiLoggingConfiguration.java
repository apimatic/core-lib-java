package io.apimatic.core.configurations.http.client;

import java.util.function.Consumer;

import org.slf4j.Logger;

import io.apimatic.core.logger.configurations.RequestLogOptions;
import io.apimatic.core.logger.configurations.ResponseLogOptions;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogging;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogging;

/**
 * To hold logging configuration.
 */
public final class ApiLoggingConfiguration implements ReadonlyLogging {
	/**
	 * Level enum to use with level in {@link ApiLoggingConfiguration.Builder}. An
	 * instance of {@link LoggingLevel}.
	 */
	public enum Level {
		/**
		 * To log with logging level 'INFO'.
		 */
		INFO,

		/**
		 * To log with logging level 'Error'.
		 */
		ERROR,

		/**
		 * To log with logging level 'WARN'.
		 */
		WARN,

		/**
		 * To log with logging level 'DEBUG'.
		 */
		DEBUG,

		/**
		 * To log with logging level 'TRACE'.
		 */
		TRACE
	}

	/***
	 * An instance of Logger
	 */
	private Logger logger;

	/**
	 * An instance of {@link LoggingLevel}.
	 */
	private LoggingLevel level;

	/**
	 * Options for logging requests.
	 */
	private RequestLogOptions requestLogOptions;

	/**
	 * Options for logging responses.
	 */
	private ResponseLogOptions responseLogOptions;

	/**
	 * @param level
	 */
	private ApiLoggingConfiguration(final Logger logger, final LoggingLevel level,
			final RequestLogOptions requestLogOptions, final ResponseLogOptions responseLogOptions) {
		this.logger = logger;
		this.level = level;
		this.requestLogOptions = requestLogOptions;
		this.responseLogOptions = responseLogOptions;
	}

	/**
	 * Getter for Logger.
	 * 
	 * @return Logger instance.
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Getter for level.
	 * 
	 * @return Level of logging.
	 */
	public LoggingLevel getLevel() {
		return level;
	}

	/**
	 * Getter for the RequestLogOptions.
	 * 
	 * @return The RequestLogOptions object.
	 */
	public ReadonlyRequestLogging getRequestLogOptions() {
		return requestLogOptions;
	}

	/**
	 * Getter for the ResponseLogOptions.
	 * 
	 * @return The ResponseLogOptions object.
	 */
	public ReadonlyResponseLogging getResponseLogOptions() {
		return responseLogOptions;
	}

	/**
	 * Converts this LoggingConfiguration into string format.
	 * 
	 * @return String representation of this class.
	 */
	@Override
	public String toString() {
		return "LoggingConfiguration [logger=" + getLogger() + "level=" + getLevel() + "requestLogOptions"
				+ getRequestLogOptions() + "responseLogOptions" + getResponseLogOptions() + "]";
	}

	/**
	 * Builds a new {@link ApiLoggingConfiguration.Builder} object. Creates the
	 * instance with the current state.
	 * 
	 * @return a new {@link ApiLoggingConfiguration.Builder} object.
	 */
	public Builder newBuilder() {
		return new Builder().logger(logger).level(level)
				.requestLogOptions(builder -> builder = requestLogOptions.newBuilder())
				.responseLogOptions(builder -> builder = responseLogOptions.newBuilder());
	}

	/**
	 * Class to build instances of {@link ApiLoggingConfiguration}.
	 */
	public static class Builder {
		/***
		 * An instance of Logger
		 */
		private Logger logger;
		/**
		 * An instance of {@link LoggingLevel}.
		 */
		private LoggingLevel level;

		/**
		 * Options for logging requests.
		 */
		private RequestLogOptions.Builder requestLogOptionsBuilder = new RequestLogOptions.Builder();

		/**
		 * Options for logging responses.
		 */
		private ResponseLogOptions.Builder responseLogOptionsBuilder = new ResponseLogOptions.Builder();

		/***
		 * Set Logger for logging
		 * 
		 * @param logger Logger instance
		 * @return {@link ApiLoggingConfiguration.Builder}.
		 */
		public Builder logger(Logger logger) {
			this.logger = logger;
			return this;
		}

		/**
		 * Set level for logging.
		 * 
		 * @param level specify level of all logs.
		 * @return {@link ApiLoggingConfiguration.Builder}.
		 */
		public Builder level(LoggingLevel level) {
			this.level = level;
			return this;
		}

		/**
		 * Sets the RequestLogOptions.Builder for the builder.
		 * 
		 * @param requestLogOptionsBuilderAction The RequestOptions Builder object.
		 * @return This Builder object.
		 */
		public Builder requestLogOptions(Consumer<RequestLogOptions.Builder> requestLogOptionsBuilderAction) {
			if (requestLogOptionsBuilder != null) {
				requestLogOptionsBuilderAction.accept(this.requestLogOptionsBuilder);
			}
			return this;
		}

		/**
		 * Sets the ResponseLogOptions.Builder for the builder.
		 * 
		 * @param requestLogOptionsBuilderAction The ResponseOptions Builder object.
		 * @return This Builder object.
		 */
		public Builder responseLogOptions(Consumer<ResponseLogOptions.Builder> responseLogOptionsBuilderAction) {
			if (responseLogOptionsBuilder != null) {
				responseLogOptionsBuilderAction.accept(this.responseLogOptionsBuilder);
			}
			return this;
		}

		/**
		 * Builds a new LoggingConfiguration object using the set fields.
		 * 
		 * @return {@link ApiLoggingConfiguration}.
		 */
		public ApiLoggingConfiguration build() {
			RequestLogOptions requestLogOptions = requestLogOptionsBuilder.build();
			ResponseLogOptions responseLogOptions = responseLogOptionsBuilder.build();
			return new ApiLoggingConfiguration(logger, level, requestLogOptions, responseLogOptions);
		}
	}
}
