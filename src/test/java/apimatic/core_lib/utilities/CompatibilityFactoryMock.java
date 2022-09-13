package apimatic.core_lib.utilities;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_interfaces.compatibility.CompatibilityFactory;

public class CompatibilityFactoryMock extends CoreHttpRequestMock {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    public CompatibilityFactory compatibilityFactory;

}
