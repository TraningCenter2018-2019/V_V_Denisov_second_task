package core.parser;

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

    /**
     * The association between string names of dynamic types and the classes they are.
     */
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

    /**
     * Returns the class, which is now a dynamic type, by its dynamic name.
     *
     * @param type type name
     * @return class of this type or null if it was not found
     */
    public Class getTypeByName(String type) {
        return references.get(type);
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
