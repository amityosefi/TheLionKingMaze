package Server;

import algorithms.search.Solution;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class Configurations {

    public static void configurationsInit() {


    }


    public static String loadProperty(String str) {
        Properties prop = new Properties();

        try {
            // the configuration file name
            String fileName = "config.properties";
            ClassLoader classLoader = Configurations.class.getClassLoader();

            // Make sure that the configuration file exists
            URL res = Objects.requireNonNull(classLoader.getResource(fileName),
                    "Can't find configuration file app.config");

            InputStream is = new FileInputStream(res.getFile());

            // load the properties file
            prop.load(is);

        } catch (IOException e) {
            System.out.println("didnt work");
        }
        return prop.getProperty(str);
    }
}