package core.parser.dom;

/**
 * Represents json property (key-value based).
 */
public class DOMProperty {
    /**
     * Property key.
     */
    private String key;
    /**
     * Property value.
     */
    private DOMValue value;

    /**
     * Creates DOMProperty instance.
     */
    public DOMProperty() {
    }

    /**
     * Creates DOMProperty instance with key and value.
     *
     * @param key   key of property
     * @param value value of property
     */
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
