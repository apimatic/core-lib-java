package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This is a model class for Noon type.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "sessionType", defaultImpl = Noon.class, visible = true)
@JsonInclude(Include.ALWAYS)
public class Noon {
    private String startsAt;
    private String endsAt;
    private boolean offerLunch;
    private String sessionType;

    /**
     * Default constructor.
     */
    public Noon() {
        setSessionType("Noon");
    }

    /**
     * Initialization constructor.
     * @param startsAt String value for startsAt.
     * @param endsAt String value for endsAt.
     * @param offerLunch boolean value for offerLunch.
     * @param sessionType String value for sessionType.
     */
    public Noon(final String startsAt, final String endsAt, boolean offerLunch, final String sessionType) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.offerLunch = offerLunch;
        this.sessionType = sessionType;
    }

    /**
     * Getter for StartsAt. Session start time
     * @return Returns the String
     */
    @JsonGetter("startsAt")
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * Setter for StartsAt. Session start time
     * @param startsAt Value for String
     */
    @JsonSetter("startsAt")
    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * Getter for EndsAt. Session end time
     * @return Returns the String
     */
    @JsonGetter("endsAt")
    public String getEndsAt() {
        return endsAt;
    }

    /**
     * Setter for EndsAt. Session end time
     * @param endsAt Value for String
     */
    @JsonSetter("endsAt")
    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    /**
     * Getter for OfferLunch. Offer lunch during session
     * @return Returns the boolean
     */
    @JsonGetter("offerLunch")
    public boolean getOfferLunch() {
        return offerLunch;
    }

    /**
     * Setter for OfferLunch. Offer lunch during session
     * @param offerLunch Value for boolean
     */
    @JsonSetter("offerLunch")
    public void setOfferLunch(boolean offerLunch) {
        this.offerLunch = offerLunch;
    }

    /**
     * Getter for SessionType.
     * @return Returns the String
     */
    @JsonGetter("sessionType")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSessionType() {
        return sessionType;
    }

    /**
     * Setter for SessionType.
     * @param sessionType Value for String
     */
    @JsonSetter("sessionType")
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Converts this Noon into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Noon [" + "startsAt=" + startsAt + ", endsAt=" + endsAt + ", offerLunch="
                + offerLunch + ", sessionType=" + sessionType + "]";
    }

    /**
     * Builds a new {@link Noon.Builder} object. Creates the instance with the state of the current
     * model.
     * @return a new {@link Noon.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(startsAt, endsAt, offerLunch).sessionType(getSessionType());
        return builder;
    }

    /**
     * Class to build instances of {@link Noon}.
     */
    public static class Builder {
        private String startsAt;
        private String endsAt;
        private boolean offerLunch;
        private String sessionType = "Noon";

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * @param startsAt String value for startsAt.
         * @param endsAt String value for endsAt.
         * @param offerLunch boolean value for offerLunch.
         */
        public Builder(final String startsAt, final String endsAt, boolean offerLunch) {
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.offerLunch = offerLunch;
        }

        /**
         * Setter for startsAt.
         * @param startsAt String value for startsAt.
         * @return Builder
         */
        public Builder startsAt(String startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        /**
         * Setter for endsAt.
         * @param endsAt String value for endsAt.
         * @return Builder
         */
        public Builder endsAt(String endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        /**
         * Setter for offerLunch.
         * @param offerLunch boolean value for offerLunch.
         * @return Builder
         */
        public Builder offerLunch(boolean offerLunch) {
            this.offerLunch = offerLunch;
            return this;
        }

        /**
         * Setter for sessionType.
         * @param sessionType String value for sessionType.
         * @return Builder
         */
        public Builder sessionType(String sessionType) {
            this.sessionType = sessionType;
            return this;
        }

        /**
         * Builds a new {@link Noon} object using the set fields.
         * @return {@link Noon}
         */
        public Noon build() {
            return new Noon(startsAt, endsAt, offerLunch, sessionType);
        }
    }
}
