package testmodel;

import java.util.List;

public class ListFieldObject {
    private List<String> stringList;

    public ListFieldObject() {
    }

    public ListFieldObject(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public String toString() {
        return "ListFieldObject{" +
                "stringList=" + stringList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListFieldObject that = (ListFieldObject) o;

        return getStringList() != null ? getStringList().equals(that.getStringList()) : that.getStringList() == null;
    }

    @Override
    public int hashCode() {
        return getStringList() != null ? getStringList().hashCode() : 0;
    }
}
