package io.apimatic.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * This is a container of annotations for oneOf/anyOf cases.
 */
public interface TypeCombinator {
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TypeCombinatorCase {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface FormSerialize {
        /**
         * Serializer class to use for serializing contents (elements of a Collection/array, values
         * of Maps) of annotated property. Can only be used on accessors (methods, fields,
         * constructors), to apply to values of {@link java.util.Map}-valued properties; not
         * applicable for value types used as Array elements or {@link java.util.Collection} and
         * {@link java.util.Map} values.
         * @return JsonSerializer instance
         */
        public Class<? extends JsonSerializer<?>> contentUsing() default JsonSerializer.None.class;
    }

    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TypeCombinatorStringCase {
    }
}
