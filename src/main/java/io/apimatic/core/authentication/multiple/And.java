package io.apimatic.core.authentication.multiple;

import java.util.List;

import io.apimatic.coreinterfaces.authentication.Authentication;

/**
 * Holds the AND group authentication.
 */
public class And extends AuthGroup {

    /**
     * @param authParticipants List of authentication participants.
     */
    public And(List<Authentication> authParticipants) {
        super(authParticipants);
    }

    /**
     * Validates the OR group authentication.
     * @return true if the auth group are valid, false otherwise.
     */
    public boolean validate() {
        List<Authentication> authParticipants = getAuthParticipants();

        if (authParticipants == null || authParticipants.isEmpty()) {
            return false;
        }

        boolean isValid = true;
        for (Authentication authParticipant : authParticipants) {
            if (!authParticipant.validate()) {
                getErrorMessages().add(authParticipant.getErrorMessage());
                isValid = false;
            }
        }

        return isValid;
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

        return "[" + String.join(" and ", getErrorMessages()) + "]";
    }
}
