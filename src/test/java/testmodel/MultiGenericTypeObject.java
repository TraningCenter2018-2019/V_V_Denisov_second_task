package testmodel;

public class MultiGenericTypeObject<T, V> {
    private T value;
    private V value2;

    public MultiGenericTypeObject(T value, V value2) {
        this.value = value;
        this.value2 = value2;
    }

    public MultiGenericTypeObject() {
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public V getValue2() {
        return value2;
    }

    public void setValue2(V value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "MultiGenericTypeObject{" +
                "value=" + value +
                ", value2=" + value2 +
                '}';
    }
}
