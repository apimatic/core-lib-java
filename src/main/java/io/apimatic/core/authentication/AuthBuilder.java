package io.apimatic.core.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import io.apimatic.core.authentication.multiple.And;
import io.apimatic.core.authentication.multiple.Or;
import io.apimatic.core.authentication.multiple.Single;
import io.apimatic.coreinterfaces.authentication.Authentication;

/**
 * A builder for authentication.
 */
public class AuthBuilder {

    /**
     * Constant for AND group identifier.
     */
    private static final String AND = "AND";

    /**
     * Constant for OR group identifier.
     */
    private static final String OR = "OR";

    /**
     * Holds nested combination of authentication.
     */
    private Map<String, List<AuthBuilder>> authBuilders;

    /**
     * Holds the authentication keys, must belong to the provided authentication managers.
     */
    private List<String> authKeys;

    /**
     * Default constructor.
     */
    public AuthBuilder() {
        authBuilders = new HashMap<String, List<AuthBuilder>>();
        authBuilders.put(AND, new ArrayList<AuthBuilder>());
        authBuilders.put(OR, new ArrayList<AuthBuilder>());
        authKeys = new ArrayList<String>();
    }

    /**
     * Registers the authentication key to the builder.
     * @param authKey A key pointing to some authentication in the provided auth managers.
     * @return {@link AuthBuilder} The instance of the current builder.
     */
    public AuthBuilder add(String authKey) {
        authKeys.add(authKey);
        return this;
    }

    /**
     * Registers the and group for authentication.
     * @param action A consumer for the nested builder.
     * @return {@link AuthBuilder} The instance of the current builder.
     */
    public AuthBuilder and(Consumer<AuthBuilder> action) {
        AuthBuilder authBuilder = new AuthBuilder();
        action.accept(authBuilder);
        authBuilders.get(AND).add(authBuilder);
        return this;
    }

    /**
     * Registers the or group for authentication.
     * @param action A consumer for the nested builder.
     * @return {@link AuthBuilder} The instance of the current builder.
     */
    public AuthBuilder or(Consumer<AuthBuilder> action) {
        AuthBuilder authBuilder = new AuthBuilder();
        action.accept(authBuilder);
        authBuilders.get(OR).add(authBuilder);
        return this;
    }

    /**
     * Builds and validates the authentication using registered authentication keys.
     * @param authManagers The map of authentication managers.
     * @return {@link Authentication} The validated instance of authentication.
     */
    public Authentication build(Map<String, Authentication> authManagers) {
        if (authManagers == null || authManagers.isEmpty()) {
            return null;
        }

        if (authBuilders.get(AND).isEmpty() && authBuilders.get(OR).isEmpty()
                && authKeys.isEmpty()) {
            return null;
        }

        Authentication mappedAuth = null;
        if (authBuilders.get(AND).isEmpty() && authBuilders.get(OR).isEmpty()
                && !authKeys.isEmpty()) {
            mappedAuth = new Single(authManagers.get(authKeys.get(0)));
            return mappedAuth;
        }

        for (AuthBuilder authBuilder : authBuilders.get(AND)) {
            mappedAuth = new And(authBuilder.buildAuthGroup(authManagers));
        }

        for (AuthBuilder authBuilder : authBuilders.get(OR)) {
            mappedAuth = new Or(authBuilder.buildAuthGroup(authManagers));
        }

        return mappedAuth;
    }

    /**
     * Builds the nested authentication groups.
     * @param authManagers The map of authentication managers.
     * @return List<{@link Authentication}> The converted instance of nested authentications.
     */
    private List<Authentication> buildAuthGroup(Map<String, Authentication> authManagers) {
        List<Authentication> auths = new ArrayList<Authentication>();

        authKeys.forEach(authKey -> {
            if (authManagers.containsKey(authKey)) {
                auths.add(new Single(authManagers.get(authKey)));
            }
        });

        authBuilders.get(AND).forEach(authBuilder -> {
            auths.add(new And(authBuilder.buildAuthGroup(authManagers)));
        });

        authBuilders.get(OR).forEach(authBuilder -> {
            auths.add(new Or(authBuilder.buildAuthGroup(authManagers)));
        });

        return auths;
    }
}
