package core.parser;

public interface Parser<T> {
    T parseString(String toParse, Class<T> type);
}
