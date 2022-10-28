package apimatic.core.utilities;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.coreinterfaces.compatibility.CompatibilityFactory;

public class CompatibilityFactoryMock extends CoreHttpRequestMock {

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link CompatibilityFactory}
     */
    @Mock
    private CompatibilityFactory compatibilityFactory;

    /**
     * @return {@link CompatibilityFactoryF}
     */
    public CompatibilityFactory getCompatibilityFactory() {
        return compatibilityFactory;
    }
}
