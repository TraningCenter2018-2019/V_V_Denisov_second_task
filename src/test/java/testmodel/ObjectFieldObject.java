package testmodel;

public class ObjectFieldObject {
    public static class InnerClass {
        private String innerStringField;

        public String getInnerStringField() {
            return innerStringField;
        }

        public void setInnerStringField(String innerStringField) {
            this.innerStringField = innerStringField;
        }

        @Override
        public String toString() {
            return "InnerClass{" +
                    "innerStringField='" + innerStringField + '\'' +
                    '}';
        }
    }

    private String stringField;
    private InnerClass innerClassField;

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public InnerClass getInnerClassField() {
        return innerClassField;
    }

    public void setInnerClassField(InnerClass innerClassField) {
        this.innerClassField = innerClassField;
    }

    @Override
    public String toString() {
        return "ObjectFieldObject{" +
                "stringField='" + stringField + '\'' +
                ", innerClassField=" + innerClassField +
                '}';
    }
}
