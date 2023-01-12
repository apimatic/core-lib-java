package io.apimatic.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonPointer;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.type.functional.ExceptionCreator;

/**
 * A class is responsible to generate the SDK Exception.
 * @param <ExceptionType> Represents error response from the server.
 */
public final class ErrorCase<ExceptionType extends CoreApiException> {
    /**
     * A key for the default errors.
     */
    public static final String DEFAULT = "DEFAULT";

    /**
     * A error's reason.
     */
    private String reason;

    /**
     * Error message a template or not.
     */
    private static boolean isErrorTemplate;

    /**
     * An instance of {@link ExceptionCreator}.
     */
    private ExceptionCreator<ExceptionType> exceptionCreator;

    /**
     * A private constructor.
     * @param reason the exception reason.
     * @param exceptionCreator the exceptionCreator.
     */
    private ErrorCase(final String reason, final ExceptionCreator<ExceptionType> exceptionCreator) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
    }

    /**
     * this method throw the configured exception using functional interface.
     * @param httpContext is wrapped the request sent to the server and the response received from
     *        the server.
     * @throws ExceptionType Represents error response from the server.
     */
    public void throwException(Context httpContext) throws ExceptionType {
        throw exceptionCreator.apply(getReason(httpContext), httpContext);
    }

    /**
     * Create the errorcase using the error reason and exception creator functional interface which
     * throws the respective exception while throwing.
     * @param <ExceptionType> Represents error response from the server.
     * @param reason the exception message.
     * @param exceptionCreator the functional interface which is responsible to create the server
     *        thrown exception.
     * @return {@link ErrorCase}.
     */
    public static <ExceptionType extends CoreApiException> ErrorCase<ExceptionType> create(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        ErrorCase<ExceptionType> errorCase = new ErrorCase<ExceptionType>(reason, exceptionCreator);
        return errorCase;
    }

    /**
     * Create the errorcase using the error reason and exception creator functional interface which
     * throws the respective exception while throwing.
     * @param <ExceptionType> Represents error response from the server.
     * @param reason the exception message.
     * @param exceptionCreator the functional interface which is responsible to create the server
     *        thrown exception.
     * @return {@link ErrorCase}.
     */
    public static <ExceptionType extends CoreApiException> ErrorCase<ExceptionType> createErrorTemplate(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        isErrorTemplate = true;
        return new ErrorCase<ExceptionType>(reason, exceptionCreator);
    }


    private String getReason(Context context) {
        if (!isErrorTemplate) {
            return reason;
        }
        reason = replacePlaceHolder(context.getResponse(), reason);
        return reason;
    }

    /**
     * Replace the placeholder of error template.
     * @param response A request response from server side.
     * @param format A error template.
     * @return A updated string
     */
    private String replacePlaceHolder(Response response, String format) {
        format = replaceStatusCodePlaceHolder(format, response.getStatusCode()); // improve the method name
        format = replaceHeadersPlaceHolder(format, response.getHeaders());
        format = replaceBodyPlaceHolder(format, response.getBody());
        return format;
    }

    private String replaceHeadersPlaceHolder(String format, HttpHeaders headers) {
        StringBuilder formatter = new StringBuilder(format);
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(format);
        while (matcher.find()) {
            String key = matcher.group(1);
            String pointerKey = key;
            if (pointerKey.startsWith("$response.header.")) {
                pointerKey = pointerKey.replace("$response.header.", "");
                String formatKey = String.format("{%s}", key);
                int index = formatter.indexOf(formatKey);
                if (index != -1) {
                    formatter.replace(index, index + formatKey.length(),
                            "" + (headers.has(pointerKey) ? headers.value(pointerKey) : ""));
                }
            }
        }
        return formatter.toString();
    }

    private String replaceBodyPlaceHolder(String format, String responseBody) {
        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        StringBuilder formatter = new StringBuilder(format);
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(format);
        JsonReader JsonReader = Json.createReader(reader);
        JsonStructure jsonStructure = JsonReader.read();
        JsonReader.close();
        while (matcher.find()) {
            String key = matcher.group(1);
            String pointerKey = key;
            if (pointerKey.startsWith("$response.body#")) {
                pointerKey = pointerKey.replace("$response.body#", "");
                JsonPointer jsonPointer = Json.createPointer(pointerKey);
                String formatKey = String.format("{%s}", key);
                int index = formatter.indexOf(formatKey);
                if (index != -1) {
                    try {
                        formatter.replace(index, index + formatKey.length(),
                                "" + (jsonPointer.containsValue(jsonStructure)
                                        ? jsonPointer.getValue(jsonStructure).toString().replaceAll("^\"|\"$", "")
                                        : ""));
                    } catch (JsonException ex) {
                        formatter.replace(index, index + formatKey.length(), "");
                    }
                }
            }
        }
        return formatter.toString();
    }

    private static String replaceStatusCodePlaceHolder(String format, int statusCode) {
        StringBuilder formatter = new StringBuilder(format);
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(format);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (key.equals("$statusCode")) {
                String formatKey = String.format("{%s}", key);
                int index = formatter.indexOf(formatKey);
                if (index != -1) {
                    formatter.replace(index, index + formatKey.length(), "" + statusCode);
                }
            }
        }
        return formatter.toString();
    }
}
