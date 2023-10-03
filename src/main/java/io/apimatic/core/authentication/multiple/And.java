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
		setValidity(true);
	}

    /**
     * Validates the OR group authentication.
     */
	public void validate() {
		List<Authentication> authParticipants = getAuthParticipants();

		if (authParticipants == null || authParticipants.isEmpty()) {
			setValidity(false);
			return;
		}

		for (Authentication authParticipant : authParticipants) {
			authParticipant.validate();
			if (!authParticipant.isValid()) {
				getErrorMessages().add(authParticipant.getErrorMessage());
				setValidity(false);
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

		return "[" + String.join(" and ", getErrorMessages()) + "]";
	}
}
