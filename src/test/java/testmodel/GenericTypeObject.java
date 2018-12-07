package testmodel;

public class GenericTypeObject<T> {
    private T value;

    public GenericTypeObject(T value) {
        this.value = value;
    }

    public GenericTypeObject() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GenericTypeObject{" +
                "value=" + value +
                '}';
    }
}
