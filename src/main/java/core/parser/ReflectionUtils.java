package core.parser;

/**
 * Tools to simplify work with reflection.
 */
public final class ReflectionUtils {
    /**
     * Utility classes should not have a public or default constructor.
     */
    private ReflectionUtils() {
    }

    /**
     * Get the name of the setter for the field.
     *
     * @param name name of the field
     * @return name of the setter
     */
    public static String getSetterMethodName(String name) {
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException(name);
        }
        return String.format("set%S%s", name.charAt(0), name.substring(1));
    }
}
