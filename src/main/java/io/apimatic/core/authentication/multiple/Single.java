package io.apimatic.core.authentication.multiple;

import io.apimatic.coreinterfaces.authentication.Authentication;
import io.apimatic.coreinterfaces.http.request.Request;

public class Single extends Authentication {
	private Authentication authentication;

	public Single(Authentication authentication) {
		this.authentication = authentication;
	}

	public Request apply(Request httpRequest) {
		if (!isValid()) {
			return httpRequest;
		}

		return authentication.apply(httpRequest);
	}

	public void validate() {
		authentication.validate();
		setValidity(authentication.isValid());
		if (!isValid()) {
			setErrorMessage(authentication.getErrorMessage());
		}
	}
}
