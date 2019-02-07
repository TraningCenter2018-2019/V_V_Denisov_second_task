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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneFieldObject that = (OneFieldObject) o;

        return getStringField() != null ? getStringField().equals(that.getStringField()) : that.getStringField() == null;
    }

    @Override
    public int hashCode() {
        return getStringField() != null ? getStringField().hashCode() : 0;
    }
}
