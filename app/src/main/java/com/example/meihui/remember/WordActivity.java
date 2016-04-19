package com.example.meihui.remember;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meihui.remember.model.Dict;
import com.example.meihui.remember.model.Sent;
import com.example.meihui.remember.utils.MediaHelper;
import com.example.meihui.remember.utils.ModelHelper;
import com.example.meihui.remember.utils.MyDatabaseHelper;
import com.example.meihui.remember.utils.OnlineSearch;


public class WordActivity extends AppCompatActivity {

    public static String EXTRA_KEY="com.example.meihui.remember.key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        init();


        //开启线程访问网络，查找单词释义并将结果解析成Dict返回
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent=getIntent();
                String searchWord=intent.getStringExtra(EXTRA_KEY);
                dict=OnlineSearch.searchWord(searchWord);
                Message message=new Message();
                message.obj=dict;
                handler.sendMessage(message);
            }
        }).start();

        databaseHelper=new MyDatabaseHelper(WordActivity.this,"remember.db",null,2);

        //加入生词本
        BtnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.insertVocabulary(ModelHelper.dictToVocabulary(dict));
                setButtonUnabled();
            }
        });

    }

    private void setButtonUnabled() {
        BtnAddToList.setEnabled(false);
        BtnAddToList.setText("已添加");
    }


    private void init() {
        TvKey = (TextView) findViewById(R.id.key);
        TvAcceptation = (TextView) findViewById(R.id.acceptation);
        TvPhonetic = (TextView) findViewById(R.id.phonetic);
        TvPos= (TextView) findViewById(R.id.pos);
        LvSent= (ListView) findViewById(R.id.sent);
        BtnAddToList= (Button) findViewById(R.id.addToList);
        BtnPlaySound= (ImageView) findViewById(R.id.word_play_sound);

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
    }

    //获取dict并在主线程更新UI
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            progressBar.setVisibility(View.GONE);
            final Dict dict= (Dict) msg.obj;
            TvKey.setText(dict.getKey());
            TvAcceptation.setText(dict.getAcceptation());
            TvPhonetic.setText(dict.getPs());
            TvPos.setText(dict.getPos());
            if(databaseHelper.isVocabularyExist(dict.getKey())) {
                setButtonUnabled();
            }

            adapter=new SentAdapter<Sent>(WordActivity.this);
            for(Sent sent:dict.getSents()){
                adapter.add(sent);
            }
            LvSent.setAdapter(adapter);

            BtnPlaySound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaHelper.playSound(dict.getPron(), WordActivity.this);
                }
            });
        }
    };
    private TextView TvKey, TvAcceptation, TvPhonetic,TvPos;
    private ListView LvSent;
    private SentAdapter<Sent> adapter;
    private Button BtnAddToList;
    private ImageView BtnPlaySound;
    private ProgressBar progressBar;

    private Dict dict;
    private MyDatabaseHelper databaseHelper;

    private class SentAdapter<T> extends ArrayAdapter<T>{

        public SentAdapter(Context context) {
            super(context, R.layout.listitem_sent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//          可优化
            View view= LayoutInflater.from(getContext()).inflate(R.layout.listitem_sent,null);
            Sent sent= (Sent) getItem(position);
            TextView orig= (TextView) view.findViewById(R.id.orig);
            TextView trans= (TextView)view.findViewById(R.id.trans);
            orig.setText(sent.getOrig());
            trans.setText(sent.getTrans());

            return view;
        }
    }
}
