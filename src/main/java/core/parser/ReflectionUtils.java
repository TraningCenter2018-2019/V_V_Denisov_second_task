package core.parser;

public class ReflectionUtils {
    private ReflectionUtils(){
    }

    public static String getSetterMethodName(String name) {
        if (name == null || name.length()<1) {
            throw new IllegalArgumentException(name);
        }
        return String.format("set%S%s", name.charAt(0), name.substring(1));
    }
}
