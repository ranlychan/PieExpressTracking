package com.ranlychen.pieexpresstracking;

import org.json.JSONObject;

public interface JsonReader {
    JSONObject root = null;
    Item item = new Item();

    Item readJson(String content);
    String addNewJson(String JsonFileContent, String addNewString);
    String deleteJson(String JsonFileContent, int index);
    String updateJson(String JsonFileContent, String newJsonContent);

}
