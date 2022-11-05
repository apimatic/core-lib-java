package io.apimatic.core.utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.apimatic.core.annotations.TypeCombinator.FormSerialize;
import io.apimatic.core.annotations.TypeCombinator.TypeCombinatorCase;
import io.apimatic.core.annotations.TypeCombinator.TypeCombinatorStringCase;
import io.apimatic.core.types.http.request.MultipartFileWrapper;
import io.apimatic.core.types.http.request.MultipartWrapper;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;

/**
 * This is a Helper class with commonly used utilities for the SDK.
 */
public class CoreHelper {

    /**
     * A string of user agent.
     */
    private static String userAgent;

    /**
     * A tab separated array serialization format.
     */
    private static final String TSV_FORMAT = "%09";

    /**
     * A comma separated array serialization format.
     */
    private static final String CSV_FORMAT = ",";

    /**
     * A pipe separated array serialization format
     */
    private static final String PSV_FORMAT = "%7C";

    /**
     * Deserialization of Json data.
     */
    private static ObjectMapper mapper =
            JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .withConfigOverride(BigDecimal.class,
                            mutableConfigOverride -> mutableConfigOverride
                                    .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING)))
                    .build();

    /**
     * Strict Deserialization of Json data.
     */
    private static ObjectMapper strictMapper =
            JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
                    .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
                    .addModule(new SimpleModule().addDeserializer(String.class,
                            new CoercionLessStringDeserializer()))
                    .withConfigOverride(BigDecimal.class,
                            mutableConfigOverride -> mutableConfigOverride
                                    .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING)))
                    .build();

    protected CoreHelper() {}


    /**
     * Get a JsonSerializer instance for a collection from the provided annotation.
     * @param serializerAnnotation The Annotation containing information about the custom serializer
     *        of a collection.
     * @return The JsonSerializer instance of the required type.
     */
    private static JsonSerializer<?> getCollectionCustomSerializer(
            FormSerialize serializerAnnotation) {
        try {
            return serializerAnnotation.contentUsing().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * List of classes that are wrapped directly. This information is needed when traversing object
     * trees for reference matching.
     */
    private static final Set<Object> WRAPPER_TYPES =
            new HashSet<Object>(Arrays.asList(Boolean.class, Character.class, Byte.class,
                    Short.class, String.class, Integer.class, Long.class, Float.class, Double.class,
                    BigDecimal.class, Void.class, File.class, MultipartWrapper.class,
                    MultipartFileWrapper.class));

    /**
     * Get a JsonSerializer instance from the provided annotation.
     * @param serializerAnnotation The Annotation containing information about the serializer.
     * @return The JsonSerializer instance of the required type.
     */
    private static JsonSerializer<?> getSerializer(JsonSerialize serializerAnnotation) {
        try {
            return serializerAnnotation.using().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get a JsonSerializer instance for a collection from the provided annotation.
     * @param serializerAnnotation The Annotation containing information about the serializer of a
     *        collection.
     * @return The JsonSerializer instance of the required type.
     */
    private static JsonSerializer<?> getCollectionSerializer(JsonSerialize serializerAnnotation) {
        try {
            return serializerAnnotation.contentUsing().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Deserialization of Json data.
     * @return {@link ObjectMapper}.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }


    /**
     * Strict Deserialization of Json data.
     * @return {@link ObjectMapper}.
     */
    public static ObjectMapper getStrictMapper() {
        return strictMapper;
    }

    /**
     * Json Serialization of a given object.
     * @param obj The object to serialize into Json.
     * @return The serialized Json String representation of the given object.
     * @throws JsonProcessingException Signals that a Json Processing Exception has occurred.
     */
    public static String serialize(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }

        return mapper.writeValueAsString(obj);
    }

    /**
     * Json Serialization of a given object using a specified JsonSerializer.
     * @param obj The object to serialize into Json.
     * @param serializer The instance of JsonSerializer to use.
     * @return The serialized Json string representation of the given object.
     * @throws JsonProcessingException Signals that a Json Processing Exception has occurred.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String serialize(Object obj, final JsonSerializer serializer)
            throws JsonProcessingException {
        if (obj == null || serializer == null) {
            return null;
        }

        Class<? extends Object> cls = null;
        if (obj.getClass().getName().equals("java.util.ArrayList")) {
            // need to find the generic type if it's an ArrayList
            cls = ((ArrayList) obj).get(0).getClass();
        } else if (obj.getClass().getName().equals("java.util.LinkedHashMap")) {
            cls = ((LinkedHashMap) obj).values().toArray()[0].getClass();
        } else {
            cls = obj.getClass();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(cls, serializer);
        objectMapper.registerModule(module);

        return objectMapper.writeValueAsString(obj);
    }


    /**
     * Xml Serialization of a given object list.
     * @param <T> Type of object to be serialized.
     * @param objArray Object Array to be serialized.
     * @param rootName Root name for the xml.
     * @param nodeName Node name for the array nodes.
     * @param cls Class of object to be serialized.
     * @return The serialized Xml String representation of the given object array.
     * @throws IOException Signals that an IO exception occurred.
     */
    public static <T> String serializeXmlArray(
            T[] objArray, String rootName, String nodeName, Class<T> cls) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(cls);
            JAXBElement<T> jaxbElement;
            String xmlBlock = "<" + rootName + ">\n";
            for (T element : objArray) {
                jaxbElement = new JAXBElement<>(new QName(nodeName), cls, element);
                StringWriter writer = new StringWriter();
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                marshaller.marshal(jaxbElement, writer);
                xmlBlock += "  " + writer.toString() + "\n";
            }

            xmlBlock += "</" + rootName + ">";
            return xmlBlock;
        } catch (JAXBException jaxbException) {
            throw new IOException(jaxbException);
        }
    }

    /**
     * Xml Serialization of a given object.
     * @param <T> Type of object to be serialized.
     * @param obj Object to be serialized.
     * @param rootName Root name for the xml.
     * @param cls Class of object to be serialized.
     * @return The serialized Xml String representation of the given object.
     * @throws IOException Signals that an IOException exception occurred.
     */
    public static <T> String serializeXml(T obj, String rootName, Class<T> cls) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            JAXBElement<T> elem = new JAXBElement<>(new QName(rootName), cls, obj);

            StringWriter writer = new StringWriter();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(elem, writer);
            return writer.toString();
        } catch (JAXBException jaxbException) {
            throw new IOException(jaxbException);
        }
    }


    /**
     * Json Serialization of a given container object based on annotation.
     * @param obj The object to serialize into Json.
     * @return The serialized Json String representation of the given object.
     * @throws JsonProcessingException Signals that a Json Processing Exception has occurred.
     */
    public static String serializeTypeCombinator(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }

        Annotation stringCaseAnnotation =
                obj.getClass().getAnnotation(TypeCombinatorStringCase.class);

        if (stringCaseAnnotation != null) {
            return obj.toString();
        }

        return serialize(obj);
    }

    /**
     * Json deserialization of the given Json string using a specified JsonDerializer.
     * @param jsonNode The JsonNode to deserialize.
     * @param typeReference TypeReference of T1.
     * @param <T1> The type of the object to deserialize into.
     * @param <T2> The type of the custom deserializer.
     * @param cls The class to attach the deserializer to.
     * @param deserializer The deserializer to use.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T1 extends Object, T2 extends Object> T1 deserialize(
            JsonNode jsonNode, final TypeReference<T1> typeReference, final Class<T2> cls,
            final JsonDeserializer<T2> deserializer) throws IOException {
        if (jsonNode == null) {
            return null;
        }

        return deserialize(mapper.writeValueAsString(jsonNode), typeReference, cls, deserializer);
    }


    /**
     * Json deserialization of the given Json string using a specified JsonDerializer.
     * @param json The Json string to deserialize.
     * @param typeReference TypeReference of T1.
     * @param <T1> The type of the object to deserialize into.
     * @param <T2> The type of the custom deserializer.
     * @param cls The class to attach the deserializer to.
     * @param deserializer The deserializer to use.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T1 extends Object, T2 extends Object> T1 deserialize(
            String json, final TypeReference<T1> typeReference, final Class<T2> cls,
            final JsonDeserializer<T2> deserializer) throws IOException {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }

        return new ObjectMapper() {
            private static final long serialVersionUID = -1639089569991988232L;
            {
                SimpleModule module = new SimpleModule();
                module.addDeserializer(cls, deserializer);
                this.registerModule(module);
            }
        }.readValue(json, typeReference);
    }

    /**
     * Json deserialization of the given Json string.
     * @param <T> The type of the object to deserialize into.
     * @param json The Json string to deserialize.
     * @param clazz The type of the object to deserialize into.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> T deserialize(String json, Class<T> clazz) throws IOException {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }

        return mapper.readValue(json, clazz);
    }

    /**
     * Strict JSON deserialization of the given JSON string with FAIL_ON_UNKNOWN_PROPERTIES flag as
     * true, used particularly for type combinators.
     * @param <T> The type of the object to deserialize into
     * @param json The JsonNode to deserialize
     * @param classes The list of types of the object to deserialize into
     * @param isOneOf The boolean flag to validate for oneOf flow
     * @return The deserialized object
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T> T deserialize(
            JsonNode json, List<Class<? extends T>> classes, boolean isOneOf) throws IOException {
        if (json == null) {
            return null;
        }


        T deserializedObject = null;
        int deserializationCount = 0;
        for (Class<? extends T> clazz : classes) {
            try {
                if (isOneOf) {
                    deserializedObject = strictMapper.convertValue(json, clazz);
                    deserializationCount++;
                    if (deserializationCount > 1) {
                        throw new IOException(
                                "More than 1 matching one-of types found against given json");
                    }
                } else {
                    return strictMapper.convertValue(json, clazz);
                }
            } catch (IllegalArgumentException e) {
                // Ignoring the exception
            }
        }
        if (deserializationCount == 0) {
            throw new IOException("No " + (isOneOf ? "one-of" : "any-of")
                    + " type deserializer found against given json");
        }

        return deserializedObject;
    }

    /**
     * Json deserialization of the given Json string.
     * @param json The Json string to deserialize.
     * @return The deserialized Json as a Map.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static LinkedHashMap<String, Object> deserialize(String json) throws IOException {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }

        TypeReference<LinkedHashMap<String, Object>> typeRef =
                new TypeReference<LinkedHashMap<String, Object>>() {};
        return deserialize(json, typeRef);
    }

    /**
     * JSON Deserialization of the given json string.
     * @param json The json string to deserialize.
     * @param typeReference TypeReference of T.
     * @param <T> The type of the object to deserialize into.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> T deserialize(String json, TypeReference<T> typeReference)
            throws IOException {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }

        return mapper.readValue(json, typeReference);
    }

    /**
     * JSON Deserialization of the given json string with FAIL_ON_UNKNOWN_PROPERTIES flag as true.
     * @param jsonNode The Json Node to deserialize.
     * @param typeReference TypeReference of T.
     * @param <T> The type of the object to deserialize into.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> T deserialize(
            JsonNode jsonNode, TypeReference<T> typeReference) throws IOException {
        if (jsonNode == null) {
            return null;
        }

        return strictMapper.convertValue(jsonNode, typeReference);
    }

    /**
     * JSON deserialization of the given JsonNode with FAIL_ON_UNKNOWN_PROPERTIES flag as true.
     * @param <T> The type of the object to deserialize into.
     * @param jsonNode The Json Node to deserialize.
     * @param clazz The type of the object to deserialize into.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> T deserialize(JsonNode jsonNode, Class<T> clazz)
            throws IOException {
        if (jsonNode == null) {
            return null;
        }

        return strictMapper.convertValue(jsonNode, clazz);
    }

    /**
     * XML Deserialization of the given xml string.
     * @param <T> The class of the object to deserialize into.
     * @param xml The xml string to deserialize.
     * @param cls The class of the object to deserialize into.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> T deserializeXml(String xml, Class<T> cls) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            JAXBElement<T> jaxbElement = jaxbUnmarshaller.unmarshal(new StreamSource(reader), cls);

            return jaxbElement.getValue();
        } catch (JAXBException jaxbException) {
            throw new IOException(jaxbException);
        }
    }

    /**
     * XML Deserialization of the given xml string.
     * @param <T> The class of the object to deserialize into.
     * @param xml The xml string to deserialize.
     * @param cls The class of the object to deserialize into.
     * @return The deserialized object list.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> List<T> deserializeXmlArray(String xml, Class<T[]> cls)
            throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            JAXBElement<T[]> jaxbElement =
                    jaxbUnmarshaller.unmarshal(new StreamSource(reader), cls);

            return Arrays.asList(jaxbElement.getValue());
        } catch (JAXBException jaxbException) {
            throw new IOException(jaxbException);
        }
    }

    /**
     * XML Deserialization of the given xml string for simple types.
     * @param <T> The class of the object to deserialize into.
     * @param xml The xml string to deserialize.
     * @param cls The class of the object to deserialize into.
     * @return The deserialized simple types object list.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> List<T> deserializeXmlSimpleTypesArray(
            String xml, Class<T> cls) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            List<T> deserializedList = new ArrayList<>();
            Pattern pattern = Pattern.compile("<.+?>(.+?)</.+?>");
            Matcher patternMatcher = pattern.matcher(xml);
            while (patternMatcher.find()) {
                StringReader reader = new StringReader(patternMatcher.group());
                T unmarshalledElement =
                        jaxbUnmarshaller.unmarshal(new StreamSource(reader), cls).getValue();
                deserializedList.add(unmarshalledElement);
            }
            return deserializedList;
        } catch (JAXBException jaxbException) {
            throw new IOException(jaxbException);
        }
    }

    /**
     * JSON Deserialization from custom deserializer based on given discriminator and registry.
     * @param jp Parsed used for reading JSON content.
     * @param ctxt Context that can be used to access information about this deserialization
     *        activity.
     * @param discriminator The model's discriminator.
     * @param registry The map containing all discriminators as keys and associated classes as
     *        values.
     * @param typesWithoutDiscriminator The list containing all types without discriminators.
     * @param isOneOf The boolean flag to validate for oneOf flow.
     * @param <T> the deserialized response type.
     * @return The deserialized object.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T> T deserialize(
            JsonParser jp, DeserializationContext ctxt, String discriminator,
            List<Map<String, Class<? extends T>>> registry,
            List<Class<? extends T>> typesWithoutDiscriminator, boolean isOneOf)
            throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode jsonNode = oc.readTree(jp);
        List<Class<? extends T>> types = deduceType(jsonNode, discriminator, registry);

        if (types == null || types.isEmpty()) {
            if (typesWithoutDiscriminator != null && !typesWithoutDiscriminator.isEmpty()) {
                types = typesWithoutDiscriminator;
            } else {
                throw new IOException("Discriminator is missing.");
            }
        }

        return deserialize(jsonNode, types, isOneOf);
    }

    /**
     * Json deserialization of the given Json string.
     * @param json The Json string to deserialize.
     * @return The deserialized Json as an Object.
     */
    public static Object deserializeAsObject(String json) {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }
        try {
            return CoreHelper.deserialize(json, new TypeReference<Object>() {});
        } catch (IOException e) {
            // Failed to deserialize when json is not representing a JSON object.
            // i.e. either its string or any primitive type.
            return json;
        }
    }

    /**
     * JSON Deserialization of the given json string.
     * @param <T> The type of the object to deserialize into.
     * @param json The Json string to deserialize.
     * @param classArray The class of the array of objects to deserialize into.
     * @return The deserialized list of objects.
     * @throws IOException Signals if any I/O exception occurred..
     */
    public static <T extends Object> List<T> deserializeArray(String json, Class<T[]> classArray)
            throws IOException {
        if (isNullOrWhiteSpace(json)) {
            return null;
        }

        return Arrays.asList(mapper.readValue(json, classArray));
    }



    /**
     * Replaces template parameters in the given URL.
     * @param queryBuilder The query string builder to replace the template parameters.
     * @param parameters The parameters to replace in the URL.
     */
    public static void appendUrlWithTemplateParameters(
            StringBuilder queryBuilder, Map<String, SimpleEntry<Object, Boolean>> parameters) {
        // Perform parameter validation
        if (null == queryBuilder) {
            throw new IllegalArgumentException(
                    "Given value for parameter \"queryBuilder\" is invalid.");
        }

        if (null == parameters) {
            return;
        }

        // Iterate and append parameters
        for (Map.Entry<String, SimpleEntry<Object, Boolean>> pair : parameters.entrySet()) {

            String replaceValue = "";
            Object element = pair.getValue().getKey();
            boolean shouldEncode = pair.getValue().getValue();

            // Load element value as string
            if (null == element) {
                replaceValue = "";
            } else if (element instanceof Collection<?>) {
                replaceValue =
                        flattenCollection("", (Collection<?>) element, shouldEncode, "%s%s%s", '/');
            } else {
                if (shouldEncode) {
                    replaceValue = tryUrlEncode(element.toString(), false);
                } else {
                    replaceValue = element.toString();
                }
            }

            // Find the template parameter and replace it with its value
            replaceAll(queryBuilder, "{" + pair.getKey() + "}", replaceValue);
        }
    }

    /**
     * Appends the given set of parameters to the given query string.
     * @param queryBuilder The query URL string to append the parameters.
     * @param parameters The parameters to append.
     * @param arraySerializationFormat the array serialization format.
     */
    public static void appendUrlWithQueryParameters(
            StringBuilder queryBuilder, Map<String, Object> parameters,
            ArraySerializationFormat arraySerializationFormat) {
        // Perform parameter validation
        if (queryBuilder == null) {
            throw new IllegalArgumentException(
                    "Given value for parameter \"queryBuilder\" is invalid.");
        }
        if (parameters == null || parameters.isEmpty()) {
            return;
        }

        // Check if query string already has parameters
        boolean hasParams = queryBuilder.indexOf("?") > 0;
        queryBuilder.append(hasParams ? '&' : '?');

        encodeObjectAsQueryString("", parameters, queryBuilder, arraySerializationFormat);
    }

    /**
     * Validates if the string is null, empty or whitespace.
     * @param s The string to validate.
     * @return The result of validation.
     */
    public static boolean isNullOrWhiteSpace(String s) {
        if (s == null) {
            return true;
        }

        int length = s.length();
        if (length > 0) {
            for (int start = 0, middle = length / 2,
                    end = length - 1; start <= middle; start++, end--) {
                if (s.charAt(start) > ' ' || s.charAt(end) > ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Replaces all occurrences of the given string in the string builder.
     * @param stringBuilder The string builder to update with replaced strings.
     * @param toReplace The string to replace in the string builder.
     * @param replaceWith The string to replace with.
     */
    public static void replaceAll(
            StringBuilder stringBuilder, String toReplace, String replaceWith) {
        int index = stringBuilder.indexOf(toReplace);

        while (index != -1) {
            stringBuilder.replace(index, index + toReplace.length(), replaceWith);
            index += replaceWith.length(); // Move to the end of the replacement
            index = stringBuilder.indexOf(toReplace, index);
        }
    }

    /**
     * Updates the user agent header value.
     * @param apiUserAgent the String value of apiUserAgent.
     * @param userAgentConfig the Map of user agent config.
     * @return {@link String}.
     */
    public static String updateUserAgent(String apiUserAgent, Map<String, String> userAgentConfig) {
        String engineVersion = System.getProperty("java.runtime.version");
        String osName = System.getProperty("os.name") + "-" + System.getProperty("os.version");
        userAgent = apiUserAgent;
        userAgent = userAgent.replace("{engine}", "JRE");
        userAgent =
                userAgent.replace("{engine-version}", engineVersion != null ? engineVersion : "");
        userAgent = userAgent.replace("{os-info}", osName != null ? osName : "");

        if (userAgentConfig != null) {
            userAgentConfig.forEach((key, value) -> {
                userAgent = userAgent.replace(key, value);
            });
        }

        return userAgent;
    }

    /**
     * Removes null values from the given map.
     * @param map Map of values.
     */
    public static void removeNullValues(Map<String, ?> map) {
        if (map == null) {
            return;
        }

        map.values().removeAll(Collections.singleton(null));
    }

    /**
     * Validates and processes the given URL.
     * @param url The given URL to process.
     * @return Pre-process URL as string.
     */
    public static String cleanUrl(StringBuilder url) {
        // Ensures that the URLs are absolute
        Pattern pattern = Pattern.compile("^(https?://[^/]+)");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid Url format.");
        }

        // Get the http protocol match
        String protocol = matcher.group(1);

        // Removes redundant forward slashes
        String query = url.substring(protocol.length());
        query = query.replaceAll("//+", "/");

        // Returns processed URL
        return protocol.concat(query);
    }

    /**
     * Prepares Array style form fields from a given array of values.
     * @param value Value for the form fields.
     * @param arraySerializationFormat serialization format.
     * @return Dictionary of form fields created from array elements.
     */
    public static List<SimpleEntry<String, Object>> prepareFormFields(
            Map<?, ?> value, ArraySerializationFormat arraySerializationFormat) {
        List<SimpleEntry<String, Object>> formFields = new ArrayList<>();
        if (value != null) {
            objectToList("", value, formFields, new HashSet<Integer>(), arraySerializationFormat);
        }
        return formFields;
    }

    /**
     * JSON Deserialization of the given json string with FAIL_ON_UNKNOWN_PROPERTIES flag as true.
     * @param <T> The type of the object to deserialize into.
     * @param json The Json string to deserialize.
     * @param classArray The class of the array of objects to deserialize into.
     * @return The deserialized list of objects.
     * @throws IOException Signals if any I/O exception occurred.
     */
    public static <T extends Object> List<T> deserializeArray(JsonNode json, Class<T[]> classArray)
            throws IOException {
        if (json == null) {
            return null;
        }

        return Arrays.asList(strictMapper.convertValue(json, classArray));
    }

    /**
     * Deduces the type based on given discriminator and registry.
     * @param jsonNode The json to check against.
     * @param discriminator The model's discriminator.
     * @param registry The Map containing all discriminators as keys and associated classes as
     *        values.
     * @param <T> The type of the object to deserialize into.
     * @return The type to deserialize into.
     * @throws IOException Signals if any I/O exception occurred.
     */
    private static <T> List<Class<? extends T>> deduceType(
            JsonNode jsonNode, String discriminator, List<Map<String, Class<? extends T>>> registry)
            throws IOException {
        if (jsonNode == null || registry == null) {
            return null;
        }

        final String discriminatorValue;
        if (jsonNode.isArray()) {
            if (jsonNode.has(0) && jsonNode.get(0).has(discriminator)) {
                // JSON is an array of model objects
                discriminatorValue = jsonNode.get(0).get(discriminator).asText();
            } else {
                discriminatorValue = deduceTypeFromImmidiateChild(jsonNode.get(0), discriminator);
            }
        } else {
            if (jsonNode.has(discriminator)) {
                // JSON is a model object
                discriminatorValue = jsonNode.get(discriminator).asText();
            } else {
                // JSON is a Map so deduce discriminator for first child only
                discriminatorValue = deduceTypeFromImmidiateChild(jsonNode, discriminator);
            }
        }

        return registry.stream().filter(item -> item.get(discriminatorValue) != null)
                .map(item -> item.get(discriminatorValue)).distinct().collect(Collectors.toList());
    }

    /**
     * Deduces the type from immediate child if exists based on given discriminator.
     * @param jsonNode The json to check against.
     * @param discriminator The model's discriminator.
     * @return The type to deserialize into.
     * @throws IOException Signals if any I/O exception occurred.
     */
    private static String deduceTypeFromImmidiateChild(JsonNode jsonNode, String discriminator) {
        Iterator<JsonNode> iterator = jsonNode.iterator();
        while (iterator.hasNext()) {
            JsonNode tempNode = iterator.next();
            if (tempNode.isArray()) {
                if (tempNode.has(0) && tempNode.get(0).has(discriminator)) {
                    // JSON is an array of model objects
                    return tempNode.get(0).get(discriminator).asText();
                }
            } else {
                if (tempNode.has(discriminator)) {
                    // JSON is a model object
                    return tempNode.get(discriminator).asText();
                }
            }
        }
        return null;
    }

    /**
     * Encodes a given object to URL encoded string.
     * @param name Name of the object.
     * @param obj Raw object sent from caller.
     * @param objBuilder String of elements.
     * @param arraySerializationFormat The array serialization format.
     */
    private static void encodeObjectAsQueryString(
            String name, Object obj, StringBuilder objBuilder,
            ArraySerializationFormat arraySerializationFormat) {

        List<SimpleEntry<String, Object>> objectList = new ArrayList<>();
        objectToList(name, obj, objectList, new HashSet<Integer>(), arraySerializationFormat);
        boolean hasParam = false;
        List<String> arrays = new ArrayList<String>();

        for (SimpleEntry<String, Object> pair : objectList) {
            String accessor = pair.getKey();
            // Ignore null
            Object value = pair.getValue();
            if (value == null) {
                continue;
            }

            hasParam = true;
            // Load element value as string


            if (accessor.matches(".*?\\[\\d+\\]$") && isDelimeterFormat(arraySerializationFormat)) {

                String arrayName = accessor.substring(0, accessor.lastIndexOf('['));

                if (arrays.contains(arrayName)) {
                    objBuilder.setLength(objBuilder.length() - 1);
                    accessor = getAccessorStringFormat(arraySerializationFormat);
                } else {
                    accessor = arrayName + "=";
                }

                if (!arrays.contains(arrayName)) {
                    arrays.add(arrayName);
                }

                appendParamKeyValuePair("%s%s&", objBuilder, accessor, value);
            } else {
                appendParamKeyValuePair("%s=%s&", objBuilder, accessor, value);
            }
        }

        // Removing the last &
        if (hasParam) {
            objBuilder.setLength(objBuilder.length() - 1);
        }
    }

    private static void appendParamKeyValuePair(
            String formatString, StringBuilder objBuilder, String accessor, Object value) {

        String paramKeyValPair =
                String.format(formatString, accessor, tryUrlEncode(value.toString(), false));
        objBuilder.append(paramKeyValPair);
    }

    /**
     * Flattening a collection of objects into a string.
     * @param elemName The element name of collection.
     * @param array Array of elements to flatten.
     * @param encode Need to encode?.
     * @param fmt Format string to use for array flattening.
     * @param separator Separator to use for string concatenation.
     * @return Representative string made up of array elements.
     */
    private static String flattenCollection(
            String elemName, Collection<?> array, boolean encode, String fmt, char separator) {
        StringBuilder builder = new StringBuilder();

        // Append all elements of the array into a string
        for (Object element : array) {
            String elemValue = null;

            // Replace null values with empty string to maintain index order
            if (element == null) {
                elemValue = "";
            } else {
                elemValue = element.toString();
            }
            if (encode) {
                elemValue = tryUrlEncode(elemValue, false);
            }
            builder.append(String.format(fmt, elemName, elemValue, separator));
        }

        // Remove the last separator, if appended
        if ((builder.length() > 1) && (builder.charAt(builder.length() - 1) == separator)) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    /**
     * Tries URL encode using UTF-8.
     * @param value The value to URL encode.
     * @param spaceAsPercentEncoded The flag get space character as percent encoded.
     * @return Encoded url.
     */
    public static String tryUrlEncode(String value, boolean spaceAsPercentEncoded) {
        try {
            String encodedUrl = URLEncoder.encode(value, "UTF-8");
            if (spaceAsPercentEncoded) {
                return encodedUrl.replace("+", "%20");
            }
            return encodedUrl;
        } catch (UnsupportedEncodingException ex) {
            return value;
        }
    }

    /**
     * Responsible to encode into base64 the username and password
     * @param basicAuthUserName The auth username
     * @param basicAuthPassword The auth password
     * @return The base64 encoded String
     */
    public static String getBase64EncodedCredentials(
            String basicAuthUserName, String basicAuthPassword) {
        String authCredentials = basicAuthUserName + ":" + basicAuthPassword;
        return "Basic " + Base64.getEncoder().encodeToString(authCredentials.getBytes());
    }

    private static void objectToList(
            String objName, Collection<?> obj, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, ArraySerializationFormat arraySerializationFormat) {

        Collection<?> array = obj;
        // Append all elements of the array into a string
        int index = 0;
        for (Object element : array) {
            // load key value pair
            String key;

            if (isWrapperType(element)
                    && (arraySerializationFormat == ArraySerializationFormat.UNINDEXED
                            || arraySerializationFormat == ArraySerializationFormat.PLAIN)) {
                key =
                        arraySerializationFormat == ArraySerializationFormat.UNINDEXED
                                ? String.format("%s[]", objName)
                                : objName;
            } else {
                key = String.format("%s[%d]", objName, index++);
            }
            loadKeyValuePairForEncoding(key, element, objectList, processed,
                    arraySerializationFormat);
        }

    }

    private static void objectToList(
            String objName, Map<?, ?> obj, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, ArraySerializationFormat arraySerializationFormat) {
        // Process map
        Map<?, ?> map = obj;
        // Append all elements of the array into a string
        for (Map.Entry<?, ?> pair : map.entrySet()) {
            String attribName = pair.getKey().toString();
            if ((objName != null) && (!objName.isEmpty())) {
                attribName = String.format("%s[%s]", objName, attribName);
            }
            loadKeyValuePairForEncoding(attribName, pair.getValue(), objectList, processed,
                    arraySerializationFormat);
        }
    }

    /**
     * Converts a given object to a form encoded map.
     * @param objName Name of the object.
     * @param obj The object to convert into a map.
     * @param objectList The object list to populate.
     * @param processed List of object hashCodes that are already parsed.
     * @param arraySerializationFormat The array serialization format.
     */
    private static void objectToList(
            String objName, Object obj, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, ArraySerializationFormat arraySerializationFormat) {
        // Null values need not to be processed
        if (obj == null) {
            return;
        }

        // Wrapper types are autoboxed, so reference checking is not needed
        Class<?> clazz = obj.getClass();

        Annotation typeCombinatorAnnotation = clazz.getAnnotation(TypeCombinatorCase.class);
        if (!isWrapperType(clazz) && typeCombinatorAnnotation == null) {
            // Avoid infinite recursion
            if (processed.contains(objName.hashCode())) {
                return;
            }
            processed.add(objName.hashCode());
        }

        // Process arrays
        if (obj instanceof Collection<?>) {
            objectToList(objName, (Collection<?>) obj, objectList, processed,
                    arraySerializationFormat);
        } else if (obj.getClass().isArray()) {
            // Process array

            Object[] array = (Object[]) obj;
            // Append all elements in the array into a string
            int index = 0;
            for (Object element : array) {
                // Load key value pair
                String key = String.format("%s[%d]", objName, index++);
                loadKeyValuePairForEncoding(key, element, objectList, processed,
                        arraySerializationFormat);
            }
        } else if (obj instanceof Map) {
            objectToList(objName, (Map<?, ?>) obj, objectList, processed, arraySerializationFormat);
        } else {
            // Process objects
            if (typeCombinatorAnnotation != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    // unexpected field $jococoData came for unit test coverage and makes test fails
                    if (field.getName() == "$jacocoData") {
                        continue;
                    }

                    Object fieldValue = null;
                    Annotation serializeAnnotation = null;
                    try {
                        field.setAccessible(true);
                        fieldValue = field.get(obj);
                        serializeAnnotation = field.getAnnotation(JsonSerialize.class);
                        if (serializeAnnotation == null) {
                            serializeAnnotation = field.getAnnotation(FormSerialize.class);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        // Ignoring the exception
                    }

                    if (serializeAnnotation != null) {
                        if (serializeAnnotation instanceof JsonSerialize) {
                            loadKeyValuePairForEncoding(objName, fieldValue, objectList, processed,
                                    (JsonSerialize) serializeAnnotation, arraySerializationFormat);
                        } else {
                            loadKeyValuePairForEncoding(objName, fieldValue, objectList, processed,
                                    (FormSerialize) serializeAnnotation, arraySerializationFormat);
                        }
                    } else {
                        loadKeyValuePairForEncoding(objName, fieldValue, objectList, processed,
                                arraySerializationFormat);
                    }
                }
                return;
            }
            // Invoke getter methods
            while (clazz != null) {
                for (Method method : clazz.getDeclaredMethods()) {

                    // Is a public/protected getter or internalGetter?
                    if (method.getParameterTypes().length != 0
                            || Modifier.isPrivate(method.getModifiers())
                            || (!method.getName().startsWith("get")
                                    && !method.getName().startsWith("internalGet"))) {
                        continue;
                    }

                    // Get JsonGetter annotation
                    Annotation getterAnnotation = method.getAnnotation(JsonGetter.class);
                    if (getterAnnotation == null) {
                        continue;
                    }

                    // Load key name from getter attribute name
                    String attribName = ((JsonGetter) getterAnnotation).value();
                    if ((objName != null) && (!objName.isEmpty())) {
                        attribName = String.format("%s[%s]", objName, attribName);
                    }

                    try {
                        // Load value by invoking getter method
                        method.setAccessible(true);
                        Object value = method.invoke(obj);
                        JsonSerialize serializerAnnotation =
                                method.getAnnotation(JsonSerialize.class);
                        // Load key value pair into objectList
                        if (serializerAnnotation != null) {
                            loadKeyValuePairForEncoding(attribName, value, objectList, processed,
                                    serializerAnnotation, arraySerializationFormat);
                        } else {
                            loadKeyValuePairForEncoding(attribName, value, objectList, processed,
                                    arraySerializationFormat);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        // This block only calls getter methods.
                        // These getters don't throw any exception except invocationTargetException.
                        // The getters are public so there is no chance of an IllegalAccessException
                        // Steps we've followed ensure that the object has the specified method.
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    private static String getAccessorStringFormat(
            ArraySerializationFormat arraySerializationFormat) {
        switch (arraySerializationFormat) {
            case CSV:
                return CSV_FORMAT;
            case PSV:
                return PSV_FORMAT;
            case TSV:
                return TSV_FORMAT;
            default:
                return "";
        }
    }

    private static boolean isDelimeterFormat(ArraySerializationFormat arraySerializationFormat) {
        return (arraySerializationFormat == ArraySerializationFormat.CSV
                || arraySerializationFormat == ArraySerializationFormat.TSV
                || arraySerializationFormat == ArraySerializationFormat.PSV);
    }

    /**
     * Processes the value and load into objectList against key.
     * @param key The key to used for creation of key value pair.
     * @param value The value to process against the given key.
     * @param objectList The object list to process with key value pair.
     * @param processed List of processed objects hashCodes.
     * @param serializer The serializer for serialize the object.
     * @param arraySerializationFormat The array serialization format.
     * @throws JsonProcessingException Signals that a Json Processing Exception has occurred.
     */
    private static void loadKeyValueUsingSerializer(
            String key, Object value, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, JsonSerializer<?> serializer,
            ArraySerializationFormat arraySerializationFormat) throws JsonProcessingException {
        value = serialize(value, serializer);

        Object obj = deserializeAsObject(value.toString());
        if (obj instanceof List<?> || obj instanceof Map<?, ?>) {
            loadKeyValuePairForEncoding(key, obj, objectList, processed, arraySerializationFormat);
        } else {
            if (value.toString().startsWith("\"")) {
                value = value.toString().substring(1, value.toString().length() - 1);
            }
            objectList.add(new SimpleEntry<String, Object>(key, value));
        }
    }


    /**
     * While processing objects to map, loads value after serializing.
     * @param key The key to used for creation of key value pair.
     * @param value The value to process against the given key.
     * @param objectList The object list to process with key value pair.
     * @param processed List of processed objects hashCodes.
     * @param formSerializerAnnotation Annotation for serializer.
     * @param arraySerializationFormat The array serialization format.
     */
    private static void loadKeyValuePairForEncoding(
            String key, Object value, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, FormSerialize formSerializerAnnotation,
            ArraySerializationFormat arraySerializationFormat) {
        if (value == null) {
            return;
        }

        try {
            JsonSerializer<?> serializer = getCollectionCustomSerializer(formSerializerAnnotation);
            loadKeyValueUsingSerializer(key, value, objectList, processed, serializer,
                    arraySerializationFormat);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    /**
     * While processing objects to map, decides whether to perform recursion or load value.
     * @param key The key for creating key value pair.
     * @param value The value to process against the given key.
     * @param objectList The object list to process with key value pair.
     * @param processed List of processed objects hashCodes.
     * @param arraySerializationFormat The array serialization format.
     */
    private static void loadKeyValuePairForEncoding(
            String key, Object value, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, ArraySerializationFormat arraySerializationFormat) {
        if (value == null) {
            return;
        }
        if (isWrapperType(value)) {
            objectList.add(new SimpleEntry<String, Object>(key, value));
        } else if (value instanceof CoreJsonObject) {
            objectToList(key, ((CoreJsonObject) value).getStoredObject(), objectList, processed,
                    arraySerializationFormat);
        } else if (value instanceof CoreJsonValue) {
            Object storedValue = ((CoreJsonValue) value).getStoredObject();
            if (isWrapperType(storedValue)) {
                objectList.add(new SimpleEntry<String, Object>(key, storedValue));
            } else {
                objectToList(key, storedValue, objectList, processed, arraySerializationFormat);
            }
        } else if (value instanceof UUID) {
            // UUIDs can be converted to string
            objectList.add(new SimpleEntry<String, Object>(key, value.toString()));
        } else {
            objectToList(key, value, objectList, processed, arraySerializationFormat);
        }
    }

    /**
     * While processing objects to map, loads value after serializing.
     * @param key The key to used for creation of key value pair.
     * @param value The value to process against the given key.
     * @param objectList The object list to process with key value pair.
     * @param processed List of processed objects hashCodes.
     * @param serializerAnnotation Annotation for serializer.
     * @param arraySerializationFormat The array serialization format.
     */
    private static void loadKeyValuePairForEncoding(
            String key, Object value, List<SimpleEntry<String, Object>> objectList,
            HashSet<Integer> processed, JsonSerialize serializerAnnotation,
            ArraySerializationFormat arraySerializationFormat) {
        if (value == null) {
            return;
        }

        try {
            JsonSerializer<?> serializer = getSerializer(serializerAnnotation);
            if (serializer == null) {
                serializer = getCollectionSerializer(serializerAnnotation);
            }

            loadKeyValueUsingSerializer(key, value, objectList, processed, serializer,
                    arraySerializationFormat);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the given object can be wrapped directly.
     * @param object The given object.
     * @return true if the class is an autoboxed class e.g., Integer.
     */
    private static boolean isWrapperType(Object object) {
        if (object == null) {
            return false;
        }
        return WRAPPER_TYPES.contains(object.getClass()) || object.getClass().isPrimitive()
                || object.getClass().isEnum();
    }

    /**
     * Json Serialization of an ENUM defined under oneOf/anyOf container.
     * @param value The object to serialize into Json String.
     * @return The serialized Json String representation of the given object.
     * @throws JsonProcessingException Signals that a Json Processing Exception has occurred.
     */
    public static String serializeEnumContainer(Object value) throws JsonProcessingException {
        if (value instanceof String || value instanceof Integer) {
            return String.valueOf(value);
        }

        return serialize(value);
    }

    /**
     * Custom deserializer class of any string property for disallowing implicit type conversion.
     */
    private static class CoercionLessStringDeserializer extends StringDeserializer {
        private static final long serialVersionUID = 1L;

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            if (p.getCurrentToken() != JsonToken.VALUE_STRING) {
                String message = "Cannot coerce " + p.getCurrentToken() + " to String value";
                throw MismatchedInputException.from(p, String.class, message);
            }
            return super.deserialize(p, ctxt);
        }
    }
}
