package testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResourceManager {
    private ResourceManager() {
    }

    public static String getResourceString(String resourceName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (bufferedReader.ready())
                stringBuilder.append(bufferedReader.readLine());

            return stringBuilder.toString();
        }
    }
}
