package Server;

import algorithms.search.Solution;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configurations {

    public static void configurationsInit() {

        try (OutputStream output = new FileOutputStream("config.properties")) {

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

    public static String loadProperty(String str)
    {
        String propertyValue = "";

        try (InputStream input = Configurations.class.getClassLoader().getResourceAsStream("config.properties"))
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
}