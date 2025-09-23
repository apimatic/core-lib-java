package apimatic.core.security;

import io.apimatic.core.security.DigestCodec;
import io.apimatic.core.security.DigestCodecFactory;
import io.apimatic.core.security.HmacSignatureVerifier;
import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.HttpHeaders;
import io.apimatic.coreinterfaces.http.Method;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.security.VerificationResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class HmacSignatureVerifierTest {

    /**
     * Secret key used for HMAC tests.
     */
    private static final String SECRET_KEY = "testSecret";
    /**
     * Header name for signature.
     */
    private static final String SIGNATURE_HEADER = "X-Signature";
    /**
     * Request body for tests.
     */
    private static final String BODY = "payload";
    /**
     * Default digest codec for tests.
     */
    public static final DigestCodec DEFAULT_DIGEST_CODEC = DigestCodecFactory.hex();
    /**
     * Default HMAC algorithm for tests.
     */
    public static final String DEFAULT_ALGORITHM = "HmacSHA256";
    /**
     * Default signature value template for tests.
     */
    public static final String SIGNATURE_VALUE_TEMPLATE = "{digest}";
    /**
     * Default resolver for signature template.
     */
    public static final Function<Request, byte[]> SIGNATURE_TEMPLATE_RESOLVER =
            req -> req.getBody().toString().getBytes(StandardCharsets.UTF_8);

    /**
     * Rule for expecting exceptions in tests.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Request mockRequest(Map<String, String> headers, String body) {
        Request request = Mockito.mock(Request.class);
        HttpHeaders httpHeaders = Mockito.mock(HttpHeaders.class);
        Mockito.when(httpHeaders.asSimpleMap()).thenReturn(headers);
        Mockito.when(request.getHeaders()).thenReturn(httpHeaders);
        Mockito.when(request.getHttpMethod()).thenReturn(Method.POST);
        Mockito.when(request.getBody()).thenReturn(body);
        return request;
    }

    private String computeSignature(
            String key, String data, String algorithm, DigestCodec codec
    ) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance(algorithm);
        mac.init(new javax.crypto.spec.SecretKeySpec(
                key.getBytes(java.nio.charset.StandardCharsets.UTF_8), algorithm));
        byte[] digest = mac.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return codec.encode(digest);
    }

// Creational tests

    @Test
    public void testNullSecretKey() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Secret key cannot be null or Empty.");
        new HmacSignatureVerifier(
                null, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testEmptySecretKey() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Secret key cannot be null or Empty.");
        new HmacSignatureVerifier(
                "   ", SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testNullSignatureHeader() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature header cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, null, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testEmptySignatureHeader() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature header cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, "   ", DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testNullDigestCodec() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Digest encoding cannot be null.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, null, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testNullRequestSignatureTemplateResolver() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Request signature template resolver function cannot be null.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, null,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testNullAlgorithm() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Algorithm cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                null, SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testEmptyAlgorithm() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Algorithm cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                "   ", SIGNATURE_VALUE_TEMPLATE
        );
    }

    @Test
    public void testNullSignatureValueTemplate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature value template cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, null
        );
    }

    @Test
    public void testEmptySignatureValueTemplate() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature value template cannot be null or Empty.");
        new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, "   "
        );
    }

    // Behaviour tests

    @Test
    public void verifyAsyncSuccess() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void verifyAsyncMissingHeader() throws Exception {
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Signature header '" + SIGNATURE_HEADER + "' is missing.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncMalformedSignature() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "not-a-hex-signature");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Malformed signature header '" + SIGNATURE_HEADER + "'.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncWrongSignature() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "deadbeef");
        Request request = mockRequest(headers, BODY);
        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC, SIGNATURE_TEMPLATE_RESOLVER,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Signature verification failed.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncExceptionInResolver() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                req -> {
                    throw new RuntimeException("Request signature template resolver error");
                },
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Exception: Request signature template resolver error",
                result.getErrors().get(0)
        );
    }

    // DigestCodec tests

    @Test
    public void verifyAsyncWrongSignatureValueTemplate() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "{diet}"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Malformed signature header '" + SIGNATURE_HEADER + "'.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncSuccessPrefixSignatureValueTemplate() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "sha256=" + signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "sha256={digest}"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void verifyAsyncWrongPrefixSignatureValueTemplate() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "sha256={digest}"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Malformed signature header '" + SIGNATURE_HEADER + "'.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncSuccessSuffixSignatureValueTemplate() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature + "complex");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "{digest}complex"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void verifyAsyncWrongSuffixSignatureValueTemplate() throws Exception {
        String signature = computeSignature(
                SECRET_KEY, BODY, DEFAULT_ALGORITHM, DEFAULT_DIGEST_CODEC
        );
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "{digest}complex"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Malformed signature header '" + SIGNATURE_HEADER + "'.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void verifyAsyncWrongSignatureValueForTemplate() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "v0=complex");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, DEFAULT_DIGEST_CODEC,
                SIGNATURE_TEMPLATE_RESOLVER, DEFAULT_ALGORITHM, "v0={digest}complex"
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(
                "Malformed signature header '" + SIGNATURE_HEADER + "'.",
                result.getErrors().get(0)
        );
    }

    // Request signature template resolver tests

    @Test
    public void verifyAsyncSuccessRequestSignatureTemplateResolverBody() throws Exception {
        DigestCodec codec = DigestCodecFactory.base64();
        Map<String, String> headers = new HashMap<>();
        String signature = computeSignature(
                SECRET_KEY,
                "session=abc123:2025-09-17T12:34:56Z:POST:payment"
                        + ":{\"id\":123,\"type\":\"payment\",\"amount\":100.5}",
                DEFAULT_ALGORITHM, codec
        );
        headers.put(SIGNATURE_HEADER, signature);
        headers.put("Cookie", "session=abc123");
        headers.put("X-Timestamp", "2025-09-17T12:34:56Z");
        Request req = mockRequest(headers, "{\"id\":123,\"type\":\"payment\",\"amount\":100.5}");

        Function<Request, byte[]> requestSignatureTemplateResolver = (request) -> {
            if (request == null) {
                throw new IllegalArgumentException("request cannot be null");
            }
            String cookie = request.getHeaders().asSimpleMap().get("Cookie");
            String timestamp = request.getHeaders().asSimpleMap().get("X-Timestamp");
            String resolvedBody = CoreHelper.resolveRequestPointer("$request.body#/type", request);

            return String.join(
                    ":",
                    cookie,
                    timestamp,
                    request.getHttpMethod().toString(),
                    resolvedBody == null ? "" : resolvedBody,
                    request.getBody().toString()
            ).getBytes(StandardCharsets.UTF_8);
        };

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, codec, requestSignatureTemplateResolver,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(req).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void verifyAsyncSuccessRequestSignatureTemplateResolverHeaders() throws Exception {
        DigestCodec codec = DigestCodecFactory.base64();
        Map<String, String> headers = new HashMap<>();
        String signature = computeSignature(
                SECRET_KEY,
                "session=abc123:2025-09-17T12:34:56Z:POST:x-signature-header-value"
                        + ":{\"id\":123,\"type\":\"payment\",\"amount\":100.5}",
                DEFAULT_ALGORITHM, codec
        );
        headers.put(SIGNATURE_HEADER, signature);
        headers.put("Cookie", "session=abc123");
        headers.put("x-signature", "x-signature-header-value");
        headers.put("X-Timestamp", "2025-09-17T12:34:56Z");
        Request req = mockRequest(headers, "{\"id\":123,\"type\":\"payment\",\"amount\":100.5}");

        Function<Request, byte[]> requestSignatureTemplateResolver = (request) -> {
            if (request == null) {
                throw new IllegalArgumentException("request cannot be null");
            }
            String cookie = request.getHeaders().asSimpleMap().get("Cookie");
            String timestamp = request.getHeaders().asSimpleMap().get("X-Timestamp");
            String resolvedBody = CoreHelper.resolveRequestPointer(
                    "$request.headers#/x-signature", request);

            return String.join(
                    ":",
                    cookie,
                    timestamp,
                    request.getHttpMethod().toString(),
                    resolvedBody == null ? "" : resolvedBody,
                    request.getBody().toString()
            ).getBytes(StandardCharsets.UTF_8);
        };

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY, SIGNATURE_HEADER, codec, requestSignatureTemplateResolver,
                DEFAULT_ALGORITHM, SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(req).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void resolveRequestPointerNullPointer() {
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        String value = CoreHelper.resolveRequestPointer(null, request);
        assertNull(value);
    }

    @Test
    public void resolveRequestPointerInvalidPointer() {
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        String value = CoreHelper.resolveRequestPointer("$request.method#", request);
        assertNull(value);
    }
}
