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

    private static final String SECRET_KEY = "testSecret";
    private static final String SIGNATURE_HEADER = "X-Signature";
    private static final String BODY = "payload";

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

    private String computeSignature(String key, String data, String algorithm, DigestCodec codec) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance(algorithm);
        mac.init(new javax.crypto.spec.SecretKeySpec(key.getBytes(java.nio.charset.StandardCharsets.UTF_8), algorithm));
        byte[] digest = mac.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return codec.encode(digest);
    }

    // Creational tests

    @Test
    public void testNullSecretKey() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Secret key cannot be null or Empty.");
        new HmacSignatureVerifier(null, SIGNATURE_HEADER);
    }

    @Test
    public void testEmptySecretKey() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Secret key cannot be null or Empty.");
        new HmacSignatureVerifier("   ", SIGNATURE_HEADER);
    }

    @Test
    public void testNullSignatureHeader() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature header cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, null);
    }

    @Test
    public void testEmptySignatureHeader() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature header cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, "   ");
    }

    @Test
    public void testNullDigestCodec() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Digest encoding cannot be null.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, null);
    }

    @Test
    public void testNullRequestSignatureTemplateResolver() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Request signature template resolver function cannot be null.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec, null, HmacSignatureVerifier.DEFAULT_ALGORITHM, HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE);
    }

    @Test
    public void testNullAlgorithm() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Algorithm cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec, HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, null, HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE);
    }

    @Test
    public void testEmptyAlgorithm() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Algorithm cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec, HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, "   ", HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE);
    }

    @Test
    public void testNullSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature value template cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec, HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, null);
    }

    @Test
    public void testEmptySignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Signature value template cannot be null or Empty.");
        new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec, HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "   ");
    }

    // Behaviour tests

    @Test
    public void testVerifyAsync_Success() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);

        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec);

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testVerifyAsync_MissingHeader() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec);

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Signature header '" + SIGNATURE_HEADER + "' is missing.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_MalformedSignature() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "not-a-hex-signature");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec);

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Malformed signature header '" + SIGNATURE_HEADER + "' value.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_WrongSignature() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "deadbeef");
        Request request = mockRequest(headers, BODY);
        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec);

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Signature verification failed.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_ExceptionInResolver() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);

        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY,
                SIGNATURE_HEADER,
                codec,
                req -> { throw new RuntimeException("Request signature template resolver error"); },
                HmacSignatureVerifier.DEFAULT_ALGORITHM,
                HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE
        );

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Exception: Request signature template resolver error", result.getErrors().get(0));
    }

    // DigestCodec tests

    @Test
    public void testVerifyAsync_WrongSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "{diet}");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Malformed signature header '" + SIGNATURE_HEADER + "' value.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_SuccessPrefixSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "sha256=" + signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "sha256={digest}");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testVerifyAsync_WrongPrefixSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "sha256={digest}");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Malformed signature header '" + SIGNATURE_HEADER + "' value.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_SuccessSuffixSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature + "complex");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "{digest}complex");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testVerifyAsync_WrongSuffixSignatureValueTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        String signature = computeSignature(SECRET_KEY, BODY, HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, signature);
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "{digest}complex");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Malformed signature header '" + SIGNATURE_HEADER + "' value.", result.getErrors().get(0));
    }

    @Test
    public void testVerifyAsync_WrongSignatureValueForTemplate() throws Exception {
        DigestCodec codec = DigestCodecFactory.hex();
        Map<String, String> headers = new HashMap<>();
        headers.put(SIGNATURE_HEADER, "v0=complex");
        Request request = mockRequest(headers, BODY);

        HmacSignatureVerifier verifier = new HmacSignatureVerifier(SECRET_KEY, SIGNATURE_HEADER, codec,
                HmacSignatureVerifier.SIGNATURE_TEMPLATE_RESOLVER, HmacSignatureVerifier.DEFAULT_ALGORITHM, "v0={digest}complex");

        VerificationResult result = verifier.verifyAsync(request).get();
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("Malformed signature header '" + SIGNATURE_HEADER + "' value.", result.getErrors().get(0));
    }

    // Request signature template resolver tests

    @Test
    public void testVerifyAsync_SuccessRequestSignatureTemplateResolver_Body() throws Exception {
        DigestCodec codec = DigestCodecFactory.base64();
        Map<String, String> headers = new HashMap<>();
        String signature = computeSignature(SECRET_KEY,
                "session=abc123:2025-09-17T12:34:56Z:POST:payment:{\"id\":123,\"type\":\"payment\",\"amount\":100.5}",
                HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        headers.put(SIGNATURE_HEADER, signature);
        headers.put("Cookie", "session=abc123");
        headers.put("X-Timestamp", "2025-09-17T12:34:56Z");
        Request req = mockRequest(headers, "{\"id\":123,\"type\":\"payment\",\"amount\":100.5}");

        Function<Request, byte[]> requestSignatureTemplateResolver =
                (request) -> {
                    if (request == null) {
                        throw new IllegalArgumentException("request cannot be null");
                    }

                    // {request.Headers.Cookie}:{request.Headers.X-Timestamp}:{request.Method}:{$request.body#/type}:{$request.body}
                    String cookie = request.getHeaders().asSimpleMap().get("Cookie");
                    String timestamp = request.getHeaders().asSimpleMap().get("X-Timestamp");
                    String resolvedBody = CoreHelper.resolveRequestPointer("$request.body#/type", request);

                    return String.join(":",
                            cookie,
                            timestamp,
                            request.getHttpMethod().toString(),
                            resolvedBody == null ? "" : resolvedBody,
                            request.getBody().toString()
                    ).getBytes(StandardCharsets.UTF_8);
                };


        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY,
                SIGNATURE_HEADER,
                codec,
                requestSignatureTemplateResolver,
                HmacSignatureVerifier.DEFAULT_ALGORITHM,
                HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE);

        VerificationResult result = verifier.verifyAsync(req).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testVerifyAsync_SuccessRequestSignatureTemplateResolver_Headers() throws Exception {
        DigestCodec codec = DigestCodecFactory.base64();
        Map<String, String> headers = new HashMap<>();
        String signature = computeSignature(SECRET_KEY,
                "session=abc123:2025-09-17T12:34:56Z:POST:x-signature-header-value:{\"id\":123,\"type\":\"payment\",\"amount\":100.5}",
                HmacSignatureVerifier.DEFAULT_ALGORITHM, codec);
        headers.put(SIGNATURE_HEADER, signature);
        headers.put("Cookie", "session=abc123");
        headers.put("x-signature", "x-signature-header-value");
        headers.put("X-Timestamp", "2025-09-17T12:34:56Z");
        Request req = mockRequest(headers, "{\"id\":123,\"type\":\"payment\",\"amount\":100.5}");

        Function<Request, byte[]> requestSignatureTemplateResolver =
                (request) -> {
                    if (request == null) {
                        throw new IllegalArgumentException("request cannot be null");
                    }

                    // {request.Headers.Cookie}:{request.Headers.X-Timestamp}:{request.Method}:{$request.headers#/x-signature}:{$request.body}
                    String cookie = request.getHeaders().asSimpleMap().get("Cookie");
                    String timestamp = request.getHeaders().asSimpleMap().get("X-Timestamp");
                    String resolvedBody = CoreHelper.resolveRequestPointer("$request.headers#/x-signature", request);

                    return String.join(":",
                            cookie,
                            timestamp,
                            request.getHttpMethod().toString(),
                            resolvedBody == null ? "" : resolvedBody,
                            request.getBody().toString()
                    ).getBytes(StandardCharsets.UTF_8);
                };


        HmacSignatureVerifier verifier = new HmacSignatureVerifier(
                SECRET_KEY,
                SIGNATURE_HEADER,
                codec,
                requestSignatureTemplateResolver,
                HmacSignatureVerifier.DEFAULT_ALGORITHM,
                HmacSignatureVerifier.SIGNATURE_VALUE_TEMPLATE);

        VerificationResult result = verifier.verifyAsync(req).get();
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testResolveRequestPointer_NullPointer() {
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        String value = CoreHelper.resolveRequestPointer(null, request);
        assertNull(value);
    }

    @Test
    public void testResolveRequestPointer_InvalidPointer() {
        Map<String, String> headers = new HashMap<>();
        Request request = mockRequest(headers, BODY);
        String value = CoreHelper.resolveRequestPointer("$request.method#", request);
        assertNull(value);
    }
}


