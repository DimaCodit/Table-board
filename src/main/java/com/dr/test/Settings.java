package com.dr.test;

import javafx.scene.control.TableColumn;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Settings {

    private Properties properties;
    private String fileName = "settings.properties";

    public Settings() {
        try {
            //Создаем объект свойст
            properties = new Properties();
            File externalFile = new File("./settings.properties");
            if (externalFile.exists()) {
                properties.load(new FileInputStream(externalFile));
            }
            else {
                properties.load(getClass().getClassLoader().getResourceAsStream(fileName));
            }





        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public Map getColumnsSettings() {
        String columns = properties.getProperty("columns");
        String widths  = properties.getProperty("widths");

        String[] columnsNames = columns.split(";");
        String[] widthsValues = widths.split(";");

        Map result = new HashMap();

        result.put("columnsNames", columnsNames);
        result.put("widthsValues", widthsValues);

        return result;

    }

    public int getPort() {
        int port = 6667;
        String portValue = properties.getProperty("port");
        if (portValue != null){
            port = Integer.valueOf(portValue);
        }
        return port;
    }

    public void setPort(int port) {
        properties.setProperty("port", Integer.toString(port));
    }

    public String getFontSize() {
        String fontSize = properties.getProperty("fontSize");
        if (fontSize == null){
            fontSize = "15";
        }
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        properties.setProperty("fontSize", fontSize);
    }

    public void setColumnsSettings(List<TableColumn> columns) {

        String valueColumns = "";
        String valueWidths   = "";

        for (int i = 1; i < columns.size(); i++) {
            valueColumns = valueColumns + columns.get(i).getText() + ";";
            valueWidths   = valueWidths + columns.get(i).getWidth() + ";";
        }

        valueColumns = StringUtils.chop(valueColumns);
        valueWidths = StringUtils.chop(valueWidths);

        properties.setProperty("columns", valueColumns);
        properties.setProperty("widths", valueWidths);

    }

    public void saveProps(){
        try {

            properties.store(new FileOutputStream(new File("./settings.properties")), null);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
    }

    public String getTheme() {
        String theme = properties.getProperty("theme");
        if (theme == null){
            theme = "#ececec";
        }
        return theme;
    }
}
