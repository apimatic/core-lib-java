package io.apimatic.core.authentication.multiple;

import java.util.List;

import io.apimatic.coreinterfaces.authentication.Authentication;

/**
 * Handles the OR authentication group.
 */
public class Or extends AuthGroup {

    /**
     * @param authParticipants List of authentication participants.
     */
    public Or(final List<Authentication> authParticipants) {
        super(authParticipants);
        setValidity(false);
    }

    /**
     * Validates the AND group authentication.
     */
    public void validate() {
        List<Authentication> authParticipants = getAuthParticipants();

        if (authParticipants.isEmpty()) {
            setValidity(false);
            return;
        }

        for (Authentication authParticipant : authParticipants) {
            authParticipant.validate();
            if (authParticipant.isValid()) {
                setValidity(true);
                return;
            } else {
                getErrorMessages().add(authParticipant.getErrorMessage());
            }
        }
    }

    /**
     * Getter for the error message.
     * @return String the consolidated error messages in this authentication group.
     */
    public String getErrorMessage() {
        List<String> errorMessages = getErrorMessages();
        if (errorMessages.size() == 1) {
            return errorMessages.get(0);
        }

        return "[" + String.join(" or ", getErrorMessages()) + "]";
    }
}
