package core.parser.dom;

import core.parser.Parser;
import core.parser.ReflectionUtils;
import core.parser.TypeReference;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.*;

/**
 * DOM - Document Object Module. The parser that builds the tree initially.
 */
public class DOMParser implements Parser {
    private String json = null;
    private StringReader reader = null;
    /**
     * Root of DOM. This value is because it can be an array or an object.
     */
    private DOMValue root;

    public static void main(String[] args) throws Exception {
        DOMParser parser = new DOMParser();
//        String json = ResourceManager.getResourceString("test.json");
//        StrangeCat<String, Cat> c = parser.parseString(json, new TypeReference<StrangeCat<String, Cat>>() {
//        });
    }

    @Override
    public <T> T parseString(String toParse, Class<T> type)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ParseException {
        createDOM(toParse);
        T resultObject = type.newInstance();
        DOMObject domObject = root.getObjectValue();
        if (domObject != null) {
            for (DOMProperty property :
                    domObject.getProperties()) {
                String setterName = ReflectionUtils.getSetterMethodName(property.getKey());
                DOMValue domValue = property.getValue();
                if (domValue.getStringValue() != null) {
                    try {
                        Method method = type.getMethod(setterName, String.class);
                        method.invoke(resultObject, domValue.getStringValue());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } else if (domValue.getListValue() != null) {
                    List<Method> methods = getMethodsByName(type, setterName);
                    if (methods.size() == 1) {
                        Method method = methods.get(0);
                        method.invoke(resultObject, createValueTypes(Object.class, domValue.getListValue(), null));
                    }
                } else if (domValue.getObjectValue() != null) {
                    List<Method> methods = getMethodsByName(type, setterName);
                    if (methods.size() == 1) {
                        Method method = methods.get(0);
                        method.invoke(resultObject, createObjectByType(
                                (Class) method.getParameters()[0].getParameterizedType(),
                                null, domValue.getObjectValue()));
                    }
                }
            }
        }
        return resultObject;
    }

    @Override
    public <T> T parseString(String toParse, TypeReference<T> ref) throws Exception {
        createDOM(toParse);
        Class<T> type = (Class<T>) ((ParameterizedType) ref.getType()).getRawType();
        if (Arrays.asList(type.getInterfaces()).contains(List.class) || type == List.class) {
            return (T) createValueTypes(
                    (Class) ((ParameterizedType) ref.getType()).getActualTypeArguments()[0],
                    root.getListValue(),
                    ref);
        }
        T resultObject = type.newInstance();
        DOMObject domObject = root.getObjectValue();
        if (domObject != null) {
            for (DOMProperty property :
                    domObject.getProperties()) {
                String setterName = ReflectionUtils.getSetterMethodName(property.getKey());
                DOMValue domValue = property.getValue();
                List<Method> methods = getMethodsByName(type, setterName);
                if (methods.size() == 0) {
                    throw new Exception(); //todo message
                }
                if (methods.size() == 1) {
                    fillField(methods, domValue, resultObject, ref);
                }
            }
        }
        return resultObject;
    }

    private <R> void fillField(List<Method> methods, DOMValue domValue, Object resultObject, TypeReference<R> ref)
            throws Exception {
        Method m = methods.get(0);
        if (m.getParameterCount() != 1) {
            throw new Exception();//todo message
        }
        if (m.getParameterTypes()[0] == String.class) {
            String stringValue = domValue.getStringValue();
            if (stringValue != null) {
                m.invoke(resultObject, stringValue);
            }
        } else if (Arrays.asList(m.getParameterTypes()[0].getInterfaces()).contains(List.class)
                || m.getParameterTypes()[0] == List.class) {
            List<DOMValue> values = domValue.getListValue();
            Class valueTypes = ref.getTypeByName(
                    ((ParameterizedType) m.getParameters()[0].getParameterizedType()).
                            getActualTypeArguments()[0].getTypeName());
            List valueTypeList = createValueTypes(valueTypes, values, ref);
            m.invoke(resultObject, valueTypeList);
        } else {
            Class valueTypes = ref.getTypeByName(m.getParameters()[0].getParameterizedType().getTypeName());
            if (valueTypes == String.class) {
                String stringValue = domValue.getStringValue();
                if (stringValue != null) {
                    m.invoke(resultObject, stringValue);
                }
            } else {
                Object objectByType = createObjectByType(valueTypes, ref, domValue.getObjectValue());
                m.invoke(resultObject, objectByType);
            }
        }
    }

    private <VT, R> List<VT> createValueTypes(Class<VT> type, List<DOMValue> values, TypeReference<R> ref) {
        List list = new ArrayList();
        if (type == String.class) {
            for (DOMValue value : values) {
                list.add(value.getStringValue());
            }
        } else {
            for (DOMValue value : values) {
                list.add(createObjectByType(type, ref, value.getObjectValue()));
            }
        }
        return list;
    }

    private <VT, R> VT createObjectByType(Class<VT> type, TypeReference<R> ref, DOMObject dom) {
        try {
            Object instance = type.newInstance();
            for (DOMProperty property :
                    dom.getProperties()) {
                List<Method> methods = getMethodsByName(type, ReflectionUtils.getSetterMethodName(property.getKey()));
                if (methods.size() == 0) {
                    throw new Exception();
                } else if (methods.size() == 1) {
                    fillField(methods, property.getValue(), instance, ref);
                }
            }
            return (VT) instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Method> getMethodsByName(Class<?> clazz, String name) {
        List<Method> result = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                result.add(method);
            }
        }
        return result;
    }

    private void createDOM(String toParse) throws IOException, ParseException {
        root = parseString(toParse);
    }

    /**
     * Parses a string (substring) and creates a node with information about it.
     *
     * @return node
     */
    DOMValue parseString(String toParse) throws IOException, ParseException {
        json = toParse.trim();
        root = createDOMValue((char) getReader().read());
        return root;
    }

    private DOMObject createDOMObject() throws IOException, ParseException {
        StringReader stringReader = getReader();
        DOMObject object = new DOMObject();
        List<DOMProperty> properties = new ArrayList<>();
        while (stringReader.ready()) {
            char c = (char) stringReader.read();
            if (shouldContinue(c)) {
                continue;
            }
            switch (c) {
                case '{':
                    break;
                case '}':
                    object.setProperties(properties);
                    return object;
                case '"':
                    properties.add(createDOMProperty());
            }
        }
        return null;
    }

    private DOMProperty createDOMProperty() throws IOException, ParseException {
        DOMProperty property = new DOMProperty();
        StringReader reader = getReader();
        String key = getString();
        property.setKey(key);
        while (reader.ready()) {
            char c = (char) reader.read();
            if (shouldContinue(c)) {
                continue;
            }
            switch (c) {
                case '"':
                    property.setValue(createDOMValue('"'));
                    return property;
                case '{':
                    property.setValue(createDOMValue('{'));
                    return property;
                case '[':
                    property.setValue(createDOMValue('['));
                    return property;
                default:
            }
        }
        return null;
    }

    private DOMValue createDOMValue(char first) throws IOException, ParseException {
        switch (first) {
            case '"':
                return new DOMValue(getString());
            case '{':
                return new DOMValue(createDOMObject());
            case '[':
                return new DOMValue(createListOfDomValues());
            default:
                throw new ParseException("", 0);
        }
    }

    private List<DOMValue> createListOfDomValues() throws IOException, ParseException {
        StringReader stringReader = getReader();
        List<DOMValue> values = new ArrayList<>();
        while (stringReader.ready()) {
            char c = (char) stringReader.read();
            if (shouldContinue(c)) {
                continue;
            }
            switch (c) {
                case ']':
                    return values;
                default:
                    values.add(createDOMValue(c));

            }
        }
        throw new ParseException("", 0);
    }

    private String getString() throws IOException, ParseException {
        StringBuilder stringBuilder = new StringBuilder();
        StringReader reader = getReader();
        boolean isMeta = false;
        while (reader.ready()) {
            char c = (char) reader.read();
            switch (c) {
                case '"':
                    if (isMeta) {
                        stringBuilder.append('"');
                        isMeta = false;
                    } else {
                        return stringBuilder.toString();
                    }
                    break;
                case '\\':
                    if (isMeta) {
                        stringBuilder.append('\\');
                        isMeta = false;
                    } else {
                        isMeta = true;
                    }
                    break;
                default:
                    if (isMeta) {
                        c = escapeChar(c);
                        isMeta = false;
                    }
                    stringBuilder.append(c);
                    break;
            }
        }
        throw new ParseException("", 0);
    }

    private static Map<Character, Character> map = new HashMap<>(7);

    static {
        map.put('b', '\b');
        map.put('f', '\f');
        map.put('n', '\n');
        map.put('r', '\r');
        map.put('t', '\t');
        map.put('"', '\"');
        map.put('\\', '\\');
    }

    private boolean isMetaChar(char c) {
        return map.containsKey(c);
    }

    private char escapeChar(char c) {
        return map.get(c);
    }


    private static Set<Character> set = new HashSet<>(7);

    static {
        set.add(' ');
        set.add(',');
        set.add(':');
        set.add('\n');
        set.add('\t');
        set.add('\r');
    }

    private boolean shouldContinue(char c) {
        return set.contains(c);
    }

    private StringReader getReader() {
        if (reader == null && json != null) {
            reader = new StringReader(json);
        }
        return reader;
    }
}
