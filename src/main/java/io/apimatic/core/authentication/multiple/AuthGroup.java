package io.apimatic.core.authentication.multiple;

import java.util.ArrayList;
import java.util.List;
import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

public abstract class AuthGroup extends Authentication {

    private List<Authentication> authParticipants;

    private List<String> errorMessages;

    public AuthGroup(List<Authentication> authParticipants) {
        if (authParticipants == null)
            throw new IllegalArgumentException("Auth Participants can not be null.");

        this.authParticipants = authParticipants;
        errorMessages = new ArrayList<>();
    }

    public Request apply(Request httpRequest) {
        if (!isValid()) {
            return httpRequest;
        }

        for (Authentication participant : authParticipants) {
            httpRequest = participant.apply(httpRequest);
        }

        return httpRequest;
    }

    protected List<String> getErrorMessages() {
        return errorMessages;
    }

    protected List<Authentication> getAuthParticipants() {
        return authParticipants;
    }

}
