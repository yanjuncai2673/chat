package com.talk.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 安装apk的工具类   apk 打包
 */
public class ApkUtils {

    /**
     * 安装apk
     * @param apkPath
     * @return
     */
    public static boolean install(String apkPath){
        boolean result = false;
        ProcessBuilder processBuilder = null;
        BufferedReader errorStream = null;
        Process process;
        try{
            // 申请su权限  
            String[] args = {"pm","install","-r",apkPath};
            processBuilder = new ProcessBuilder(args);
            process = processBuilder.start();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg ="";
            String line;
            // 读取命令的执行结果  
            while ((line=errorStream.readLine())!=null){
                msg+=line;
            }
            Log.d("TAG","install msg is "+msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功  
            if(!msg.contains("Failure")){
                result = true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(errorStream != null){
                    errorStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

}
