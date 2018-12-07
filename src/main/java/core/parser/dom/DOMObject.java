package core.parser.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents JSONObject.
 */
public class DOMObject {
    /**
     * JSON object contains list of properties.
     */
    private List<DOMProperty> properties = new ArrayList<>();

    /**
     * Creates DOMObject instance.
     */
    public DOMObject() {
    }

    /**
     * Creates DOMObject instance with property list.
     *
     * @param properties properties of json object
     */
    public DOMObject(List<DOMProperty> properties) {
        this.properties = properties;
    }

    public List<DOMProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<DOMProperty> properties) {
        this.properties = properties;
    }
}
