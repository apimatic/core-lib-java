package io.apimatic.core.authentication.multiple;

import java.util.List;

import io.apimatic.coreinterfaces.authentication.Authentication;

public class Or extends AuthGroup {

	public Or(List<Authentication> authParticipants) {
		super(authParticipants);
		setValidity(false);
	}

	@Override
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
			} else {
				getErrorMessages().add(authParticipant.getErrorMessage());
			}
		}
	}

	@Override
	public String getErrorMessage() {
		List<String> errorMessages = getErrorMessages();
		if (errorMessages.size() == 1) {
			return errorMessages.get(0);
		}

		return "[" + String.join(" or ", getErrorMessages()) + "]";
	}

}
