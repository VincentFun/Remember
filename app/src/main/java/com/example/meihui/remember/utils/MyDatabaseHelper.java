package com.example.meihui.remember.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.meihui.remember.model.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meihui on 2016/4/7.
 * 1.创建生词本数据库WordList(id,key,acceptation,ps,listName,progress,timestamp)
 *
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String TB_WORDLIST="wordlist";
    private  static final String CREATE_WORDLIST="create table wordlist ("
            +"id text primary key not null,"
            +"key text," +
            "acceptation text,"
            +"ps text," +
            "listName text," +
            "progress integer," +
            "timestamp integer)";//integer最多也可以存储8个字节的整数
    private static final String ADD_COLUMN_ISSYNC ="alter table wordlist add 'isSync' integer";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //database=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDLIST);
        //database=getWritableDatabase();
        //Toast.makeText(context,"create succeed",Toast.LENGTH_LONG).show();
        //Log.d("test",CREATE_WORDLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL(ADD_COLUMN_ISSYNC);
                Log.d("test","update column success!");
        }
    }

    //添加生词
    public void insertVocabulary(Vocabulary voc){
        database=getWritableDatabase();
        //WordList(id,key,acceptation,ps,listName,progress,timestamp)
        database.execSQL("insert into "+TB_WORDLIST+" values(?,?,?,?,?,?,?,?)",new Object[]{
                voc.getId().toString(),voc.getKey(),voc.getAcceptation(),voc.getPs(),voc.getListName(),
                voc.getProgress(),voc.getTimestamp(),voc.getIsSync()
        });
        Log.d("test","添加成功");
        database.close();
    }
    public void insertVocabularyList(List<Vocabulary> vocabularyList){
        for (Vocabulary v:vocabularyList){
            insertVocabulary(v);
        }
    }

    //查询单词本名字
    public List<String> selectListName(){
        database=getReadableDatabase();
        List<String> listNames=new ArrayList<String>();
        Cursor cursor=database.rawQuery("select listName from "+TB_WORDLIST +" group by listName",null);
        //Log.d("test",cursor.getCount()+" ");
        if(cursor.moveToFirst()){
            do {
                Log.d("test", cursor.getString(0));
                listNames.add(cursor.getString(0));
            }while (cursor.moveToNext());
            cursor.close();
            database.close();
            return listNames;
        }else{
            Log.d("test","cursor is null");
            database.close();
            return null;
        }

        //Log.d("test",cursor.getCount()+"");
    }

    //根据生词本名字查找单词列表
    public List<Vocabulary> selectVocabularyListByListName(String listName){
        database=getReadableDatabase();
        List<Vocabulary> vocabularyList=new ArrayList<Vocabulary>();
        Cursor cursor=database.rawQuery("select * from " + TB_WORDLIST + " where listName=?", new String[]{listName});
        if(cursor.moveToFirst()) {
            do {
                Vocabulary voc=ModelHelper.cursorToVocabulary(cursor);
                vocabularyList.add(voc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return vocabularyList;
    }

    //查找是否已经加入生词本
    public boolean isVocabularyExist(String key){
        database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from "+TB_WORDLIST+" where key=?",new String[]{key});

        if (cursor.getCount()>0){
            return true;
        }
        return false;
    }

    //根据id删除单词
    public void delete(String id) {
        database=getWritableDatabase();
        database.execSQL("delete from "+TB_WORDLIST+" where id=?",new String[]{id});

        Log.d("test","删除成功");
        database.close();
    }

    //更改单词学习进度
    public void setProgress(int progressUpdate,String id){
        database=getWritableDatabase();
        database.execSQL("update " + TB_WORDLIST + " set progress=progress+? where id=?", new Object[]{progressUpdate, id});

    }

    public void close(){
        database.close();
    }
    private SQLiteDatabase database;

    public Vocabulary selectVocabularyById(String id) {
        database=getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + TB_WORDLIST + " where id=?", new String[]{id});
        cursor.moveToFirst();
        Vocabulary voc=ModelHelper.cursorToVocabulary(cursor);
        return voc;
    }

}
