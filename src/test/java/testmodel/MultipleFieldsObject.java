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
}
