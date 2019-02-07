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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InnerClass that = (InnerClass) o;

            return getInnerStringField() != null ? getInnerStringField().equals(that.getInnerStringField()) : that.getInnerStringField() == null;
        }

        @Override
        public int hashCode() {
            return getInnerStringField() != null ? getInnerStringField().hashCode() : 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectFieldObject that = (ObjectFieldObject) o;

        if (getStringField() != null ? !getStringField().equals(that.getStringField()) : that.getStringField() != null)
            return false;
        return getInnerClassField() != null ? getInnerClassField().equals(that.getInnerClassField()) : that.getInnerClassField() == null;
    }

    @Override
    public int hashCode() {
        int result = getStringField() != null ? getStringField().hashCode() : 0;
        result = 31 * result + (getInnerClassField() != null ? getInnerClassField().hashCode() : 0);
        return result;
    }
}
