package core.parser;

/**
 * Parser for deserialization.
 */
public interface Parser {
    /**
     * Deserializes JSON-string.
     *
     * @param toParse string in JSON format
     * @param type    class of object to create
     * @param <T>     returnable type
     * @return deserialized object
     * @throws Exception in case of a wrong json
     */
    <T> T parseString(String toParse, Class<T> type)
            throws Exception;

    /**
     * Deserializes JSON-string.
     *
     * @param toParse string in JSON format
     * @param ref     type reference of object to create
     * @param <T>     returnable type
     * @return deserialized object
     * @throws Exception in case of a wrong json
     */
    <T> T parseString(String toParse, TypeReference<T> ref)
            throws Exception;
}
