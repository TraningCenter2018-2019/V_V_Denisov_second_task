package core.parser.dom;

import java.util.ArrayList;
import java.util.List;

public class DOMObject {
    private List<DOMProperty> properties = new ArrayList<>();

    public DOMObject() {
    }

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
