package io.apimatic.core.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapAdapter extends XmlAdapter<MapAdapter.EntryList, Map<String, String>> {

    public static class EntryList {
        @XmlAnyElement
        public List<Element> entries = new ArrayList<Element>();
    }

    @Override
    public EntryList marshal(Map<String, String> map) throws Exception {
        if (map == null) return null;
        
        DocumentBuilder docBuilder;
        docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.newDocument();
        EntryList adaptedMap = new EntryList();
        for(Entry<String, String> entry : map.entrySet()) {
            Element element = document.createElement("entry");
            element.setAttribute("key", entry.getKey());
            element.setTextContent(entry.getValue());
            adaptedMap.entries.add(element);
        }
        return adaptedMap;
    }

    @Override
    public Map<String, String> unmarshal(EntryList adaptedMap) throws Exception {
        if (adaptedMap == null) return null;
        
        HashMap<String, String> map = new HashMap<String, String>();
        for(Element element : adaptedMap.entries) {
            map.put(element.getAttribute("key"), element.getTextContent());
        }
        return map;
    }

}
