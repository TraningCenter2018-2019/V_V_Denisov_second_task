package core.parser;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;

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

    private HashMap<String, Class> references = new HashMap<>();

    /**
     * Creates type reference.
     */
    public TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException();
        }
        ParameterizedType parameterized = ((ParameterizedType) superClass);
        type = parameterized.getActualTypeArguments()[0];
        Class ref = (Class<T>) ((ParameterizedType) this.getType()).getRawType();
        for (int i = 0; i < ref.getTypeParameters().length; i++) {
            TypeVariable<Class> typeParameter = ref.getTypeParameters()[i];
            references.put(typeParameter.getName(), (Class) ((ParameterizedType) type).getActualTypeArguments()[i]);
        }
    }

    public Type getType() {
        return type;
    }

    public Class getTypeByName(String type) {
        return references.get(type);
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
