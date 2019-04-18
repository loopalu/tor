package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private int port;
    private int timeToLive;
    private double lazyness;
    private int timeToWaitUrl;

    /**
     * Reads config.properties file and gets the data
     *
     * @param propertyFile The name of config.properties file
     * @throws IOException The exception when file is not found
     */
    public PropertyReader(String propertyFile) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFile);
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propertyFile + "' not found in the classpath");
        }
        this.port = Integer.parseInt(properties.getProperty("port"));
        this.timeToLive = Integer.parseInt(properties.getProperty("timeToLive"));
        this.lazyness = Double.parseDouble(properties.getProperty("lazyness"));
        this.timeToWaitUrl = Integer.parseInt(properties.getProperty("timeToWaitUrl"));
    }

    public int getPort() {
        return port;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public double getLazyness() {
        return lazyness;
    }

    public int getTimeToWaitUrl() {
        return timeToWaitUrl;
    }
}
