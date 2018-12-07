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
}
