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
    public Or(List<Authentication> authParticipants) {
        super(authParticipants);
    }

    /**
     * Validates the AND group authentication.
     * @return true if the auth group is valid, false otherwise.
     */
    public boolean validate() {
        List<Authentication> authParticipants = getAuthParticipants();

        if (authParticipants.isEmpty()) {
            return false;
        }

        boolean isValid = false;
        for (Authentication authParticipant : authParticipants) {
            if (authParticipant.validate()) {
                isValid = true;
            } else {
                getErrorMessages().add(authParticipant.getErrorMessage());
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

        return "[" + String.join(" or ", getErrorMessages()) + "]";
    }
}
