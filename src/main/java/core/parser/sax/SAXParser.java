package core.parser.sax;

import core.parser.Parser;
import core.parser.TypeReference;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * SAX - Simple API for XML. In this case Simple API for JSON.
 * Event-based parser.
 */
public class SAXParser implements Parser {
    /**
     * Stack of objects to create.
     */
    private Stack<Object> objectStack = new Stack<>();

    /**
     * Stack of method names to call.
     */
    private Stack<String> methodNameStack = new Stack<>();

    /**
     * Stack of lists to create.
     */
    private Stack<List> listStack = new Stack<>();

    @Override
    public <T> T parseString(String toParse, Class<T> type)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        T obj = (T) type.newInstance();
        return obj;
    }

    @Override
    public <T> T parseString(String toParse, TypeReference<T> ref) throws Exception {
        Class<?> clazz = null;
        try {
            clazz = ((ParameterizedTypeImpl) ref.getType()).getRawType();
        } catch (ClassCastException ignored) {
            clazz = (Class<?>) ref.getType();
        }
        //((ParameterizedTypeImpl) ref.getType()).getActualTypeArguments();
        if (clazz == List.class) {
            clazz = ArrayList.class;
        }
        T obj = (T) clazz.newInstance();
        return obj;
    }

    /**
     * It called when the parser encounters the start of the object.
     *
     * @param clazz type of object which parser met
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible
     * @throws InstantiationException if this class represents an abstract class,
     *                                an interface, an array class, a primitive type, or void;
     *                                or if the class has no nullary constructor;
     *                                or if the instantiation fails for some other reason.
     */
    private void startObject(Class<?> clazz)
            throws IllegalAccessException, InstantiationException {
        objectStack.push(clazz.newInstance());
    }

    /**
     * It called when the parser encounters the start of the object.
     *
     * @throws IllegalAccessException    if the class or its nullary constructor is not accessible
     * @throws NoSuchMethodException     if a setter method is not found.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    private void endObject()
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!methodNameStack.empty()) {
            endProperty(objectStack.pop());
        }
    }

    /**
     * It called when the parser encounters the start of the property.
     *
     * @param key property key
     * @throws NoSuchMethodException if a setter method is not found.
     */
    private void startProperty(String key) throws NoSuchMethodException {
        String setterNamePattern = "set%S%s";
        String setterName = String.format(setterNamePattern, key.charAt(0), key.substring(1));
        methodNameStack.push(setterName);
    }

    /**
     * Set property value.
     *
     * @param value property value. Could be any type
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException    if the class or its nullary constructor is not accessible
     * @throws NoSuchMethodException     if a setter method is not found.
     */
    private void endProperty(Object value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setter = getMethod(objectStack.peek(), methodNameStack.pop(), value.getClass());
        setter.invoke(objectStack.peek(), value);
    }

    /**
     * It called when the parser encounters the start of the array.
     */
    private void startArray() {
        listStack.push(new ArrayList());
        objectStack.push(listStack.peek());
    }

    /**
     * It called when the parser encounters the end of the array.
     *
     * @throws IllegalAccessException    if the class or its nullary constructor is not accessible
     * @throws NoSuchMethodException     if a setter method is not found.
     * @throws InvocationTargetException if the underlying method throws an exception.
     */
    private void endArray() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!methodNameStack.empty()) {
            endProperty(objectStack.pop());
        }
    }

    /**
     * It called when the parser encounters the start of the array value.
     * Array value is a value into array.
     */
    private void startArrayValue() {
        methodNameStack.push("add");
    }

    /**
     * Method returns a {@link Method} instance of requested method.
     * Firstly, it searches a method with one param by type.
     * If it does not, then it searches for a method that accepts {@link Object}.
     * In the case of a {@link ArrayList}, searches for methods among the interface.
     *
     * @param object    object to find a method
     * @param name      name of the method
     * @param paramType type of method only parameter
     * @return method by name and param type
     * @throws NoSuchMethodException if a matching method is not found
     *                               or if the name is "&lt;init&gt;"or "&lt;clinit&gt;".
     */
    private Method getMethod(Object object, String name, Class<?> paramType) throws NoSuchMethodException {
        try {
            return object.getClass().getMethod(name, paramType);
        } catch (NoSuchMethodException ignored) {
            if (paramType == ArrayList.class) {
                return getMethod(object, name, List.class);
            }
            return object.getClass().getMethod(name, Object.class);
        }
    }

//    public static void main(String[] args)
//            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
//        SAXParser<Person> parser = new SAXParser<>();
//        parser.startObject(Person.class);
//        parser.startProperty("personName");
//        parser.endProperty("Vasiliy");
//        parser.startProperty("personalCat");
//        parser.startObject(Cat.class);
//        parser.startProperty("name");
//        parser.endProperty("Murzik");
//        parser.endObject();
//        parser.startProperty("otherCats");
//        parser.startArray();
//
//        parser.startArrayValue();
//        parser.startObject(Cat.class);
//        parser.startProperty("name");
//        parser.endProperty("otherCat1");
//        parser.endObject();
//
//        parser.startArrayValue();
//        parser.startObject(Cat.class);
//        parser.startProperty("name");
//        parser.endProperty("otherCat2");
//        parser.endObject();
//
//        parser.endArray();
//
//        parser.endObject();
//        Person person = (Person) parser.objectStack.pop();
//        System.out.println(person);
//    }
}
