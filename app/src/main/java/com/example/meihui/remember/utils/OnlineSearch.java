package com.example.meihui.remember.utils;


import android.util.Log;

import com.example.meihui.remember.model.Dict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by meihui on 2016/4/5.
 */
public class OnlineSearch {

    //金山词霸查词API
    private static String JINSHAN_URL = "http://dict-co.iciba.com/api/dictionary.php?";
    private static String JINSHAN_KEY = "9E1ABBA17D2C1B00E8ED8DD82F22218F";

    //有道词典翻译API
    private static String YOUDAO_URL="http://fanyi.youdao.com/openapi.do?keyfrom=RememberMeihui&key=1351914136&type=data&doctype=json&version=1.1&q=";
    //private static String Doctype = "xml";

    public static Dict searchWord(String SearchContent) {
        String Url = JINSHAN_URL + "key=" + JINSHAN_KEY +"&w=" + SearchContent;
        System.out.println(Url);
        try {
            String result = URLHelper.getResultFromUrlPath(Url);
            if (result != null&&!result.equals("")) {
                Dict dict=PullXmlHelper.pullParseXml(result);
                return dict;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String transSentences(String input) {

        try {
            String result=URLHelper.getResultFromUrlPath(YOUDAO_URL + input);
            if (result!=null&&!result.equals("")){
                return parseJson(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseJson(String result) {
        try {
            JSONObject object=new JSONObject(result);
            Log.d("test", result + "");
            JSONArray jsonArray=object.getJSONArray("translation");
            Log.d("test",jsonArray.optString(0));

            return object.getJSONArray("translation").get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


