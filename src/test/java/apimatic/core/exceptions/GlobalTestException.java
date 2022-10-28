/*
 * 
 *
 * This file was automatically generated for Stamplay by APIMATIC v3.0 ( https://www.apimatic.io ).
 */

package apimatic.core.exceptions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.Context;

/**
 * This is a model class for GlobalTestException type.
 */
public class GlobalTestException extends CoreApiException {
    /**
     * A serialVersionUID
     */
    private static final long serialVersionUID = 5538622956749835538L;

    /**
     * A server message string
     */
    private String serverMessage;

    /**
     * A server response code
     */
    private int serverCode;

    /**
     * Initialization constructor.
     * @param reason The reason for throwing exception
     * @param context The http context of the API exception
     */
    public GlobalTestException(String reason, Context context) {
        super(reason, context);
    }


    /**
     * Getter for ServerMessage. Represents the server's exception message
     * @return Returns the String
     */
    @JsonGetter("ServerMessage")
    public String getServerMessage() {
        return this.serverMessage;
    }

    /**
     * Setter for ServerMessage. Represents the server's exception message
     * @param serverMessage Value for String
     */
    @JsonSetter("ServerMessage")
    private void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    /**
     * Getter for ServerCode. Represents the server's error code
     * @return Returns the int
     */
    @JsonGetter("ServerCode")
    public int getServerCode() {
        return this.serverCode;
    }

    /**
     * Setter for ServerCode. Represents the server's error code
     * @param serverCode Value for int
     */
    @JsonSetter("ServerCode")
    private void setServerCode(int serverCode) {
        this.serverCode = serverCode;
    }
}
