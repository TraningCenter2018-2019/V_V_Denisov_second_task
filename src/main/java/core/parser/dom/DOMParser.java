package core.parser.dom;

import core.parser.Parser;
import core.parser.TypeReference;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
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

    @Override
    public <T> T parseString(String toParse, Class<T> type)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, ParseException {
        createDOM(toParse);
//        T obj = (T) type.getConstructors()[0].newInstance();
//        return obj;
        return null;
    }

    @Override
    public <T> T parseString(String toParse, TypeReference<T> ref) throws Exception {
        createDOM(toParse);
        return null;
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
