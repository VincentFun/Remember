package com.example.meihui.remember.utils;

import android.database.Cursor;

import com.example.meihui.remember.model.Dict;
import com.example.meihui.remember.model.Vocabulary;

import java.util.UUID;

/**
 * Created by meihui on 2016/4/7.
 */
public class ModelHelper {

    public static Vocabulary dictToVocabulary(Dict dict){
        Vocabulary voc=new Vocabulary(true);
        voc.setKey(dict.getKey());
        voc.setAcceptation(dict.getPos() + dict.getAcceptation());
        voc.setPs(dict.getPs());
        return voc;
    }


    public static  Vocabulary cursorToVocabulary(Cursor cursor){
        Vocabulary voc = new Vocabulary(false);
        voc.setKey(cursor.getString(cursor.getColumnIndex("key")));
        voc.setAcceptation(cursor.getString(cursor.getColumnIndex("acceptation")));
        voc.setPs(cursor.getString(cursor.getColumnIndex("ps")));
        voc.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex("id"))));
        voc.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
        voc.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
        voc.setListName(cursor.getString(cursor.getColumnIndex("listName")));
        voc.setIsSync(cursor.getInt(cursor.getColumnIndex("isSync")));
        return voc;
    }
}
