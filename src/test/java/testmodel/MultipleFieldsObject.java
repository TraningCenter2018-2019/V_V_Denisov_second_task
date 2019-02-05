package testmodel;

public class MultipleFieldsObject {
    private String stringField1;
    private String stringField2;

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    @Override
    public String toString() {
        return "MultipleFieldsObject{" +
                "stringField1='" + stringField1 + '\'' +
                ", stringField2='" + stringField2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultipleFieldsObject that = (MultipleFieldsObject) o;

        if (getStringField1() != null ? !getStringField1().equals(that.getStringField1()) : that.getStringField1() != null)
            return false;
        return getStringField2() != null ? getStringField2().equals(that.getStringField2()) : that.getStringField2() == null;
    }

    @Override
    public int hashCode() {
        int result = getStringField1() != null ? getStringField1().hashCode() : 0;
        result = 31 * result + (getStringField2() != null ? getStringField2().hashCode() : 0);
        return result;
    }
}
