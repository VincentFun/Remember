package com.example.meihui.remember.model;

import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Created by meihui on 2016/4/7.
 * 生词本-单词Model
 */
public class Vocabulary{
    private UUID id;
    private String key;//英语单词
    private String acceptation;//中文释义
    private String ps;//音标
    private String listName;//生词本名
    private int progress;//复习进度
    private long timestamp;//加入时间戳
    private int isSync=0;

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public Vocabulary(boolean IsCreate) {
        //创建生词时
        if(IsCreate) {
            id = UUID.randomUUID();
            timestamp = new Date().getTime();
            listName = "默认生词本";
            progress = 1;
        }
    }

    public String getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(String acceptation) {
        this.acceptation = acceptation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public UUID getId() {
        return id;
    }


    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return key;
    }


    public static final class TimeComparator implements Comparator<Vocabulary>{

        @Override
        public int compare(Vocabulary v1, Vocabulary v2) {
            return (int)(v1.getTimestamp()-v2.getTimestamp());
        }
    }
    public static class ProgressComparator implements Comparator<Vocabulary>{

        @Override
        public int compare(Vocabulary v1, Vocabulary v2) {
            return v1.getProgress()-v2.getProgress();
        }
    }
    public static class AlphabetComparator implements Comparator<Vocabulary>{

        @Override
        public int compare(Vocabulary v1, Vocabulary v2) {
            return v1.getKey().compareTo(v2.getKey());
        }
    }
}
