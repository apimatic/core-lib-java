package io.apimatic.core.authentication.multiple;

import java.util.ArrayList;
import java.util.List;

import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

/**
 * Handles the authentication group i.e. AND/OR.
 */
public abstract class AuthGroup extends Authentication {

    /**
     * Holds the list of authentication participants for this group.
     */
    private List<Authentication> authParticipants;

    /**
     * Holds the list of error messages for this group.
     */
    private List<String> errorMessages;

    /**
     * @param authParticipants List of authentication participants.
     */
    public AuthGroup(List<Authentication> authParticipants) {
        if (authParticipants == null) {
            throw new IllegalArgumentException("Auth Participants can not be null.");
        }

        this.authParticipants = authParticipants;
        errorMessages = new ArrayList<>();
	}

    /**
     * Applies the authentication on the httpRequest.
     * @param httpRequest the request on which authentication is being applied.
     * @return {@link Request} The authenticated request.
     */
    public Request apply(Request httpRequest) {
        for (Authentication participant : authParticipants) {
            httpRequest = participant.apply(httpRequest);
        }

        return httpRequest;
    }

    /**
     * Getter for the list of error message in this authentication group.
     * @return List&lt;String&gt; The list of error messages.
     */
    protected List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Getter for the list of participants in this authentication group.
     * @return List<{@link Authentication}> The list of error messages.
     */
    protected List<Authentication> getAuthParticipants() {
        return authParticipants;
    }
}
