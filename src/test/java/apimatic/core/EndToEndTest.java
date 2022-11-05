package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import apimatic.core.mocks.MockCoreConfig;
import io.apimatic.core.ApiCall;
import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.Callback;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.HttpClient;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;
import io.apimatic.coreinterfaces.http.response.Response;

public class EndToEndTest extends MockCoreConfig {

    /**
     * Success code status code.
     */
    private static final int SUCCESS_CODE = 200;

    /**
     * Initializes mocks annotated with Mock.
     */
    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();

    /**
     * Mock of {@link Callback}.
     */
    @Mock
    private Callback callback;

    /**
     * Mock of {@link Context}.
     */
    @Mock
    private Context context;

    /**
     * Mock of {@link HttpClient}.
     */
    @Mock
    private HttpClient httpClient;

    /**
     * Mock of {@link Response}.
     */
    @Mock
    private Response response;


    /**
     * Mock of {@link Request}.
     */
    @Mock
    private Request coreHttpRequest;

    /**
     * Mock of {@link CoreEndpointConfiguration}.
     */
    @Mock
    private CoreEndpointConfiguration endpointConfiguration;

    /**
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Before
    public void setup() throws IOException {
        prepareStub();
    }

    /**
     * End to End sync call.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws CoreApiException Exception in api call execution.
     */
    @Test
    public void testEndToEndSyncCall() throws IOException, CoreApiException {
        String expected = "Turtle";
        String actual = getApiCall().execute();
        assertEquals(actual, expected);
    }

    private ApiCall<String, CoreApiException> getApiCall() throws IOException {
        when(response.getBody()).thenReturn("\"Turtle\"");
        return new ApiCall.Builder<String, CoreApiException>().globalConfig(getGlobalConfig())
                .requestBuilder(requestBuilder -> requestBuilder.server("https://localhost:3000")
                        .path("/v2/bank-accounts")
                        .queryParam(param -> param.key("cursor").value("cursor").isRequired(false))
                        .formParam(param -> param.key("limit").value("limit").isRequired(false))
                        .templateParam(param -> param.key("location_id").value("locationId")
                                .shouldEncode(true).isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(Collections.emptyMap()))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                                .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();

    }

    private GlobalConfiguration getGlobalConfig() {
        String userAgent = "APIMATIC 3.0";
        GlobalConfiguration globalConfig =
                new GlobalConfiguration.Builder().authentication(Collections.emptyMap())
                        .compatibilityFactory(getCompatibilityFactory()).httpClient(httpClient)
                        .baseUri(server -> getBaseUri(server)).callback(callback)
                        .userAgent(userAgent).userAgentConfig(Collections.emptyMap())
                        .additionalHeaders(null).globalHeader("version", "0.1")
                        .globalHeader("version", "1.2").build();
        return globalConfig;
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }

    private void prepareStub() throws IOException {
        when(httpClient.execute(any(Request.class), any(CoreEndpointConfiguration.class)))
                .thenReturn(response);
        when(getCompatibilityFactory().createHttpHeaders(anyMap())).thenReturn(getHttpHeaders());
        when(getCompatibilityFactory().createHttpRequest(any(Method.class),
                any(StringBuilder.class), any(HttpHeaders.class), anyMap(), anyList()))
                        .thenReturn(coreHttpRequest);
        when(getCompatibilityFactory().createHttpContext(coreHttpRequest, response))
                .thenReturn(context);
        when(context.getResponse()).thenReturn(response);
        when(response.getStatusCode()).thenReturn(SUCCESS_CODE);
    }
}
