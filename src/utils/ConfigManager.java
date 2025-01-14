package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ConfigManager {
    
    // 1) Singleton instance
    private static ConfigManager instance;

    // 2) Fields to store config values
    private String webserviceUrl;
    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;

    // 3) Private constructor (to enforce singleton usage)
    private ConfigManager() {
        loadConfig();
    }

    /**
     * Public method to get the singleton instance.
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Parse config.xml from the classpath and populate the fields.
     */
    private void loadConfig() {
        try (InputStream is = getClass().getResourceAsStream("/config.xml")) {
            if (is == null) {
                throw new RuntimeException("Could not find config.xml on the classpath");
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            // Retrieve <webserviceUrl> value
            this.webserviceUrl = doc.getElementsByTagName("webserviceUrl")
                                    .item(0)
                                    .getTextContent();

            // Retrieve <database> values
            NodeList dbNodeList = doc.getElementsByTagName("database");
            if (dbNodeList != null && dbNodeList.getLength() > 0) {
                Element dbElement = (Element) dbNodeList.item(0);

                this.databaseUrl = dbElement.getElementsByTagName("url")
                                            .item(0)
                                            .getTextContent();

                this.databaseUser = dbElement.getElementsByTagName("username")
                                             .item(0)
                                             .getTextContent();

                this.databasePassword = dbElement.getElementsByTagName("password")
                                                 .item(0)
                                                 .getTextContent();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load or parse config.xml", e);
        }
    }

    // 4) Getter methods
    public String getWebserviceUrl() {
        return webserviceUrl;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}
