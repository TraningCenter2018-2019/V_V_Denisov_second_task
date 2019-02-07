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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiGenericTypeObject<?, ?> that = (MultiGenericTypeObject<?, ?>) o;

        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        return getValue2() != null ? getValue2().equals(that.getValue2()) : that.getValue2() == null;
    }

    @Override
    public int hashCode() {
        int result = getValue() != null ? getValue().hashCode() : 0;
        result = 31 * result + (getValue2() != null ? getValue2().hashCode() : 0);
        return result;
    }
}
