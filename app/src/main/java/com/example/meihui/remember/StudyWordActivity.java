package com.example.meihui.remember;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meihui.remember.model.Vocabulary;
import com.example.meihui.remember.utils.MyDatabaseHelper;

import java.util.List;

public class StudyWordActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_word);
        databaseHelper=new MyDatabaseHelper(this,"remember.db",null,2);
        vocabularyList=databaseHelper.selectVocabularyListByListName("默认生词本");

        init();
        updateUI(vocabularyList.get(currentIndex));

    }

    private void init() {
        BtnForget= (Button) findViewById(R.id.btn_Forget);
        BtnUncertain= (Button) findViewById(R.id.btn_uncertain);
        BtnRemember= (Button) findViewById(R.id.btn_remember);
        TvAcceptation= (TextView) findViewById(R.id.study_word_acceptation);
        TvKey= (TextView) findViewById(R.id.study_word_key);
        TvPs= (TextView) findViewById(R.id.study_word_ps);

        BtnForget.setOnClickListener(this);
        BtnUncertain.setOnClickListener(this);
        BtnRemember.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String id=vocabularyList.get(currentIndex).getId().toString();
        switch (v.getId()){
            case R.id.btn_Forget:
                databaseHelper.setProgress(PROGRESS_UPDATE_FORGET,id);
                break;
            case R.id.btn_uncertain:
                databaseHelper.setProgress(PROGRESS_UPDATE_UNCERTAIN,id);
                break;
            case R.id.btn_remember:
                databaseHelper.setProgress(PROGRESS_UPDATE_REMEMBER, id);
                break;
            default:
                break;
        }
        if (currentIndex<vocabularyList.size()-1) {
            next();
        }else {
            new AlertDialog.Builder(this).setTitle("单词已全部复习完毕").setPositiveButton(
                    "进行测试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(StudyWordActivity.this,TestActivity.class);
                            startActivity(intent);
                        }
                    }
            ).setNegativeButton("退出学习", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(StudyWordActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }).create().show();
            Log.d("test","没有更多单词");
        }
    }

    private void next(){
        Vocabulary voc=vocabularyList.get(++currentIndex);

        updateUI(voc);
    }

    private void updateUI(Vocabulary voc) {
        Log.d("test",voc.toString());
        TvKey.setText(voc.getKey());
        TvPs.setText(voc.getPs());
        TvAcceptation.setText(voc.getAcceptation());
    }


    private Button BtnForget,BtnUncertain,BtnRemember;
    private TextView TvKey,TvPs,TvAcceptation;
    private MyDatabaseHelper databaseHelper;
    private List<Vocabulary> vocabularyList;
    private int currentIndex;

    private static final int PROGRESS_UPDATE_FORGET=0;
    private static final int PROGRESS_UPDATE_UNCERTAIN=1;
    private static final int PROGRESS_UPDATE_REMEMBER=2;

}
