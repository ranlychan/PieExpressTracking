package com.ranlychen.pieexpresstracking.utils;

import android.content.Context;
import android.util.Log;

import com.ranlychen.pieexpresstracking.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

public class DataIOUtil {

    private static Log XLog;
    Context context;

    public DataIOUtil(Context context){
        this.context = context;
    }

    /**
     * 方法名：saveToLocal(String content,String expNo)
     * 功    能：创建文件夹,保存json到本地
     * 参    数：String content，需要保存的内容 ; String expNo，传入的单号或代码，用于标记文件名
     * 特    性：当expNo为正常单号时，将输入的content保存为itemdata+expNo+.json; expNo为null时将输入的content保存为expNos.json
     * 返回值：String
     */
    public String saveToLocal(String content,String expNo) {

        Boolean success;
        File file;

        //文件夹路径
        File dir = new File(context.getFilesDir() + context.getString(R.string.folderpath));////Environment.getExternalStorageDirectory()
        //文件名
        String fileName = context.getString(R.string.filename)+ expNo +context.getString(R.string.filetail);
        String fileName_expNos = context.getString(R.string.filename_expNo) + context.getString(R.string.filetail);

        try {
            //文件夹不存在和传入的value值为1时，才允许进入创建
            if (!dir.exists()) {
                //创建文件夹
                dir.mkdirs();
                XLog.i("PieExpressTracking_folder_write", context.getString(R.string.folderpath)+"文件夹不存在，已新建");
               // return file.getPath();
            }


            if (expNo == null) {
                file = new File(dir, fileName_expNos);
            }
            else{
                file = new File(dir, fileName);
            }

            if(!file.exists()){
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();
                XLog.i("PieExpressTracking_file_write", "保存"+file.getName()+"成功 path:" + file.getPath());

                success=true;
            }
            else{
                XLog.i("PieExpressTracking_file_rewrite", "保存"+file.getName()+"成功 文件已存在但已覆写 path:" + file.getPath());
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes("UTF-8"));
                out.close();
                success=true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            XLog.i("PieExpressTracking_file_write", "保存失败");
            success=false;
        }
        return String.valueOf(success);
    }

    /**
     * 方法名：openJsonFile(String filePath)
     * 功    能：从本地读取json
     * 参    数：String filePath
     * 返回值：String result
     */
    public  String openJsonFile(String filePath){

        String result=null;
        try {
            File file = new File(context.getFilesDir()+filePath);//Environment.getExternalStorageDirectory()
            if (file.exists()){
                InputStream input=new FileInputStream(file);
                Reader reader=new InputStreamReader(input);
                StringBuffer stringBuffer=new StringBuffer();
                char b[]=new char[1024];
                int len=-1;
                try {
                    while ((len = reader.read(b))!= -1){
                        stringBuffer.append(b);
                    }
                }catch (IOException e){
                    Log.e("ReadingFile","IOException");
                }
                result = stringBuffer.toString();

            }
            else{
                result = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}

