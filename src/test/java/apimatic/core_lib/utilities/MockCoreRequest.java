package apimatic.core_lib.utilities;

import java.io.IOException;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import io.apimatic.core_lib.HttpRequest;
import io.apimatic.core_lib.Parameter;

public class MockCoreRequest extends MockCoreConfig {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    
    @Mock
    HttpRequest.Builder coreRequestBuilder;
    
    @Captor
    ArgumentCaptor<Consumer<Parameter.Builder>> argument;
    
    @Before
    public void setup() throws IOException {
    }
    
 
}
