package apimatic.core.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.apimatic.core.authentication.AuthBuilder;
import io.apimatic.core.authentication.HeaderAuth;
import io.apimatic.core.authentication.QueryAuth;
import io.apimatic.coreinterfaces.authentication.Authentication;

@SuppressWarnings("serial")
public class AuthBuilderTest {

    @Test
    public void testInvalidAuthManager() {
        // Test with null authentication managers map
        Authentication auth = new AuthBuilder().add("basic-auth").build(null);

        assertNull(auth);

        // Test with an empty authentication managers map
        auth = new AuthBuilder().add("basic-auth").build(new HashMap<String, Authentication>());

        assertNull(auth);
    }

    @Test
    public void testValidSingleHeaderAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
            }
        };

        Authentication auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertTrue(auth.validate());
    }

    @Test
    public void testValidSingleQueryAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new QueryAuth(Collections.singletonMap("username", "password")));
            }
        };

        Authentication auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertTrue(auth.validate());
    }

    @Test
    public void testInvalidSingleHeaderAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, "password")));
            }
        };

        Authentication auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap("username", null)));
            }
        };

        auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());
    }

    @Test
    public void testInvalidSingleQueryAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new QueryAuth(Collections.singletonMap(null, "password")));
            }
        };

        Authentication auth = new AuthBuilder().add("basic-auth").build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new QueryAuth(Collections.singletonMap("username", null)));
            }
        };

        auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new QueryAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder().add("basic-auth").build(authManagers);
        auth.validate();

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());
    }

    @Test
    public void testValidAndAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key", "A1B2C3")));
            }
        };

        Authentication auth = new AuthBuilder()
                .and(andAuth -> andAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertTrue(auth.validate());
    }

    @Test
    public void testInvalidAndAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, "password")));
                put("query-auth", new QueryAuth(Collections.singletonMap("x-api-key", "A1B2C3")));
            }
        };

        Authentication auth = new AuthBuilder()
                .and(andAuth -> andAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, "password")));
                put("query-auth", new QueryAuth(Collections.singletonMap(null, "A1B2C3")));
            }
        };

        auth = new AuthBuilder()
                .and(andAuth -> andAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[[Auth key and value cannot be null] "
                + "and [Auth key and value cannot be null]]",
                auth.getErrorMessage());
    }

    @Test
    public void testValidOrAuth() {
        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key", "A1B2C3")));
            }
        };

        Authentication auth = new AuthBuilder()
                .or(orAuth -> orAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertTrue(auth.validate());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, "password")));
                put("query-auth", new QueryAuth(Collections.singletonMap("x-api-key", "A1B2C3")));
            }
        };

        auth = new AuthBuilder()
                .or(orAuth -> orAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertTrue(auth.validate());
    }

    @Test
    public void testInvalidOrAuth() {

        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth", new HeaderAuth(Collections.singletonMap(null, "password")));
                put("query-auth", new QueryAuth(Collections.singletonMap(null, "A1B2C3")));
            }
        };

        Authentication auth = new AuthBuilder()
                .or(orAuth -> orAuth.add("basic-auth").add("query-auth"))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[[Auth key and value cannot be null]"
                + " or [Auth key and value cannot be null]]",
                auth.getErrorMessage());
    }

    @Test
    public void testValidComplexAuth() {

        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key-query", "A1B2C3")));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap("x-custom-header", "123456")));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap("x-custom-query", "QWERTY")));
            }
        };

        Authentication auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertTrue(auth.validate());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key-query", "A1B2C3")));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap("x-custom-header", "123456")));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertTrue(auth.validate());
    }

    @Test
    public void testInvalidComplexAuth() {

        Map<String, Authentication> authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key-query", "A1B2C3")));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap("x-custom-header", "123456")));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap("x-custom-query", "QWERTY")));
            }
        };

        Authentication auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[Auth key and value cannot be null]", auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap("x-custom-header", "123456")));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[[Auth key and value cannot be null]"
                + " and [Auth key and value cannot be null]]",
                auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap("username", "password")));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap("x-api-key-query", "A1B2C3")));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap("x-api-key-header", "ABCDEF")));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals("[[Auth key and value cannot be null]"
                + " or [Auth key and value cannot be null]]",
                auth.getErrorMessage());

        authManagers = new HashMap<String, Authentication>() {
            {
                put("basic-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
                put("header-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("custom-header-auth",
                        new HeaderAuth(Collections.singletonMap(null, null)));
                put("custom-query-auth",
                        new QueryAuth(Collections.singletonMap(null, null)));
            }
        };

        auth = new AuthBuilder()
                .and(andAuth -> andAuth
                        .add("basic-auth")
                        .and(orAuth -> orAuth.add("query-auth").add("header-auth"))
                        .or(orAuth -> orAuth.add("custom-header-auth").add("custom-query-auth")))
                .build(authManagers);

        assertNotNull(auth);
        assertFalse(auth.validate());
        assertEquals(
                "[[Auth key and value cannot be null] and "
                        + "[[Auth key and value cannot be null] "
                        + "and [Auth key and value cannot be null]] and "
                        + "[[Auth key and value cannot be null] or "
                        + "[Auth key and value cannot be null]]]",
                auth.getErrorMessage());
    }
}
