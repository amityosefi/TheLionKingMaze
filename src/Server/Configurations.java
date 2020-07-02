package Server;

import algorithms.search.Solution;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

public class Configurations {

    public static void configurationsInit() {

        try (OutputStream output = new FileOutputStream("Resources/config.properties")) {

            Properties prop = new Properties();

            // set the config.properties value
            prop.setProperty("Generate", "MyMazeGenerator");
            prop.setProperty("Search", "BreadthFirstSearch");
            prop.setProperty("ThreadPool", String.valueOf(5));

            // save config.properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /*
        public static String loadProperty(String str)
        {
            String propertyValue = "";

            try (InputStream input = Configurations.class.getClassLoader().getResourceAsStream("Resources/config.properties"))
            {
                if (input == null) {
                    System.out.println("Sorry, unable to find config.properties");
                    return null;
                }

                //load a config.properties file from class path, inside static method
                Properties prop = new Properties();
                prop.load(input);
                propertyValue = prop.getProperty(str);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return propertyValue;
        }

     */
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