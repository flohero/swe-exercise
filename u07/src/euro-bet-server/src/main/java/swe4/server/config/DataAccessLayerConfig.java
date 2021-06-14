package swe4.server.config;

import swe4.server.ConnectionFactory;

import java.io.IOException;
import java.util.Properties;

public class DataAccessLayerConfig {
    private static final String DATA_ACCESS_PROPERTIES = "dataAccess.properties";
    private static final String IN_MEMORY = "inMemory";
    private static Properties props = null;

    public static boolean isInMemory() {
        return loadDbProperties().get("dataAccessLayer").equals(IN_MEMORY);
    }

    public static Properties loadDbProperties() {
        if (props == null) {
            Properties newProps = new Properties();
            try {
                newProps.load(ConnectionFactory.class.getClassLoader().getResourceAsStream(DATA_ACCESS_PROPERTIES));
                props = newProps;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }
}
