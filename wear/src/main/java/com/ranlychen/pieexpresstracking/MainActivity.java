package com.ranlychen.pieexpresstracking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mobvoi.android.speech.SpeechRecognitionApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends SpeechRecognitionApi.SpeechRecogActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final int ADDCOMPLETED = 0;
    private static final int UNSUPPORTEDNO = 1;
    private static final int ADDFAIL = 2;
    private static final int UPDATECOMPLETED = 3;
    private static final int UPDATEFAIL = 4;
    private static final int DELETECOMPLETED = 5;
    private static final int DELETEFAIL = 6;
    private static final int UNKNOWNERR = 7;

    private static final int NAME = 1;
    private static final int NO = 2;

    boolean isfirst = true;
    String[] names;
    String[] expInfos;
    String[] expNos;
    int[] imgIds;
    Item[] items;

    SimpleAdapter myAdapter;
    List<Map<String, Object>> listitem;

    View headView;
    View footView;
    ListView listView;

    Intent aboutpage;
    Intent dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LayoutInflater inflater = LayoutInflater.from(this);
        headView = inflater.inflate(R.layout.view_header, null, false);
        footView = inflater.inflate(R.layout.view_footer, null, false);
        aboutpage = new Intent(MainActivity.this, AppInfoActivity.class);
        listView = findViewById(R.id.list_test);
        listView.addHeaderView(headView);
        listView.addFooterView(footView);
        listView.setOnItemClickListener(this);

        listitem = new ArrayList<Map<String, Object>>();

        updateView();
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            switch(msg.what){

                case ADDCOMPLETED:
                    updateView();
                    Toast.makeText(MainActivity.this,getString(R.string.addsuccess),Toast.LENGTH_SHORT).show();
                    break;
                case UNSUPPORTEDNO:
                    Toast.makeText(MainActivity.this,getString(R.string.unsupportedNo),Toast.LENGTH_LONG).show();
                    break;
                case ADDFAIL:
                    Toast.makeText(MainActivity.this,getString(R.string.addfail),Toast.LENGTH_LONG).show();
                    break;
                case UPDATECOMPLETED:
                    updateView();
                    Toast.makeText(MainActivity.this,getString(R.string.updatesuccess),Toast.LENGTH_LONG).show();
                    break;
                case UPDATEFAIL:
                    Toast.makeText(MainActivity.this,getString(R.string.updatefail),Toast.LENGTH_LONG).show();
                    break;
                case DELETECOMPLETED:
                    updateView();
                    Toast.makeText(MainActivity.this,getString(R.string.deletesuccess),Toast.LENGTH_LONG).show();
                    break;
                case DELETEFAIL:
                    Toast.makeText(MainActivity.this,getString(R.string.deletefail),Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this,getString(R.string.UNKNOWNERROR),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void updateView(){
        loadLocalDatatoMap();
        updateAdapter();
    }
    /**
     * 此处可以另写一个抽象类或接口
     */

    private void loadLocalDatatoMap(){

        DataIO io = new DataIO(MainActivity.this);
        KdnJsonReader rh = new KdnJsonReader();

        /**
         * 读取本地运单号索引,无则数据为空
         */

        String index = io.openJsonFile(this.getString(R.string.folderpath)+this.getString(R.string.filename_expNo)+this.getString(R.string.filetail));

        if (index == null){
            //System.out.println("index is empty");
            expNos = null;
        }else{
            expNos = rh.readExpNos(index);
        }

        /**
         * 数据对接
         */

        if(expNos!=null && expNos.length>0){
            names = new String[expNos.length];
            expInfos = new String[expNos.length];
            imgIds = new int[expNos.length];

            items = new Item[expNos.length];
            for(int i = 0; i < expNos.length ; i++){
                String filepath =this.getString(R.string.folderpath)+this.getString(R.string.filename)+expNos[i]+this.getString(R.string.filetail);
                String data = io.openJsonFile(filepath);
                items[i] = rh.readJson(data);
            }

            for(int i = 0;i < expNos.length;i++){
                names[i] = items[i].getName();
                imgIds[i] = items[i].getIcon();
                expInfos[i] = items[i].getNewestTrace();
            }
        } else
        {
            names = new String[]{};
            expInfos = new String[]{};
            imgIds = new int[]{};
            expNos = new String[]{};
        }

        /**
         *快递信息更新
         */
        listitem.clear();

        int len = 0;
        if (expNos==null){
            len = 0;
        }else{
            len = expNos.length;
        }
        for (int i = 0; i < len; i++) {
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("comIcon", imgIds[i]);
            showitem.put("name", names[i]);
            showitem.put("expNos", expNos[i]);
            showitem.put("expInfos", expInfos[i]);
            listitem.add(showitem);
        }
    }

    private void updateAdapter() {

        if(isfirst){
            isfirst = false;
            myAdapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.list_item, new String[]{"comIcon", "name", "expNos","expInfos"}, new int[]{R.id.comIcon, R.id.name, R.id.expNum,R.id.expInfo});
            listView.setAdapter(myAdapter);
        }else{
            myAdapter.notifyDataSetChanged();
        }
        listView.invalidate();//如不重绘listView会出现一个奇葩的现象：数据刷新完以后需要滑动屏幕才会显示出来
        Toast.makeText(MainActivity.this, "加载成功！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 检测点击或是长按了哪一个item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final DetailDialog detaildialog = new DetailDialog(MainActivity.this, position,items[position-1]);
        detaildialog.setDelete(new DetailDialog.IOnCancelListener() {
            @Override
            public void onDelete(int state) {
                Message msg = new Message();
                msg.what = state;
                handler.sendMessage(msg);
            }
        });
        detaildialog.setUpdate(new DetailDialog.IOnConfirmListener() {
            @Override
            public void onUpdate(int state) {
                Message msg = new Message();
                msg.what = state;
                handler.sendMessage(msg);
            }
        });
        detaildialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.putExtra("target","AllMainActivity");
        dialog.putExtra("text", this.getResources().getString(R.string.deleteText));
        startActivity(dialog);
        finish();
        return true;
    }

    /**
     * 不用监听器实现按钮点击事件监听
     * 添加快递信息按钮
     */
    AddItemDialog additem;
    int editviewname;
    public void footadd(View v) {
        additem = new AddItemDialog(MainActivity.this);
        additem.setCancel(new AddItemDialog.IOnCancelListener() {
            @Override
            public void onCancel(AddItemDialog dialog) {
                Toast.makeText(MainActivity.this,getString(R.string.addfail),Toast.LENGTH_SHORT).show();
            }
        });
        additem.setConfirm(new AddItemDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(int state) {
                Message msg = new Message();
                msg.what = state;
                handler.sendMessage(msg);
            }
        });
        additem.setVoiceListenerForName(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("VoiceInputStart,Name");
                startVoiceInput();
                editviewname = NAME;
            }
        });
        additem.setVoiceListenerForNo(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("VoiceInputStart,No");
                startVoiceInput();
                editviewname = NO;
            }
        });
        additem.show();

    }

    public void footUpDate(View v) {
        updateView();
        Toast.makeText(MainActivity.this, "刷新成功！",Toast.LENGTH_SHORT).show();
    }

    public void logo(View v) {
        startActivity(aboutpage);
    }

    @Override
    public void onRecognitionSuccess(String s) {
        additem.setEditViewText(editviewname,s);
    }

    @Override
    public void onRecognitionFailed() {

    }
}