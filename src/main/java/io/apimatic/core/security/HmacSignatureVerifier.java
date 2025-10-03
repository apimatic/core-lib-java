package io.apimatic.core.security;

import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.security.SignatureVerifier;
import io.apimatic.coreinterfaces.security.VerificationResult;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * HMAC-based signature verifier for HTTP requests.
 * <p>
 * Supports signature templates such as:
 * <ul>
 *   <li>{@code Sha256={digest}}</li>
 *   <li>{@code Sha256={digest}=abc}</li>
 *   <li>{@code signature="{digest}"; ts=1690000000}</li>
 * </ul>
 * The template is matched inside the header value (noise tolerated before/after).
 */
public class HmacSignatureVerifier implements SignatureVerifier {
    private static final String SIGNATURE_VALUE_PLACEHOLDER = "{digest}";

    /** Name of the header carrying the provided signature (lookup is case-insensitive). */
    private final String signatureHeaderName;

    /** HMAC algorithm used for signature generation (default: HmacSHA256). */
    private final String algorithm;

    /** Initialized key spec; used to create a new Mac per verification call. */
    private final SecretKeySpec keySpec;

    /** Template containing "{digest}". */
    private final String signatureValueTemplate;

    /** Resolves the bytes to sign from the request. */
    private final Function<Request, byte[]> requestBytesResolver;

    /** Codec used to decode (and possibly encode) digest text ↔ bytes (e.g., hex/base64). */
    private final DigestCodec digestCodec;

    /**
     * Initializes a new instance of the HmacSignatureVerifier class.
     *
     * @param secretKey Secret key for HMAC computation.
     * @param signatureHeaderName Name of the header containing the signature.
     * @param digestCodec Encoding type for the signature.
     * @param requestBytesResolver Optional custom resolver for extracting data to sign.
     * @param algorithm Algorithm (default HmacSHA256).
     * @param signatureValueTemplate Template for signature format.
     */
    public HmacSignatureVerifier(
            final String secretKey,
            final String signatureHeaderName,
            final DigestCodec digestCodec,
            final Function<Request, byte[]> requestBytesResolver,
            final String algorithm,
            final String signatureValueTemplate
    ) {

        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or Empty.");
        }
        if (signatureHeaderName == null || signatureHeaderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Signature header cannot be null or Empty.");
        }
        if (signatureValueTemplate == null || signatureValueTemplate.trim().isEmpty()) {
            throw new IllegalArgumentException("Signature value template cannot be null or Empty.");
        }
        if (requestBytesResolver == null) {
            throw new IllegalArgumentException(
                    "Request signature template resolver function cannot be null.");
        }
        if (digestCodec == null) {
            throw new IllegalArgumentException("Digest encoding cannot be null.");
        }
        if (algorithm == null || algorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("Algorithm cannot be null or Empty.");
        }

        this.signatureHeaderName = signatureHeaderName;
        this.algorithm = algorithm;
        this.keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm);
        this.signatureValueTemplate = signatureValueTemplate;
        this.requestBytesResolver = requestBytesResolver;
        this.digestCodec = digestCodec;
    }

    /**
     * Verifies the HMAC signature of the specified HTTP request.
     *
     * @param request The HTTP request to verify.
     * @return A CompletableFuture containing the verification result.
     */
    @Override
    public CompletableFuture<VerificationResult> verifyAsync(final Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String headerValue = request.getHeaders().asSimpleMap().entrySet().stream()
                        .filter(e -> e.getKey() != null
                                && e.getKey().equalsIgnoreCase(signatureHeaderName))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(null);

                if (headerValue == null) {
                    return VerificationResult.failure(
                            "Signature header '" + signatureHeaderName + "' is missing.");
                }

                byte[] provided = extractSignature(headerValue);
                if (provided == null || provided.length == 0) {
                    return VerificationResult.failure(
                            "Malformed signature header '" + signatureHeaderName + "'.");
                }

                byte[] message = requestBytesResolver.apply(request);
                // HMAC per call (thread-safe)
                Mac mac = Mac.getInstance(algorithm);
                mac.init(keySpec);
                byte[] computed = mac.doFinal(message);

                return MessageDigest.isEqual(provided, computed)
                        ? VerificationResult.success()
                        : VerificationResult.failure("Signature verification failed.");
            } catch (Exception ex) {
                return VerificationResult.failure("Exception: " + ex.getMessage());
            }
        });
    }

    /**
     * Extracts the digest value from the signature header according to the template
     * and decodes the signature from the header value.
     *
     * @param headerValue The value of the signature header.
     * @return The decoded signature as a byte array, or null if extraction fails.
     */
    private byte[] extractSignature(final String headerValue) {
        try {
            int index = signatureValueTemplate.indexOf(SIGNATURE_VALUE_PLACEHOLDER);
            if (index < 0) {
                return new byte[0];
            }

            String prefix = signatureValueTemplate.substring(0, index);
            String suffix = signatureValueTemplate.substring(
                    index + SIGNATURE_VALUE_PLACEHOLDER.length());

            // find prefix anywhere (case-insensitive)
            int prefixAt = indexOfIgnoreCase(headerValue, prefix, 0);
            if (prefixAt < 0) {
                return new byte[0];
            }

            int digestStart = prefixAt + prefix.length();

            // find suffix after the digest start (case-insensitive)
            final int digestEnd;
            if (suffix.isEmpty()) {
                digestEnd = headerValue.length();
            } else {
                digestEnd = indexOfIgnoreCase(headerValue, suffix, digestStart);
                if (digestEnd < 0) {
                    return new byte[0];
                }
            }

            if (digestEnd < digestStart) {
                return new byte[0];
            }

            String digest = headerValue.substring(digestStart, digestEnd).trim();
            // strip optional quotes
            if (digest.length() >= 2 && digest.charAt(0) == '"'
                    && digest.charAt(digest.length() - 1) == '"') {
                digest = digest.substring(1, digest.length() - 1);
            }

            byte[] decoded = digestCodec.decode(digest);
            return (decoded == null || decoded.length == 0) ? null : decoded;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Finds the index of the first case-insensitive {@code needle} in {@code haystack}
     * starting from {@code fromIndex}, or -1 if not found.
     *
     * @param haystack The string to search in.
     * @param needle The substring to search for.
     * @param fromIndex The index to start searching from.
     * @return The index of the first occurrence, or -1 if not found.
     */
    private static int indexOfIgnoreCase(
            final String haystack,
            final String needle,
            final int fromIndex
    ) {
        if (needle.isEmpty()) {
            return fromIndex;
        }
        int max = haystack.length() - needle.length();
        for (int i = Math.max(0, fromIndex); i <= max; i++) {
            if (haystack.regionMatches(true, i, needle, 0, needle.length())) {
                return i;
            }
        }
        return -1;
    }
}
