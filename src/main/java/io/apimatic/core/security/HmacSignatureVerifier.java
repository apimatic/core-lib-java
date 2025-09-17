package io.apimatic.core.security;

import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.security.SignatureVerifier;
import io.apimatic.coreinterfaces.security.VerificationResult;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * HMAC-based signature verifier for HTTP requests.
 */
public class HmacSignatureVerifier implements SignatureVerifier {

    public static final String DEFAULT_ALGORITHM = "HmacSHA256";
    public static final String SIGNATURE_VALUE_TEMPLATE = "{digest}";
    public static final Function<Request, byte[]> SIGNATURE_TEMPLATE_RESOLVER =
            req -> req.getBody().toString().getBytes(StandardCharsets.UTF_8);

    /** Name of the header carrying the provided signature (case-insensitive lookup). */
    private final String signatureHeader;

    /** HMAC algorithm used for signature generation (default: HmacSHA256). */
    private final Mac signatureAlgorithm;

    /** Optional template for the expected signature value. */
    private final String signatureValueTemplate;

    /** Resolves the request data into a byte array for signature computation. */
    private final Function<Request, byte[]> requestSignatureTemplateResolver;

    /** Codec used for encoding and decoding digests. */
    private final DigestCodec digestCodec;


    /**
     * Initializes a new instance of the HmacSignatureVerifier class using HEX Encoding as default.
     *
     * @param secretKey Secret key for HMAC computation.
     * @param signatureHeader Name of the header containing the signature.
     */
    public HmacSignatureVerifier(
            String secretKey,
            String signatureHeader) throws Exception {
        this(
                secretKey,
                signatureHeader,
                DigestCodecFactory.hex()
        );
    }


    /**
     * Initializes a new instance of the HmacSignatureVerifier class.
     *
     * @param secretKey Secret key for HMAC computation.
     * @param signatureHeader Name of the header containing the signature.
     * @param digestCodec Encoding type for the signature.
     */
    public HmacSignatureVerifier(
            String secretKey,
            String signatureHeader,
            DigestCodec digestCodec) throws Exception {
        this(
            secretKey,
            signatureHeader,
            digestCodec,
            SIGNATURE_TEMPLATE_RESOLVER,
            DEFAULT_ALGORITHM,
            SIGNATURE_VALUE_TEMPLATE
        );
    }

    /**
     * Initializes a new instance of the HmacSignatureVerifier class.
     *
     * @param secretKey Secret key for HMAC computation.
     * @param signatureHeader Name of the header containing the signature.
     * @param digestCodec Encoding type for the signature.
     * @param requestSignatureTemplateResolver Optional custom resolver for extracting data to sign.
     * @param algorithm Algorithm (default HmacSHA256).
     * @param signatureValueTemplate Template for signature format.
     */
    public HmacSignatureVerifier(
            String secretKey,
            String signatureHeader,
            DigestCodec digestCodec,
            Function<Request, byte[]> requestSignatureTemplateResolver,
            String algorithm,
            String signatureValueTemplate) throws Exception {

        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or Empty.");
        }

        if (signatureHeader == null || signatureHeader.trim().isEmpty()) {
            throw new IllegalArgumentException("Signature header cannot be null or Empty.");
        }
        this.signatureHeader = signatureHeader;

        if (signatureValueTemplate == null || signatureValueTemplate.trim().isEmpty()) {
            throw new IllegalArgumentException("Signature value template cannot be null or Empty.");
        }
        this.signatureValueTemplate = signatureValueTemplate;

        if (requestSignatureTemplateResolver == null) {
            throw new IllegalArgumentException("Request signature template resolver function cannot be null.");
        }
        this.requestSignatureTemplateResolver = requestSignatureTemplateResolver;

        if (digestCodec == null) {
            throw new IllegalArgumentException("Digest encoding cannot be null.");
        }
        this.digestCodec = digestCodec;

        if (algorithm == null || algorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("Algorithm cannot be null or Empty.");
        }
        this.signatureAlgorithm = Mac.getInstance(algorithm);
        this.signatureAlgorithm.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm));
    }

    /**
     * Verifies the HMAC signature of the specified HTTP request.
     */
    @Override
    public CompletableFuture<VerificationResult> verifyAsync(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String signatureValue = request.getHeaders()
                        .asSimpleMap()
                        .get(signatureHeader);

                if (signatureValue == null) {
                    return VerificationResult.failure("Signature header '" + signatureHeader + "' is missing.");
                }

                byte[] providedSignature = extractSignature(signatureValue);
                if (providedSignature == null) {
                    return VerificationResult.failure("Malformed signature header '" + signatureHeader + "' value.");
                }

                byte[] resolvedTemplateBytes = requestSignatureTemplateResolver.apply(request);
                byte[] computedHash = signatureAlgorithm.doFinal(resolvedTemplateBytes);

                return Arrays.equals(providedSignature, computedHash)
                        ? VerificationResult.success()
                        : VerificationResult.failure("Signature verification failed.");
            } catch (Exception ex) {
                return VerificationResult.failure("Exception: " + ex.getMessage());
            }
        });
    }

    /**
     * Extracts the digest value from the signature header according to the template.
     * And decode the signature from the header value.
     */
    private byte[] extractSignature(String signatureValue) {
        try {
            int index = signatureValueTemplate.indexOf(SIGNATURE_VALUE_TEMPLATE);
            if (index < 0) return null;

            String prefix = signatureValueTemplate.substring(0, index);
            String suffix = signatureValueTemplate.substring(index + 8);

            if (!signatureValue.startsWith(prefix) || !signatureValue.endsWith(suffix)) return null;

            String digest = signatureValue.substring(prefix.length(), signatureValue.length() - suffix.length());
            if (digest.isEmpty()) return null;

            return digestCodec.decode(digest);
        } catch (Exception e) {
            return null;
        }
    }
}
