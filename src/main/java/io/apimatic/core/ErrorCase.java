package io.apimatic.core;

import java.io.StringReader;
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
    private boolean isErrorTemplate;

    /**
     * An instance of {@link ExceptionCreator}.
     */
    private ExceptionCreator<ExceptionType> exceptionCreator;

    /**
     * A private constructor.
     * @param reason the exception reason.
     * @param exceptionCreator the exceptionCreator.
     * @param isErrorTemplate error case for error template.
     */
    private ErrorCase(final String reason, final ExceptionCreator<ExceptionType> exceptionCreator,
            boolean isErrorTemplate) {
        this.reason = reason;
        this.exceptionCreator = exceptionCreator;
        this.isErrorTemplate = isErrorTemplate;
    }

    /**
     * this method throw the configured exception using functional interface.
     * @param httpContext is wrapped the request sent to the server and the response received from
     *        the server.
     * @throws ExceptionType Represents error response from the server.
     */
    public void throwException(Context httpContext) throws ExceptionType {
        if (isErrorTemplate) {
            replacePlaceHolder(httpContext.getResponse());
        }
        throw exceptionCreator.apply(reason, httpContext);
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
    public static <ExceptionType extends CoreApiException> ErrorCase<ExceptionType> setReason(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        ErrorCase<ExceptionType> errorCase =
                new ErrorCase<ExceptionType>(reason, exceptionCreator, false);
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
    public static <ExceptionType extends CoreApiException> ErrorCase<ExceptionType> setTemplate(
            String reason, ExceptionCreator<ExceptionType> exceptionCreator) {
        return new ErrorCase<ExceptionType>(reason, exceptionCreator, true);
    }

    private void replacePlaceHolder(Response response) {
        replaceStatusCodeFromTemplate(response.getStatusCode());
        replaceHeadersFromTemplate(response.getHeaders());
        replaceBodyFromTemplate(response.getBody());
    }

    private void replaceHeadersFromTemplate(HttpHeaders headers) {
        StringBuilder formatter = new StringBuilder(reason);
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(reason);
        while (matcher.find()) {
            String key = matcher.group(1);
            String pointerKey = key;
            if (pointerKey.startsWith("$response.header.")) {
                pointerKey = pointerKey.replace("$response.header.", "");
                String formatKey = String.format("{%s}", key);
                int index = formatter.indexOf(formatKey);
                pointerKey = pointerKey.toLowerCase();
                if (index != -1) {
                    formatter.replace(index, index + formatKey.length(),
                            "" + (headers.has(pointerKey) ? headers.value(pointerKey) : ""));
                }
            }
        }
        reason = formatter.toString();
    }

    private void replaceBodyFromTemplate(String responseBody) {
        StringBuilder formatter = new StringBuilder(reason);
        JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
        JsonStructure jsonStructure = null;
        try {
            jsonStructure = jsonReader.read();
        } catch (Exception e) {
            // No need to do anything here
        }
        jsonReader.close();
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(reason);
        while (matcher.find()) {
            String key = matcher.group(1);
            String pointerKey = key;
            replaceBodyString(responseBody, formatter, jsonStructure, key, pointerKey);
        }
        reason = formatter.toString().replaceAll("\"", "");
    }

    private void replaceBodyString(String responseBody, StringBuilder formatter,
            JsonStructure jsonStructure, String key, String pointerKey) {
        if (pointerKey.startsWith("$response.body")) {
            String formatKey = String.format("{%s}", key);
            int index = formatter.indexOf(formatKey);
            String toReplaceString = "";
            toReplaceString = extractReplacementString(responseBody, jsonStructure, pointerKey,
                    toReplaceString);
            if (index != -1) {
                try {

                    formatter.replace(index, index + formatKey.length(), toReplaceString);
                } catch (JsonException ex) {
                    formatter.replace(index, index + formatKey.length(), "");
                }
            }
        }
    }

    private String extractReplacementString(String responseBody, JsonStructure jsonStructure,
            String pointerKey, String toReplaceString) {
        if (pointerKey.contains("#")) {
            pointerKey = pointerKey.replace("$response.body#", "");
            JsonPointer jsonPointer = Json.createPointer(pointerKey);
            if (jsonStructure != null && jsonPointer.containsValue(jsonStructure)) {
                toReplaceString = jsonPointer.getValue(jsonStructure).toString();
            }
        } else {
            if (responseBody != null && !responseBody.isEmpty()) {
                toReplaceString = responseBody;
            }
        }
        return toReplaceString;
    }

    private void replaceStatusCodeFromTemplate(int statusCode) {
        StringBuilder formatter = new StringBuilder(reason);
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(reason);
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
        reason = formatter.toString();
    }
}
