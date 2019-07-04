package be.voeren2000.bezettingsportkampen.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {
    private Properties properties = new Properties();
    
    public PropertiesLoader() {
        setupProperties();
    }
    
    public String getString(String property) {
        return properties.getProperty(property);
    }
    
    private void setupProperties() {
        try (InputStream appConfigPath = new FileInputStream("./application.properties")) {
            properties.load(appConfigPath);
        } catch (IOException e) {
            createProperties();
        }
    }
    
    private void createProperties() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("url","");
        propertiesMap.put("user","");
        propertiesMap.put("password","");
        propertiesMap.put("debug", "false");
        propertiesMap.put("usePermalinkEndpoint","false");
        propertiesMap.put("max_registrations_per_day",String.valueOf(25));
        propertiesMap.put("max_registrations_reached","VOLZET");
        propertiesMap.put("html_container_id","sportkampen");
        addProperty(propertiesMap);
    }
    
    public void addProperty(Map<String,String> propertyMap) {
        try (FileOutputStream fos = new FileOutputStream("./application.properties")) {
            properties.putAll(propertyMap);
            properties.store(fos,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}