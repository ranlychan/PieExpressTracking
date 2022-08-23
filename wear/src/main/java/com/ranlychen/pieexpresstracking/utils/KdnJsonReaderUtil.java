package com.ranlychen.pieexpresstracking.utils;



import com.ranlychen.pieexpresstracking.entity.KdnPackageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class KdnJsonReaderUtil implements JsonReader {

    JSONObject root;

    /**
     * 方 法 名：readJson(String result)
     * 功    能：解析Json格式的字符串并把其中的物流公司编码
     * 参    数：String content
     * 返 回 值：String ShipperCode
     */
    public String readShipperCode(String content) {

        String ShipperCode = null;

        try {
            root = new JSONObject(content);

            if(root.getBoolean("Success")){
                System.out.println("readShipperCode:"+root.toString());
                JSONArray codeArr = root.getJSONArray("Shippers");
                for (int i = 0; i < codeArr.length(); i++) {
                    JSONObject lan = codeArr.getJSONObject(i);
                    ShipperCode = lan.getString("ShipperCode");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ShipperCode;

    }

    /**
     * 方 法 名：readJson(String content)
     * 功    能：解析Json格式的字符串并把相应存储到item类里
     * 参    数：String content
     * 返 回 值：KdnPackageItem KDN_PACKAGE_ITEM
     */
    public KdnPackageItem readJson(String content) {

        KdnPackageItem kdnPackageItem = new KdnPackageItem();
        String[][] traceArr;

        try {
            if(content != null){
                root = new JSONObject(content);

                kdnPackageItem.setName(root.optString("Name"));
                kdnPackageItem.setLogisticCode(root.getString("LogisticCode"));
                kdnPackageItem.setShipperCode(root.getString("ShipperCode"));
                kdnPackageItem.setSuccess(root.getBoolean("Success"));
                kdnPackageItem.setStateCode(root.getString("State"));
                kdnPackageItem.setReason(root.optString("Reason"));
                kdnPackageItem.linkIcon(root.getString("ShipperCode"));
                //KDN_PACKAGE_ITEM.setTraceArr(root.getJSONArray("Traces"));


                /*unused_数组读取法*/
                JSONArray jsArr = root.getJSONArray("Traces");
                traceArr = new String[jsArr.length()][3];

                for (int i = 0; i < jsArr.length(); i++) {

                    JSONObject trace = jsArr.getJSONObject(i);
                    traceArr[i][0] = trace.optString("AcceptTime");
                    traceArr[i][1] = trace.optString("AcceptStation");
                    traceArr[i][2] = trace.optString("Remark");

                }
                kdnPackageItem.setTraceArr(traceArr);

            }//endif


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return kdnPackageItem;

    }


    /**
     * 方 法 名：readExpNos(String str)
     * 功    能：解析Json格式的字符串并把相应运单号存储到String数组result里
     * 参    数：String content
     * 返 回 值：String[] result
     */
    public String[] readExpNos(String content) {

        String[] result = null;

        try {
            if(content == null||content==""){
                result = null;
            }else{
                JSONObject root = new JSONObject(content);
                JSONArray jsonArr = root.getJSONArray("expNos");

                if (jsonArr!=null||jsonArr.getJSONObject(0).optString("expNo")!=null){

                    result = new String[jsonArr.length()];

                    for (int i=0;i<jsonArr.length();i++){
                        JSONObject trace = jsonArr.getJSONObject(i);
                        result[i] = trace.optString("expNo");
                    }
                }
                else{
                    result = null;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 方 法 名：addNewJson(String JsonFileContent, String addString)
     * 功    能：解析Json格式的字符串并增加参数传入的运单号到原有运单号数组里
     * 参    数：String JsonFileContent, String addString
     * 返 回 值：String result
     */
    public String addNewJson(String JsonFileContent, String addString){//null,inputNo

        String result = null;
        JSONObject root;

        try {

            JSONArray jsonArr;
            JSONObject lan = new JSONObject();
            lan.put("expNo", addString);

            if (JsonFileContent == null){
                root = new JSONObject();
                jsonArr = new JSONArray();

            }else{
                root = new JSONObject(JsonFileContent);
                jsonArr = root.getJSONArray("expNos");
            }

            jsonArr.put(lan);
            root.put("expNos", jsonArr);
            result = root.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String deleteJson(String JsonFileContent, int index) {
        JSONObject root = null;

        try{
            root = new JSONObject(JsonFileContent);
            JSONArray jsonArr = root.getJSONArray("expNos");
            jsonArr.remove(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root.toString();
    }

    @Override
    public String updateJson(String JsonFileContent, String newJsonContent) {
        return null;
    }

}
