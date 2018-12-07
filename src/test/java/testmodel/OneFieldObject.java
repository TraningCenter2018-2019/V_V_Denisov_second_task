package testmodel;

public class OneFieldObject {
    private String stringField;

    public OneFieldObject() {
    }

    public OneFieldObject(String stringField) {
        this.stringField = stringField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    @Override
    public String toString() {
        return "OneFieldObject{" +
                "stringField='" + stringField + '\'' +
                '}';
    }
}
