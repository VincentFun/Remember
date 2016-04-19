package com.example.meihui.remember.utils;

import android.content.Context;

import com.example.meihui.remember.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by meihui on 2016/4/16.
 */
public class RawHelper {
    public static List<String> readOfflineDic(Context context){
        List<String> dicList=new ArrayList<String>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.offline_dic);
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String buffer;
        try {
            while((buffer=br.readLine())!=null){
                if(!dicList.contains(buffer)){
                    dicList.add(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dicList;
    }

    public static List<String> readOfflineDicOrdered(Context context){
        List<String> dicList=readOfflineDic(context);
        Collections.sort(dicList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return dicList;
    }

}
