package apimatic.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import apimatic.core_lib.utilities.MockCoreRequest;
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

public class EndToEndTest extends MockCoreRequest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule().silent();
    @Mock
    private Callback callback;

    @Mock
    private Context context;

    @Mock
    private HttpClient httpClient;

    @Mock
    private Response response;

    @Mock
    private CompletableFuture<Response> completableResponse;

    @Mock
    private Request coreHttpRequest;

    @Mock
    private CoreEndpointConfiguration endpointConfiguration;

    @Before
    public void setup() throws IOException {
        prepareStub();
    }

    @Test
    public void EndToEndTestSync() throws IOException, CoreApiException {
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
                        .queryParam(param -> param.key("location_id").value("locationId")
                                .isRequired(false))
                        .headerParam(param -> param.key("accept").value("application/json"))
                        .authenticationKey("global").httpMethod(Method.GET))
                .responseHandler(responseHandler -> responseHandler
                        .deserializer(response -> CoreHelper.deserialize(response, String.class))
                        .nullify404(false).globalErrorCase(Collections.EMPTY_MAP))
                .endpointConfiguration(
                        param -> param.arraySerializationFormat(ArraySerializationFormat.INDEXED)
                        .hasBinaryResponse(false).retryOption(RetryOption.DEFAULT))
                .build();

    }

    private GlobalConfiguration getGlobalConfig() {
        String userAgent = "APIMATIC 3.0";
        GlobalConfiguration globalConfig = new GlobalConfiguration.Builder()
                .authentication(Collections.EMPTY_MAP).compatibilityFactory(compatibilityFactory)
                .httpClient(httpClient).baseUri(server -> getBaseUri(server)).callback(callback)
                .userAgent(userAgent).userAgentConfig(Collections.EMPTY_MAP).additionalHeaders(null)
                .globalHeader("Square-Version", "square version").build();
        return globalConfig;
    }

    protected static String getBaseUri(String test) {
        return "http://localhost:3000";
    }

    private void prepareStub() throws IOException {
        when(httpClient.execute(any(Request.class), any(CoreEndpointConfiguration.class)))
                .thenReturn(response);
        when(httpClient.executeAsync(any(Request.class), any(CoreEndpointConfiguration.class)))
                .thenReturn(completableResponse);
        when(compatibilityFactory.createHttpHeaders(anyMap())).thenReturn(httpHeaders);
        when(compatibilityFactory.createHttpRequest(any(Method.class), any(StringBuilder.class),
                any(HttpHeaders.class), anyMap(), anyList())).thenReturn(coreHttpRequest);
        when(compatibilityFactory.createHttpContext(coreHttpRequest, response)).thenReturn(context);
        when(context.getResponse()).thenReturn(response);
        when(response.getStatusCode()).thenReturn(200);
    }
}
