package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The class allows you to get resources in the project.
 */
public final class ResourceManager {
    /**
     * Util class has private constructor.
     */
    private ResourceManager() {
    }

    /**
     * Returns the contents of the file as a string. Used to read text files.
     *
     * @param resourceName file name
     * @return file contents
     * @throws IOException file read error
     */
    public static String getResourceString(String resourceName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }

            return stringBuilder.toString();
        }
    }
}
