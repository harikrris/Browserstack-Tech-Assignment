package ConfigReader;

import java.util.ResourceBundle;

public class ConfigReader {
    public static String getKeyFromConfig(String key) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("config");
            return rb.getString(key);
        } catch (Exception e) {
            System.out.println("Invalid path given for config.properties." +e.getMessage());
            return null;
        }
    }
}
