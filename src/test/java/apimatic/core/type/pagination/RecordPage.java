package apimatic.core.type.pagination;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

public class RecordPage {
    @JsonSetter("data")
    public List<String> data;

    @JsonSetter("page_info")
    public String pageInfo;

    @JsonSetter("next_link")
    public String nextLink;

}
