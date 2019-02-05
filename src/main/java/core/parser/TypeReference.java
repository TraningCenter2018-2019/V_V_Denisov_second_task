package core.parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Reference to object type.
 *
 * @param <T> object type
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
    /**
     * Type object field.
     */
    private final Type type;

    /**
     * Creates type reference.
     */
    public TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException();
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
