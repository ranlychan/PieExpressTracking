package com.ranlychen.pieexpresstracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Item implements Serializable,JsonReader {
    private String Name;
    private int Icon;
    private String OrderCode;
    private String ShipperCode;
    private String LogisticCode;
    private Boolean Success;
    private String Reason;
    private String State;
    private Boolean Remembered;
    private String[][] TraceArr;
    //private JSONArray Traces;

    /**
    *get methods
     */
    String getName(){
        return Name;
    }

    int getIcon(){
        return  Icon;
    }

    String getOrderCode(){
        return OrderCode;
    }

    String getShipperCode(){
        return ShipperCode;
    }

    String getLogisticCode(){
        return LogisticCode;
    }

    Boolean isSuccess(){
        return Success;
    }

    String getReason(){
        return Reason;
    }

    String getStateCode(){
        return State;
    }

    String getState(){
        String State_Str;
        switch(this.getStateCode()){
            case "2": State_Str = "运输中";break;
            case "3": State_Str = "已签收";break;
            case "4": State_Str = "问题件";break;
            default:State_Str = "未知问题";break;
        }
        return State_Str;
    }

    Boolean isRembered(){
        return Remembered;
    }

    String[][] getTraces(){
        return  TraceArr;
    }

    /**
     *set methods
     */
    void setName(String Name){
       this.Name=Name;
    }

    void setIcon(int Icon){
        this.Icon=Icon;
    }

    void setOrderCode(String OrderCode){
        this.OrderCode=OrderCode;
    }

    void setShipperCode(String ShipperCode){
        this.ShipperCode=ShipperCode;
    }

    void setLogisticCode(String LogisticCode){
        this.LogisticCode=LogisticCode;
    }

    void setSuccess(Boolean Success){
        this.Success=Success;
    }

    void setReason(String Reason){
        this.Reason=Reason;
    }

    void setStateCode(String State){
        this.State=State;
    }

    void setRemembered(Boolean Rembered){ this.Remembered =Rembered; }

    void setTraceArr(String[][] TraceArr){
        this.TraceArr=TraceArr;
    }

    String getNewestTrace(){
        String newtrace;
        if(this.getStateCode().equals("0")){
            newtrace =this.getReason();
        }
        else{
            String[][] tarr = this.getTraces();
            newtrace = this.getState() +" "+ tarr[tarr.length-1][0]+" "+tarr[tarr.length-1][1] +" "+ tarr[tarr.length-1][2];
        }
        return newtrace;
    }

    void linkIcon(String ShipperCode){
        switch (ShipperCode){
            case "STO": this.setIcon(R.drawable.com_sto);
            break;
            case "YTO": this.setIcon(R.drawable.com_yto);
            break;
            case "ZTO": this.setIcon(R.drawable.com_zto);
            break;
            default: this.setIcon(R.drawable.ic_full_cancel);
                break;
        }
    }

    JSONObject root;

    @Override
    public Item readJson(String content) {
        String[][] traceArr;
        try {
            if(content != null){
                root = new JSONObject(content);

                setName(root.optString("Name"));
                setLogisticCode(root.getString("LogisticCode"));
                setShipperCode(root.getString("ShipperCode"));
                setSuccess(root.getBoolean("Success"));
                setStateCode(root.getString("State"));
                setReason(root.optString("Reason"));
                linkIcon(root.getString("ShipperCode"));

                /*unused_数组读取法*/
                JSONArray jsArr = root.getJSONArray("Traces");
                traceArr = new String[jsArr.length()][3];

                for (int i = 0; i < jsArr.length(); i++) {
                    JSONObject trace = jsArr.getJSONObject(i);
                    traceArr[i][0] = trace.optString("AcceptTime");
                    traceArr[i][1] = trace.optString("AcceptStation");
                    traceArr[i][2] = trace.optString("Remark");
                }
                setTraceArr(traceArr);
                System.out.println(this.getNewestTrace());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String addNewJson(String JsonFileContent, String addNewString) {
        return null;
    }

    @Override
    public String deleteJson(String JsonFileContent, int index) {
        return null;
    }

    @Override
    public String updateJson(String JsonFileContent, String newJsonContent) {
        return null;
    }
}
