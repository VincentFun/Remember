package com.example.meihui.remember.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by meihui on 2016/4/10.
 */
public class MediaHelper {
    public static void playSound(String path,Context context){
            MediaPlayer mp = null;
            try {
                mp = MediaPlayer.create(context, Uri.parse(path));
                mp.start();
            } finally {
                mp = null;
            }
        }
}
