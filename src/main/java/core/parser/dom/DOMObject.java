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

    @Override
    public String toString() {
        return String.format("<object: properties[%s]>", getStringFromProperties());
    }

    /**
     * Returns a string from properties.
     *
     * @return string from properties
     */
    private String getStringFromProperties() {
        String[] values = new String[properties.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = properties.get(i).toString();
        }
        return String.join(", ", values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DOMObject domObject = (DOMObject) o;

        if (getProperties() != null) {
            return getProperties().equals(domObject.getProperties());
        } else {
            return domObject.getProperties() == null;
        }
    }

    @Override
    public int hashCode() {
        if (getProperties() != null) {
            return getProperties().hashCode();
        } else {
            return 0;
        }
    }
}
