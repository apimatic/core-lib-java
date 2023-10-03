package io.apimatic.core.authentication.multiple;

import java.util.List;

import io.apimatic.coreinterfaces.authentication.Authentication;

public class And extends AuthGroup {

	public And(List<Authentication> authParticipants) {
		super(authParticipants);
		setValidity(true);
	}

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

	@Override
	public String getErrorMessage() {
		List<String> errorMessages = getErrorMessages();
		if (errorMessages.size() == 1) {
			return errorMessages.get(0);
		}

		return "[" + String.join(" and ", getErrorMessages()) + "]";
	}
}
