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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericTypeObject<?> that = (GenericTypeObject<?>) o;

        return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
    }

    @Override
    public int hashCode() {
        return getValue() != null ? getValue().hashCode() : 0;
    }
}
