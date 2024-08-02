package apimatic.core.utilities;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import io.apimatic.core.utilities.MapAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;

public class MapAdapterTest {

    private final MapAdapter mapAdapter = new MapAdapter();

    @Test
    public void marshalShouldReturnEntryListWhenGivenNonEmptyMap() throws Exception {
        // Arrange
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        // Act
        MapAdapter.EntryList result = mapAdapter.marshal(inputMap);

        // Assert
        assertEquals(2, result.getEntries().size());
        assertTrue(result.getEntries().stream()
                .anyMatch(e -> e.getAttribute("key").equals("key1")
                        && e.getTextContent().equals("value1")));
        assertTrue(result.getEntries().stream()
                .anyMatch(e -> e.getAttribute("key").equals("key2")
                        && e.getTextContent().equals("value2")));
    }

    @Test
    public void marshalShouldReturnEmptyEntryListWhenGivenEmptyMap() throws Exception {
        // Arrange
        Map<String, String> inputMap = new HashMap<>();

        // Act
        MapAdapter.EntryList result = mapAdapter.marshal(inputMap);

        // Assert
        assertNotNull(result);
        assertTrue(result.getEntries().isEmpty());
    }

    @Test
    public void unmarshalShouldReturnMapWhenGivenNonEmptyEntryList() throws Exception {
        // Arrange
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element entry1 = document.createElement("entry");
        entry1.setAttribute("key", "key1");
        entry1.setTextContent("value1");

        Element entry2 = document.createElement("entry");
        entry2.setAttribute("key", "key2");
        entry2.setTextContent("value2");

        MapAdapter.EntryList entryList = new MapAdapter.EntryList();
        entryList.getEntries().add(entry1);
        entryList.getEntries().add(entry2);

        // Act
        Map<String, String> result = mapAdapter.unmarshal(entryList);

        // Assert
        assertEquals(2, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void unmarshalShouldReturnEmptyMapWhenGivenEmptyEntryList() throws Exception {
        // Arrange
        MapAdapter.EntryList entryList = new MapAdapter.EntryList();

        // Act
        Map<String, String> result = mapAdapter.unmarshal(entryList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void marshalShouldHandleNullValues() throws Exception {
        // Arrange
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("key1", null);

        // Act
        MapAdapter.EntryList result = mapAdapter.marshal(inputMap);

        // Assert
        assertEquals(1, result.getEntries().size());
        assertEquals("", result.getEntries().get(0).getTextContent());
    }

    @Test
    public void marshalShouldHandleNullMap() throws Exception {
        // Arrange
        Map<String, String> inputMap = null;

        // Act
        MapAdapter.EntryList result = mapAdapter.marshal(inputMap);

        // Assert
        assertNull(result);
    }

    @Test
    public void unmarshalShouldHandleNullEntry() throws Exception {
        // Arrange
        MapAdapter.EntryList entryList = null;

        // Act
        Map<String, String> result = mapAdapter.unmarshal(entryList);

        // Assert
        assertNull(result);
    }

    @Test
    public void unmarshalShouldHandleEntriesWithEmptyTextContent() throws Exception {
        // Arrange
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element entry1 = document.createElement("entry");
        entry1.setAttribute("key", "key1");
        entry1.setTextContent("");

        MapAdapter.EntryList entryList = new MapAdapter.EntryList();
        entryList.getEntries().add(entry1);

        // Act
        Map<String, String> result = mapAdapter.unmarshal(entryList);

        // Assert
        assertEquals(1, result.size());
        assertEquals("", result.get("key1"));
    }
}
