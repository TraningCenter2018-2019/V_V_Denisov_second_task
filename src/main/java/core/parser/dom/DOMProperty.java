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

    @Override
    public String toString() {
        return String.format("<key: %s, value: %s>", key, value.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOMProperty property = (DOMProperty) o;

        if (getKey() != null ? !getKey().equals(property.getKey()) : property.getKey() != null) return false;
        return getValue() != null ? getValue().equals(property.getValue()) : property.getValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
