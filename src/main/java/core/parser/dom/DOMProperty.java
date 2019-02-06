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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DOMProperty property = (DOMProperty) o;

        if (getKey() != null) {
            if (!getKey().equals(property.getKey())) {
                return false;
            }
        } else {
            if (property.getKey() != null) {
                return false;
            }
        }
        if (getValue() != null) {
            return getValue().equals(property.getValue());
        } else {
            return property.getValue() == null;
        }
    }

    /**
     * Coefficient used when calculating the hash.
     */
    private static final int HASH_SEARCH_FACTOR = 31;

    @Override
    public int hashCode() {
        int result;
        if (getKey() != null) {
            result = getKey().hashCode();
        } else {
            result = 0;
        }
        if (getValue() != null) {
            result = HASH_SEARCH_FACTOR * result + getValue().hashCode();
        } else {
            result = HASH_SEARCH_FACTOR * result;
        }
        return result;
    }
}
