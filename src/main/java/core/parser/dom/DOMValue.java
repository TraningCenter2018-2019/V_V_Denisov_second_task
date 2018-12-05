package core.parser.dom;

import java.util.List;

public class DOMValue {
    private String stringValue = null;
    private DOMObject objectValue = null;
    private List<DOMValue> listValue = null;

    public DOMValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public DOMValue(DOMObject objectValue) {
        this.objectValue = objectValue;
    }

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
