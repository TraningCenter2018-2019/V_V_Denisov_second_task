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
        if (stringValue != null)
            return String.format("<string: \"%s\">", stringValue);
        else if (objectValue != null)
            return objectValue.toString();
        else
            return String.format("<list of values: [%s]>", getStringFromListValue());
    }

    private String getStringFromListValue(){
        String[] values = new String[listValue.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = listValue.get(i).toString();
        }
        return String.join(", ", values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DOMValue value = (DOMValue) o;

        if (getStringValue() != null ? !getStringValue().equals(value.getStringValue()) : value.getStringValue() != null)
            return false;
        if (getObjectValue() != null ? !getObjectValue().equals(value.getObjectValue()) : value.getObjectValue() != null)
            return false;
        return getListValue() != null ? getListValue().equals(value.getListValue()) : value.getListValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getStringValue() != null ? getStringValue().hashCode() : 0;
        result = 31 * result + (getObjectValue() != null ? getObjectValue().hashCode() : 0);
        result = 31 * result + (getListValue() != null ? getListValue().hashCode() : 0);
        return result;
    }
}
