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
 * DOM - Document Object Model. The parser that builds the tree initially.
 */
public class DOMParser implements Parser {
    /**
     * Parsing json.
     */
    private String json = null;

    /**
     * Json reader.
     */
    private StringReader reader = null;

    /**
     * Root of DOM. This value is because it can be an array or an object.
     */
    private DOMValue root;

    /**
     * Exception message.
     */
    private static final String SETTER_IS_NOT_FOUND = "The setter for the field was not found.";

    /**
     * Exception message.
     */
    private static final String TOO_MANY_SETTERS = "Too many setters were found for the field.";

    @Override
    public <T> T parseString(String toParse, Class<T> type)
            throws Exception {
        reader = null;
        createDOM(toParse);
        T resultObject = type.newInstance();
        DOMObject domObject = root.getObjectValue();
        if (domObject != null) {
            for (DOMProperty property : domObject.getProperties()) {
                createValueAndSaveItToField(resultObject, property, type);
            }
        }
        return resultObject;
    }

    @Override
    public <T> T parseString(String toParse, TypeReference<T> ref) throws Exception {
        reader = null;
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
            for (DOMProperty property : domObject.getProperties()) {
                createValueAndSaveItToField(resultObject, property, type, ref);
            }
        }
        return resultObject;
    }

    /**
     * Creates an object of a specific type and saves it using the setter of a specific name.
     *
     * @param resultObject the object in which the string will be saved
     * @param property     property to be stored in object
     * @param type         the type of object whose setter will be used
     * @param <T>          object type
     * @throws Exception in case of an error while creating the object
     */
    private <T> void createValueAndSaveItToField(T resultObject, DOMProperty property,
                                                 Class<T> type)
            throws Exception {
        String setterName = ReflectionUtils.getSetterMethodName(property.getKey());
        DOMValue domValue = property.getValue();
        if (domValue.getStringValue() != null) {
            setStringToField(resultObject, type, domValue, setterName);
        } else if (domValue.getListValue() != null) {
            createListOfObjectsAndSetItToField(resultObject, type, domValue, setterName);
        } else if (domValue.getObjectValue() != null) {
            createObjectAndSetItToField(resultObject, type, domValue, setterName);
        }
    }

    /**
     * Creates an object of a specific type and saves it using the setter of a specific name.
     *
     * @param resultObject the object in which the string will be saved
     * @param property     property to be stored in object
     * @param type         the type of object whose setter will be used
     * @param ref          object type information
     * @param <T>          object type
     * @throws Exception in case of an error while creating the object
     */
    private <T> void createValueAndSaveItToField(T resultObject, DOMProperty property,
                                                 Class<T> type, TypeReference<T> ref)
            throws Exception {
        String setterName = ReflectionUtils.getSetterMethodName(property.getKey());
        DOMValue domValue = property.getValue();
        List<Method> methods = getMethodsByName(type, setterName);
        if (methods.size() == 0) {
            throw new Exception(SETTER_IS_NOT_FOUND);
        }
        if (methods.size() == 1) {
            fillField(methods, domValue, resultObject, ref);
        }
    }

    /**
     * Saves string using the setter of a specific name.
     *
     * @param resultObject the object in which the string will be saved
     * @param type         the type of object whose setter will be used
     * @param domValue     data for the new object
     * @param setterName   object setter name
     * @param <T>          object type
     * @throws Exception in case of an error while creating the object
     */
    private <T> void setStringToField(T resultObject, Class<T> type, DOMValue domValue, String setterName)
            throws Exception {
        try {
            Method method = type.getMethod(setterName, String.class);
            method.invoke(resultObject, domValue.getStringValue());
        } catch (NoSuchMethodException ignored) {
            throw new Exception(SETTER_IS_NOT_FOUND);
        }
    }

    /**
     * Creates an object of a specific type and saves it using the setter of a specific name.
     *
     * @param resultObject the object in which the created object will be saved
     * @param type         the type of object whose setter will be used
     * @param domValue     data for the new object
     * @param setterName   object setter name
     * @param <T>          object type
     * @throws Exception in case of an error while creating the object
     */
    private <T> void createObjectAndSetItToField(T resultObject, Class<T> type, DOMValue domValue, String setterName)
            throws Exception {
        List<Method> methods = getMethodsByName(type, setterName);
        if (methods.size() == 1) {
            Method method = methods.get(0);
            method.invoke(resultObject, createObjectByType(
                    (Class) method.getParameters()[0].getParameterizedType(),
                    null, domValue.getObjectValue()));
        }
    }

    /**
     * Creates a list of objects of a specific type and saves it using the setter of a specific name.
     *
     * @param resultObject the object in which the created object will be saved
     * @param type         the type of object whose setter will be used
     * @param domValue     data for the list of objects
     * @param setterName   object setter name
     * @param <T>          object type
     * @throws Exception in case of an error while creating the object
     */
    private <T> void createListOfObjectsAndSetItToField(T resultObject,
                                                        Class<T> type, DOMValue domValue, String setterName)
            throws Exception {
        List<Method> methods = getMethodsByName(type, setterName);
        if (methods.size() == 1) {
            Method method = methods.get(0);
            method.invoke(resultObject, createValueTypes(Object.class, domValue.getListValue(), null));
        }
    }

    /**
     * Writes a value to the field.
     *
     * @param methods      list of methods defined by name as setters we need
     * @param domValue     value to write
     * @param resultObject the object to write the value to
     * @param ref          object type information
     * @param <R>          object type
     * @throws Exception if the setter for the field was not found
     */
    private <R> void fillField(List<Method> methods, DOMValue domValue, Object resultObject, TypeReference<R> ref)
            throws Exception {
        Method m = methods.get(0);
        if (m.getParameterCount() != 1) {
            if (m.getParameterCount() > 1) {
                throw new Exception(TOO_MANY_SETTERS);
            } else {
                throw new Exception(SETTER_IS_NOT_FOUND);
            }
        }
        if (m.getParameterTypes()[0] == String.class) {
            setStringValueUsingSetter(resultObject, domValue, m);
        } else if (Arrays.asList(m.getParameterTypes()[0].getInterfaces()).contains(List.class)
                || m.getParameterTypes()[0] == List.class) {
            setListValueUsingSetter(resultObject, domValue, m, ref);
        } else {
            Class valueTypes = ref.getTypeByName(m.getParameters()[0].getParameterizedType().getTypeName());
            if (valueTypes == String.class) {
                setStringValueUsingSetter(resultObject, domValue, m);
            } else {
                Object objectByType = createObjectByType(valueTypes, ref, domValue.getObjectValue());
                m.invoke(resultObject, objectByType);
            }
        }
    }

    /**
     * Sets list value to object using setter method.
     *
     * @param resultObject the object in which the created list will be saved
     * @param domValue     data for the list of objects
     * @param setter       object setter
     * @param ref          information about dynamic object types
     * @param <T>          the type of object whose setter will be used
     * @throws Exception in case of an error while creating the object
     */
    private <T> void setListValueUsingSetter(Object resultObject, DOMValue domValue, Method setter, TypeReference<T> ref)
            throws Exception {
        List<DOMValue> values = domValue.getListValue();
        Class valueTypes = ref.getTypeByName(
                ((ParameterizedType) setter.getParameters()[0].getParameterizedType()).
                        getActualTypeArguments()[0].getTypeName());
        List valueTypeList = createValueTypes(valueTypes, values, ref);
        setter.invoke(resultObject, valueTypeList);
    }

    /**
     * Sets string value to object using setter method.
     *
     * @param resultObject the object in which the string will be saved
     * @param domValue     data for the object
     * @param setter       object setter
     * @param <T>          the type of object whose setter will be used
     * @throws Exception in case of an error while creating the object
     */
    private <T> void setStringValueUsingSetter(T resultObject, DOMValue domValue, Method setter)
            throws Exception {
        String stringValue = domValue.getStringValue();
        if (stringValue != null) {
            setter.invoke(resultObject, stringValue);
        }
    }

    /**
     * Creates a list of objects of a certain type.
     *
     * @param type   type of objects
     * @param values object values
     * @param ref    information about dynamic object types
     * @param <VT>   object type
     * @param <R>    dynamic type
     * @return list of objects
     * @throws Exception in case of an error while creating the object
     */
    private <VT, R> List<VT> createValueTypes(Class<VT> type, List<DOMValue> values, TypeReference<R> ref)
            throws Exception {
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


    /**
     * Creates an object of a certain type.
     *
     * @param type type of object
     * @param ref  information about dynamic object type
     * @param dom  object value
     * @param <VT> object type
     * @param <R>  dynamic type
     * @return created object
     * @throws Exception in case of an error while creating the object
     */
    private <VT, R> VT createObjectByType(Class<VT> type, TypeReference<R> ref, DOMObject dom) throws Exception {
        try {
            Object instance = type.newInstance();
            for (DOMProperty property : dom.getProperties()) {
                List<Method> methods = getMethodsByName(type, ReflectionUtils.getSetterMethodName(property.getKey()));
                if (methods.size() == 0) {
                    throw new Exception(SETTER_IS_NOT_FOUND);
                } else if (methods.size() == 1) {
                    fillField(methods, property.getValue(), instance, ref);
                }
            }
            return (VT) instance;
        } catch (Exception e) {
            throw new Exception("Error creating object", e);
        }
    }

    /**
     * Get a method from a class by its name.
     *
     * @param clazz the class in which we are looking for a method
     * @param name  method name
     * @return list of found methods
     */
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

    /**
     * Creates a model from string.
     *
     * @param toParse json-string for parsing
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
    private void createDOM(String toParse) throws IOException, ParseException {
        root = parseString(toParse);
    }

    /**
     * Parses a string and creates a node with information about it.
     *
     * @param toParse json-string for parsing
     * @return node
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
    DOMValue parseString(String toParse) throws IOException, ParseException {
        json = toParse.trim();
        root = createDOMValue((char) getReader().read());
        return root;
    }

    /**
     * Creates an object in the model.
     *
     * @return object
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
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
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * Creates a property in a model.
     *
     * @return property
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
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

    /**
     * Creates value in model.
     *
     * @param first first character of new value
     * @return new value
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
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

    /**
     * Creates a list of values in the model.
     *
     * @return list of values
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
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

    /**
     * Read the next string from the document.
     *
     * @return next string
     * @throws IOException    some I/O errors
     * @throws ParseException document parsing error
     */
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

    /**
     * The number of meta characters in json.
     */
    private static final int NUMBER_OF_META_CHARS_IN_JSON = 7;

    /**
     * A dictionary of the key-value form, where the key is a character
     * following in the line after the meta-symbol (slash), and value is
     * the character obtained by combining the slash and key.
     */
    private static Map<Character, Character> escapeCharsMap = new HashMap<>(NUMBER_OF_META_CHARS_IN_JSON);

    static {
        escapeCharsMap.put('b', '\b');
        escapeCharsMap.put('f', '\f');
        escapeCharsMap.put('n', '\n');
        escapeCharsMap.put('r', '\r');
        escapeCharsMap.put('t', '\t');
        escapeCharsMap.put('"', '\"');
        escapeCharsMap.put('\\', '\\');
    }

    /**
     * Checks if the value of a character can change if there is a slash in the line before it.
     *
     * @param c checked character
     * @return true - if it is a meta-symbol, otherwise - false
     */
    private boolean isMetaChar(char c) {
        return escapeCharsMap.containsKey(c);
    }

    /**
     * Returns a meta-character obtained by concatenating a slash and a passed character.
     *
     * @param c character
     * @return meta-character  or null if it is not a meta-character
     */
    private char escapeChar(char c) {
        return escapeCharsMap.get(c);
    }


    /**
     * The number of characters to skip when reading.
     */
    private static final int NUMBER_OF_ESCAPE_CHARS_IN_JSON = 6;

    /**
     * Many characters to skip when reading.
     */
    private static Set<Character> setOfCharactersToSkip = new HashSet<>(NUMBER_OF_ESCAPE_CHARS_IN_JSON);

    static {
        setOfCharactersToSkip.add(' ');
        setOfCharactersToSkip.add(',');
        setOfCharactersToSkip.add(':');
        setOfCharactersToSkip.add('\n');
        setOfCharactersToSkip.add('\t');
        setOfCharactersToSkip.add('\r');
    }

    /**
     * Should we continue reading.
     *
     * @param c read character
     * @return true if it was a whitespace character
     */
    private boolean shouldContinue(char c) {
        return setOfCharactersToSkip.contains(c);
    }

    /**
     * Returns the reader to read the document.
     * Creates a new one if it was not created.
     *
     * @return reader to read json
     * @throws IOException json hasn't been set
     */
    private StringReader getReader() throws IOException {
        if (reader == null && json != null) {
            reader = new StringReader(json);
        }
        if (reader == null) {
            throw new IOException("json hasn't been set");
        } else {
            return reader;
        }
    }
}
