package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This is a model class for Evening type.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "sessionType",
        defaultImpl = Evening.class,
        visible = true)
@JsonInclude(Include.ALWAYS)
public class Evening {
    private String startsAt;
    private String endsAt;
    private boolean offerDinner;
    private String sessionType;

    /**
     * Default constructor.
     */
    public Evening() {
        setSessionType("Evening");
    }

    /**
     * Initialization constructor.
     * @param  startsAt  String value for startsAt.
     * @param  endsAt  String value for endsAt.
     * @param  offerDinner  boolean value for offerDinner.
     * @param  sessionType  String value for sessionType.
     */
    public Evening(
            String startsAt,
            String endsAt,
            boolean offerDinner,
            String sessionType) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.offerDinner = offerDinner;
        this.sessionType = sessionType;
    }

    /**
     * Getter for StartsAt.
     * Session start time
     * @return Returns the String
     */
    @JsonGetter("startsAt")
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * Setter for StartsAt.
     * Session start time
     * @param startsAt Value for String
     */
    @JsonSetter("startsAt")
    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * Getter for EndsAt.
     * Session end time
     * @return Returns the String
     */
    @JsonGetter("endsAt")
    public String getEndsAt() {
        return endsAt;
    }

    /**
     * Setter for EndsAt.
     * Session end time
     * @param endsAt Value for String
     */
    @JsonSetter("endsAt")
    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    /**
     * Getter for OfferDinner.
     * Offer dinner during session
     * @return Returns the boolean
     */
    @JsonGetter("offerDinner")
    public boolean getOfferDinner() {
        return offerDinner;
    }

    /**
     * Setter for OfferDinner.
     * Offer dinner during session
     * @param offerDinner Value for boolean
     */
    @JsonSetter("offerDinner")
    public void setOfferDinner(boolean offerDinner) {
        this.offerDinner = offerDinner;
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
     * Converts this Evening into string format.
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Evening [" + "startsAt=" + startsAt + ", endsAt=" + endsAt + ", offerDinner="
                + offerDinner + ", sessionType=" + sessionType + "]";
    }

    /**
     * Builds a new {@link Evening.Builder} object.
     * Creates the instance with the state of the current model.
     * @return a new {@link Evening.Builder} object
     */
    public Builder toBuilder() {
        Builder builder = new Builder(startsAt, endsAt, offerDinner)
                .sessionType(getSessionType());
        return builder;
    }

    /**
     * Class to build instances of {@link Evening}.
     */
    public static class Builder {
        private String startsAt;
        private String endsAt;
        private boolean offerDinner;
        private String sessionType = "Evening";

        /**
         * Initialization constructor.
         */
        public Builder() {
        }

        /**
         * Initialization constructor.
         * @param  startsAt  String value for startsAt.
         * @param  endsAt  String value for endsAt.
         * @param  offerDinner  boolean value for offerDinner.
         */
        public Builder(String startsAt, String endsAt, boolean offerDinner) {
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.offerDinner = offerDinner;
        }

        /**
         * Setter for startsAt.
         * @param  startsAt  String value for startsAt.
         * @return Builder
         */
        public Builder startsAt(String startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        /**
         * Setter for endsAt.
         * @param  endsAt  String value for endsAt.
         * @return Builder
         */
        public Builder endsAt(String endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        /**
         * Setter for offerDinner.
         * @param  offerDinner  boolean value for offerDinner.
         * @return Builder
         */
        public Builder offerDinner(boolean offerDinner) {
            this.offerDinner = offerDinner;
            return this;
        }

        /**
         * Setter for sessionType.
         * @param  sessionType  String value for sessionType.
         * @return Builder
         */
        public Builder sessionType(String sessionType) {
            this.sessionType = sessionType;
            return this;
        }

        /**
         * Builds a new {@link Evening} object using the set fields.
         * @return {@link Evening}
         */
        public Evening build() {
            return new Evening(startsAt, endsAt, offerDinner, sessionType);
        }
    }
}
