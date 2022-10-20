package io.apimatic.core.types.http.response;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.response.DynamicType;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Represents dynamic response returned by an API call. Allows user to lazily parse the response as
 * a primitive or a more complex type using parse().
 */
public class Dynamic implements DynamicType {
    private Response response;

    /**
     * Instantiate class.
     * 
     * @param responseBody The object of HttpResponse
     */
    public Dynamic(Response responseBody) {
        this.response = responseBody;
    }

    /**
     * Parse response as instance of class cls.
     * 
     * @param <T> The type of class to be parsed
     * @param cls Class to be parsed
     * @return Object of type T
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public <T> T parse(Class<T> cls) throws ParseException {
        try {
            return CoreHelper.deserialize(getResponseString(), cls);
        } catch (Exception e) {
            throw new java.text.ParseException(
                    "Could not deserialize dynamic content as given type", 0);
        }
    }

    /**
     * Parse response as boolean.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public boolean parseAsBoolean() throws ParseException {
        return this.parse(Boolean.class);
    }

    /**
     * Parse response as boolean.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public byte parseAsByte() throws ParseException {
        return this.parse(Byte.class);
    }

    /**
     * Parse response as character.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public char parseAsCharacter() throws ParseException {
        return this.parse(Character.class);
    }

    /**
     * Parse response as float.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public float parseAsFloat() throws ParseException {
        return this.parse(Float.class);
    }

    /**
     * Parse response as integer.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public int parseAsInteger() throws ParseException {
        return this.parse(Integer.class);
    }

    /**
     * Parse response as long.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public long parseAsLong() throws ParseException {
        return this.parse(Long.class);
    }

    /**
     * Parse response as short.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public short parseAsShort() throws ParseException {
        return this.parse(Short.class);
    }

    /**
     * Parse response as double.
     * 
     * @return Parsed value
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public double parseAsDouble() throws ParseException {
        return this.parse(Double.class);
    }

    /**
     * Parse response as string.
     * 
     * @return Parsed value
     */
    @Override
    public String parseAsString() {
        return getResponseString();
    }

    /**
     * Parse response as a map of keys and values.
     * 
     * @return Parsed map
     * @throws ParseException Signals if a parse exception occured
     */
    @Override
    public Map<String, Object> parseAsDictionary() throws ParseException {
        try {
            return CoreHelper.deserialize(getResponseString());
        } catch (IOException e) {
            throw new java.text.ParseException(
                    "Could not deserialize dynamic content as given type", 0);
        }
    }

    /**
     * Get the raw stream for the response body.
     * 
     * @return Raw body
     */
    @Override
    public InputStream getRawBody() {
        return response.getRawBody();
    }

    /**
     * Get response headers for the HTTP response.
     * 
     * @return Headers
     */
    @Override
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    /**
     * Get response as string.
     * 
     * @return The Response String
     */
    private String getResponseString() {
        return response.getBody();
    }
}
