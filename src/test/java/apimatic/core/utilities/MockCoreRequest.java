package apimatic.core.utilities;

import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class MockCoreRequest extends MockCoreConfig {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    @Before
    public void setup() throws IOException {}


}
