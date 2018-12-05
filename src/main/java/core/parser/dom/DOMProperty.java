package core.parser.dom;

public class DOMProperty {
    private String key;
    private DOMValue value;

    public DOMProperty() {
    }

    public DOMProperty(String key, DOMValue value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DOMValue getValue() {
        return value;
    }

    public void setValue(DOMValue value) {
        this.value = value;
    }
}
