package com.rest.api.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vbarros on 16/09/2019.
 */
public class ConfigurationLoader {


    public static String getPropValue(String parameter) {
        InputStream inputStream = null;
        String result = "";
        try {

            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = ConfigurationLoader.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
                result = prop.getProperty(parameter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
}