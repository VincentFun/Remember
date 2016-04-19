package com.example.meihui.remember;

import android.content.Context;

import com.example.meihui.remember.model.Vocabulary;
import com.example.meihui.remember.utils.JSONHelper;
import com.example.meihui.remember.utils.MyDatabaseHelper;
import com.example.meihui.remember.utils.URLHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by meihui on 2016/4/13.
 */
public class SyncVocabularyList {
    public SyncVocabularyList(Context context){
        databaseHelper=new MyDatabaseHelper(context,"remember.db",null,2);
    }

    public void syncVocabularyList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1.将本地数据同步到服务器并获取返回结果
                List<Vocabulary> uploadList=databaseHelper.selectVocabularyListByListName("默认生词本");
                String jsonString= JSONHelper.fromVocabularyListToJSONString(uploadList);
                String result="";
                try {
                    result=URLHelper.postDataWithResult(syncUrl,jsonString.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //2.将服务器的返回结果解析成List<Vocabulary>，并插入数据库
                if (result!=null&&!result.equals("")){
                    List<Vocabulary> downloadList=JSONHelper.fromJSONStringToVocabularyList(result);
                    if (downloadList!=null){
                        databaseHelper.insertVocabularyList(downloadList);
                    }
                }
            }
        }).start();

    }

    private static String syncUrl="http://10.138.201.23:8080/MyRememberServer/SyncVocabularyList";
    private MyDatabaseHelper databaseHelper;
}
