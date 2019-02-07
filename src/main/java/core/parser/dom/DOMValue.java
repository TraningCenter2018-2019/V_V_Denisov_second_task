package core.parser.dom;

import java.util.List;

/**
 * JSON value can be either string or object or array (list).
 */
public class DOMValue {
    /**
     * String value.
     */
    private String stringValue = null;
    /**
     * Object value.
     */
    private DOMObject objectValue = null;
    /**
     * Array value.
     */
    private List<DOMValue> listValue = null;

    /**
     * Creates DOMValue instance with string value. Other values will be null.
     *
     * @param stringValue value
     */
    public DOMValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Creates DOMValue instance with object value. Other values will be null.
     *
     * @param objectValue value
     */
    public DOMValue(DOMObject objectValue) {
        this.objectValue = objectValue;
    }

    /**
     * Creates DOMValue instance with array value. Other values will be null.
     *
     * @param listValue value
     */
    public DOMValue(List<DOMValue> listValue) {
        this.listValue = listValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public DOMObject getObjectValue() {
        return objectValue;
    }

    public List<DOMValue> getListValue() {
        return listValue;
    }

    @Override
    public String toString() {
        if (stringValue != null) {
            return String.format("<string: \"%s\">", stringValue);
        } else if (objectValue != null) {
            return objectValue.toString();
        } else {
            return String.format("<list of values: [%s]>", getStringFromListValue());
        }
    }

    /**
     * Returns a list of values as a string.
     *
     * @return values as string
     */
    private String getStringFromListValue() {
        String[] values = new String[listValue.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = listValue.get(i).toString();
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

        DOMValue value = (DOMValue) o;

        if (getStringValue() != null) {
            if (!getStringValue().equals(value.getStringValue())) {
                return false;
            }
        } else {
            if (value.getStringValue() != null) {
                return false;
            }
        }
        if (getObjectValue() != null) {
            if (!getObjectValue().equals(value.getObjectValue())) {
                return false;
            }
        } else {
            if (value.getObjectValue() != null) {
                return false;
            }
        }
        if (getListValue() != null) {
            return getListValue().equals(value.getListValue());
        } else {
            return value.getListValue() == null;
        }
    }

    /**
     * Coefficient used when calculating the hash.
     */
    private static final int HASH_SEARCH_FACTOR = 31;

    @Override
    public int hashCode() {
        int result;
        if (getStringValue() != null) {
            result = getStringValue().hashCode();
        } else {
            result = 0;
        }
        if (getObjectValue() != null) {
            result = HASH_SEARCH_FACTOR * result + getObjectValue().hashCode();
        } else {
            result = HASH_SEARCH_FACTOR * result;
        }
        if (getListValue() != null) {
            result = HASH_SEARCH_FACTOR * result + getListValue().hashCode();
        } else {
            result = HASH_SEARCH_FACTOR * result;
        }
        return result;
    }
}
