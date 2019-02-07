package core;

import core.parser.Parser;
import core.parser.TypeReference;
import core.parser.dom.DOMParser;
import core.parser.model.Cat;
import core.parser.sax.SAXParser;

import java.io.IOException;

/**
 * ObjectMapper allows you to serialize objects and deserialize json-documents.
 */
public class ObjectMapper {
    /**
     * Uses a parser to deserialize.
     */
    private Parser parser;

    /**
     * Creates ObjectMapper instance.
     * Provides a selection of parser.
     *
     * @param isDOMorSAX true for {@link DOMParser}, false for {@link SAXParser}
     */
    public ObjectMapper(boolean isDOMorSAX) {
        if (isDOMorSAX) {
            parser = new DOMParser();
        } else {
            parser = new SAXParser();
        }
    }

    /**
     * Creates ObjectMapper instance.
     *
     * @param parser parser for deserialization
     */
    public ObjectMapper(Parser parser) {
        this.parser = parser;
    }

    /**
     * Deserializes JSON-string.
     *
     * @param json  string in JSON format
     * @param clazz class of object to create
     * @param <T>   returnable type
     * @return deserialized object
     * @throws Exception in case of a wrong json
     */
    public <T> T readObjectFromJSON(String json, Class<T> clazz) throws Exception {
        return parser.parseString(json, clazz);
    }

    /**
     * Deserializes JSON-string.
     *
     * @param json string in JSON format
     * @param ref  type reference of object to create
     * @param <T>  returnable type
     * @return deserialized object
     * @throws Exception in case of a wrong json
     */
    public <T> T readObjectFromJSON(String json, TypeReference<T> ref) throws Exception {
        return parser.parseString(json, ref);
    }

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper(new DOMParser());
        Cat cat = objectMapper.readObjectFromJSON(ResourceManager.getResourceString("test.json"), Cat.class);
        System.out.println(cat);
    }
}
