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
}
