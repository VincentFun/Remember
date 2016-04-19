package com.example.meihui.remember.utils;

import android.util.Log;

import com.example.meihui.remember.model.Vocabulary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by meihui on 2016/4/13.
 */
public class JSONHelper {
    public static String fromVocabularyListToJSONString(List<Vocabulary> list) {
        JSONArray jsonArray = new JSONArray();
        for (Vocabulary voc : list) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("id", voc.getId().toString());
                obj.put("key", voc.getKey());
                obj.put("acceptation", voc.getAcceptation());
                obj.put("ps", voc.getPs());
                obj.put("listName", voc.getListName());
                obj.put("progress", voc.getProgress());
                obj.put("timestamp", voc.getTimestamp());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("servlet",jsonArray.toString());
        return jsonArray.toString();
    }

    public static List<Vocabulary> fromJSONStringToVocabularyList(String jsonString) {

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
            List<Vocabulary> vocList = new ArrayList<Vocabulary>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Vocabulary voc = new Vocabulary(false);
                voc.setId(UUID.fromString(obj.getString("id")));
                voc.setKey(obj.getString("key"));
                voc.setPs(obj.getString("ps"));
                voc.setAcceptation(obj.getString("acceptation"));
                voc.setTimestamp(obj.getLong("timestamp"));
                voc.setProgress(obj.getInt("progress"));
                voc.setListName(obj.getString("listName"));
                vocList.add(voc);
            }
            return vocList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
