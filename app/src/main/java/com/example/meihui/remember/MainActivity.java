package com.example.meihui.remember;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.meihui.remember.utils.MyDatabaseHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost= (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabDictionary").setIndicator("查词").setContent(R.id.tabDictionary));
        tabHost.addTab(tabHost.newTabSpec("tabTrans").setIndicator("翻译").setContent(R.id.tabTrans));
        tabHost.addTab(tabHost.newTabSpec("tabVocabulary").setIndicator("生词本").setContent(R.id.tabVocabulary));
        tabHost.addTab(tabHost.newTabSpec("tabRecite").setIndicator("背单词").setContent(R.id.tabRecite));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_sync:
                Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
                new SyncVocabularyList(this).syncVocabularyList();
                break;
            default:
                break;
        }
        return true;
    }


    private TabHost tabHost;

}
