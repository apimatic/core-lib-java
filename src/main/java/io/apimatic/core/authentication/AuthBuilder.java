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

public class AuthBuilder {

    private static final String AND = "AND";
    private static final String OR = "OR";

    private Map<String, List<AuthBuilder>> authBuilders;
    private List<String> authKeys;

    public AuthBuilder() {
        authBuilders = new HashMap<String, List<AuthBuilder>>();
        authBuilders.put(AND, new ArrayList<AuthBuilder>());
        authBuilders.put(OR, new ArrayList<AuthBuilder>());
        authKeys = new ArrayList<String>();
    }

    public AuthBuilder add(String authKey) {
        authKeys.add(authKey);
        return this;
    }

    public AuthBuilder and(Consumer<AuthBuilder> action) {
        AuthBuilder authBuilder = new AuthBuilder();
        action.accept(authBuilder);
        authBuilders.get(AND).add(authBuilder);
        return this;
    }

    public AuthBuilder or(Consumer<AuthBuilder> action) {
        AuthBuilder authBuilder = new AuthBuilder();
        action.accept(authBuilder);
        authBuilders.get(OR).add(authBuilder);
        return this;
    }

    public Authentication build(Map<String, Authentication> authManagers) {
        
    	if(authManagers == null || authManagers.isEmpty()) {
    		return null;
    	}
    	
        if (authBuilders.get(AND).isEmpty() && authBuilders.get(OR).isEmpty() && authKeys.isEmpty()) {
            return null;
        }
        
        Authentication mappedAuth = null;
        if (authBuilders.get(AND).isEmpty() && authBuilders.get(OR).isEmpty() && !authKeys.isEmpty()) {
            mappedAuth = new Single(authManagers.get(authKeys.get(0)));
            mappedAuth.validate();
            return mappedAuth;   
        }


        for (AuthBuilder authBuilder : authBuilders.get(AND)) {
            mappedAuth = new And(authBuilder.buildAuthGroup(authManagers));
        }

        for (AuthBuilder authBuilder : authBuilders.get(OR)) {
            mappedAuth = new Or(authBuilder.buildAuthGroup(authManagers));
        }

        mappedAuth.validate();
        
        return mappedAuth;
    }

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
