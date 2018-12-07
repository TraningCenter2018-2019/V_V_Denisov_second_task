package core.parser.sax;

import core.parser.Parser;
import core.parser.TypeReference;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SAXParser implements Parser {
    private Stack<Object> objectStack = new Stack<>();
    private Stack<String> methodNameStack = new Stack<>();
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
        if (clazz == List.class)
            clazz = ArrayList.class;
        T obj = (T) clazz.newInstance();
        return obj;
    }

    private void startObject(Class<?> clazz)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] all = clazz.getConstructors();
        Constructor<?> constructor =
                (Constructor<?>) Arrays.stream(all).filter(x -> x.getParameterCount() == 0).toArray()[0];
        objectStack.push(constructor.newInstance());
    }

    private void endObject() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!methodNameStack.empty()) {
            endProperty(objectStack.pop());
        }
    }

    private void startProperty(String key) throws NoSuchMethodException {
        String setterNamePattern = "set%S%s";
        String setterName = String.format(setterNamePattern, key.charAt(0), key.substring(1));
        methodNameStack.push(setterName);
    }

    private void endProperty(Object value)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method setter = getMethod(objectStack.peek(), methodNameStack.pop(), value.getClass());
        setter.invoke(objectStack.peek(), value);
    }

    private void startArray() {
        listStack.push(new ArrayList());
        objectStack.push(listStack.peek());
    }

    private void endArray() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (!methodNameStack.empty()) {
            endProperty(objectStack.pop());
        }
    }

    private void startArrayValue() {
        methodNameStack.push("add");
    }

    private Method getMethod(Object object, String name, Class<?> paramType) throws NoSuchMethodException {
        try {
            return object.getClass().getMethod(name, paramType);
        } catch (NoSuchMethodException ignored) {
            if (paramType == ArrayList.class)
                return getMethod(object, name, List.class);
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
