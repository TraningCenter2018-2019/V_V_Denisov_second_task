package core.parser;

public interface Parser {
    <T> T parseString(String toParse, Class<T> type)
            throws Exception;

    <T> T parseString(String toParse, TypeReference<T> ref)
            throws Exception;
}
