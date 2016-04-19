package com.example.meihui.remember;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meihui.remember.model.Vocabulary;
import com.example.meihui.remember.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        databaseHelper=new MyDatabaseHelper(this,"remember.db",null,2);
        vocabularyList=databaseHelper.selectVocabularyListByListName("默认生词本");
        errorList=new ArrayList();
        init();
        showRandomQuestion();

        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                vocabularyList.remove(currentVoc);
                if (vocabularyList.size()>0) {
                    showRandomQuestion();
                }else{
                    endTest();
                }
            }
        });
    }

    private void endTest() {
        new AlertDialog.Builder(this).setTitle("测试结束")
                .setMessage("此次测试共答错"+errorList.size()+"道题。是否查看错误单词?")
                .setPositiveButton("查看错误单词", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(TestActivity.this,ErrorListActivity.class);
                        intent.putStringArrayListExtra(ErrorListActivity.ERROR_LIST_EXTRA, errorList);
                        startActivity(intent);
                    }
                }).create().show();
    }

    private void checkAnswer() {
        RadioButton radioButton= (RadioButton) radioGroup.getChildAt(trueAnswerIndex);
        if (radioButton.isChecked()){
            Toast.makeText(this,"答对了",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"答错了",Toast.LENGTH_SHORT).show();
            errorList.add(currentVoc.getId().toString());
        }
    }

    private void showRandomQuestion() {
        int random= new Random().nextInt(vocabularyList.size());
        //Log.d("test",random+"");
        currentVoc=vocabularyList.get(random);
        //Log.d("test",currentVoc.toString());
        updateUI();
    }

    private void updateUI() {
        //设置当前单词英文
        TvKey.setText(currentVoc.getKey());

        //清楚radiobutton选中状态
        for(int i=0;i<radioGroup.getChildCount();i++){
            RadioButton rb= (RadioButton) radioGroup.getChildAt(i);
            rb.setChecked(false);
        }

        //设置各个单选对应内容
        trueAnswerIndex =new Random().nextInt(4);
        RadioButton radioButton= (RadioButton) radioGroup.getChildAt(trueAnswerIndex);
        radioButton.setText(currentVoc.getAcceptation());
    }

    private void init() {
        BtnForget= (Button) findViewById(R.id.test_forget);
        BtnNext= (Button) findViewById(R.id.test_next);
        radioGroup= (RadioGroup) findViewById(R.id.test_radio_group);
        TvKey= (TextView) findViewById(R.id.test_key);
    }

    private Button BtnForget,BtnNext;
    private RadioGroup radioGroup;
    private TextView TvKey;
    private List<Vocabulary> vocabularyList;
    private ArrayList<String> errorList;
    private Vocabulary currentVoc;
    private int trueAnswerIndex;
    private MyDatabaseHelper databaseHelper;
}
