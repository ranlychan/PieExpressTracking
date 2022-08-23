package com.ranlychen.pieexpresstracking.utils;

import com.ranlychen.pieexpresstracking.entity.KdnPackageItem;

import org.json.JSONObject;

public interface JsonReader {
    JSONObject root = null;
    KdnPackageItem KDN_PACKAGE_ITEM = new KdnPackageItem();

    KdnPackageItem readJson(String content);
    String addNewJson(String JsonFileContent, String addNewString);
    String deleteJson(String JsonFileContent, int index);
    String updateJson(String JsonFileContent, String newJsonContent);

}
