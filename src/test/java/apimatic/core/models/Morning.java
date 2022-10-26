package apimatic.core.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This is a model class for Morning type.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "sessionType", defaultImpl = Morning.class, visible = true)
@JsonInclude(Include.ALWAYS)
public class Morning {
    private String startsAt;
    private String endsAt;
    private boolean offerTeaBreak;
    private String sessionType;

    /**
     * Default constructor.
     */
    public Morning() {
        setSessionType("Morning");
    }

    /**
     * Initialization constructor.
     * 
     * @param startsAt String value for startsAt.
     * @param endsAt String value for endsAt.
     * @param offerTeaBreak boolean value for offerTeaBreak.
     * @param sessionType String value for sessionType.
     */
    public Morning(String startsAt, String endsAt, boolean offerTeaBreak, String sessionType) {
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.offerTeaBreak = offerTeaBreak;
        this.sessionType = sessionType;
    }

    /**
     * Getter for StartsAt. Session start time
     * 
     * @return Returns the String
     */
    @JsonGetter("startsAt")
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * Setter for StartsAt. Session start time
     * 
     * @param startsAt Value for String
     */
    @JsonSetter("startsAt")
    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * Getter for EndsAt. Session end time
     * 
     * @return Returns the String
     */
    @JsonGetter("endsAt")
    public String getEndsAt() {
        return endsAt;
    }

    /**
     * Setter for EndsAt. Session end time
     * 
     * @param endsAt Value for String
     */
    @JsonSetter("endsAt")
    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    /**
     * Getter for OfferTeaBreak. Offer tea break during session
     * 
     * @return Returns the boolean
     */
    @JsonGetter("offerTeaBreak")
    public boolean getOfferTeaBreak() {
        return offerTeaBreak;
    }

    /**
     * Setter for OfferTeaBreak. Offer tea break during session
     * 
     * @param offerTeaBreak Value for boolean
     */
    @JsonSetter("offerTeaBreak")
    public void setOfferTeaBreak(boolean offerTeaBreak) {
        this.offerTeaBreak = offerTeaBreak;
    }

    /**
     * Getter for SessionType.
     * 
     * @return Returns the String
     */
    @JsonGetter("sessionType")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSessionType() {
        return sessionType;
    }

    /**
     * Setter for SessionType.
     * 
     * @param sessionType Value for String
     */
    @JsonSetter("sessionType")
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Converts this Morning into string format.
     * 
     * @return String representation of this class
     */
    @Override
    public String toString() {
        return "Morning [" + "startsAt=" + startsAt + ", endsAt=" + endsAt + ", offerTeaBreak="
                + offerTeaBreak + ", sessionType=" + sessionType + "]";
    }

    /**
     * Builds a new {@link Morning.Builder} object. Creates the instance with the state of the
     * current model.
     * 
     * @return a new {@link Morning.Builder} object
     */
    public Builder toBuilder() {
        Builder builder =
                new Builder(startsAt, endsAt, offerTeaBreak).sessionType(getSessionType());
        return builder;
    }

    /**
     * Class to build instances of {@link Morning}.
     */
    public static class Builder {
        private String startsAt;
        private String endsAt;
        private boolean offerTeaBreak;
        private String sessionType = "Morning";

        /**
         * Initialization constructor.
         */
        public Builder() {}

        /**
         * Initialization constructor.
         * 
         * @param startsAt String value for startsAt.
         * @param endsAt String value for endsAt.
         * @param offerTeaBreak boolean value for offerTeaBreak.
         */
        public Builder(String startsAt, String endsAt, boolean offerTeaBreak) {
            this.startsAt = startsAt;
            this.endsAt = endsAt;
            this.offerTeaBreak = offerTeaBreak;
        }

        /**
         * Setter for startsAt.
         * 
         * @param startsAt String value for startsAt.
         * @return Builder
         */
        public Builder startsAt(String startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        /**
         * Setter for endsAt.
         * 
         * @param endsAt String value for endsAt.
         * @return Builder
         */
        public Builder endsAt(String endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        /**
         * Setter for offerTeaBreak.
         * 
         * @param offerTeaBreak boolean value for offerTeaBreak.
         * @return Builder
         */
        public Builder offerTeaBreak(boolean offerTeaBreak) {
            this.offerTeaBreak = offerTeaBreak;
            return this;
        }

        /**
         * Setter for sessionType.
         * 
         * @param sessionType String value for sessionType.
         * @return Builder
         */
        public Builder sessionType(String sessionType) {
            this.sessionType = sessionType;
            return this;
        }

        /**
         * Builds a new {@link Morning} object using the set fields.
         * 
         * @return {@link Morning}
         */
        public Morning build() {
            return new Morning(startsAt, endsAt, offerTeaBreak, sessionType);
        }
    }
}
