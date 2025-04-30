package apimatic.core.type.pagination;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a page of records with pagination details.
 */
public class RecordPage {

    /**
     * The list of data items on the current page.
     */
    @JsonSetter("data")
    private List<String> data;

    /**
     * Information about the current page, such as offset or cursor.
     */
    @JsonSetter("page_info")
    private String pageInfo;

    /**
     * The link to the next page of results, if available.
     */
    @JsonSetter("next_link")
    private String nextLink;

    /**
     * Gets the data items on the current page.
     *
     * @return the data list.
     */
    public List<String> getData() {
        return data;
    }

    /**
     * Sets the data items on the current page.
     *
     * @param data the data list.
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * Gets the page information.
     *
     * @return the page info.
     */
    public String getPageInfo() {
        return pageInfo;
    }

    /**
     * Sets the page information.
     *
     * @param pageInfo the page info.
     */
    public void setPageInfo(String pageInfo) {
        this.pageInfo = pageInfo;
    }

    /**
     * Gets the link to the next page.
     *
     * @return the next link.
     */
    public String getNextLink() {
        return nextLink;
    }

    /**
     * Sets the link to the next page.
     *
     * @param nextLink the next link.
     */
    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }
}
