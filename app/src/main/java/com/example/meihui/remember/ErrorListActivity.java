package com.example.meihui.remember;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.meihui.remember.model.Sent;
import com.example.meihui.remember.model.Vocabulary;
import com.example.meihui.remember.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ErrorListActivity extends AppCompatActivity {

    public static final  String ERROR_LIST_EXTRA="com.meihui.example.errolist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_list);

        Intent intent=getIntent();
        ArrayList<String> errorList=intent.getStringArrayListExtra(ERROR_LIST_EXTRA);


        LvError= (ListView) findViewById(R.id.error_list_view);
        if(errorList!=null) {
            ArrayAdapter adapter=new ErrorListAdapter<Vocabulary>(this);
            databaseHelper=new MyDatabaseHelper(this,"remember.db",null,2);
            for(String id:errorList){
                adapter.add(databaseHelper.selectVocabularyById(id));
            }
            LvError.setAdapter(adapter);


            LvError.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ErrorListActivity.this, WordActivity.class);
                    intent.putExtra(WordActivity.EXTRA_KEY, LvError.getAdapter().getItem(position).toString());
                    startActivity(intent);
                }
            });
        }else{
            Log.d("test","errorlist is null");
        }
    }

    private ListView LvError;

    private class ErrorListAdapter<T> extends ArrayAdapter<T>{

        public ErrorListAdapter(Context context) {
            super(context, R.layout.listview_error);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//          可优化
            View view= LayoutInflater.from(getContext()).inflate(R.layout.listview_error,null);
            Vocabulary voc= (Vocabulary) getItem(position);
            TextView tvKey= (TextView) view.findViewById(R.id.error_key);
            TextView tvAcceptation= (TextView)view.findViewById(R.id.error_acceptation);
            tvKey.setText(voc.getKey());
            tvAcceptation.setText(voc.getAcceptation());

            return view;
        }
    }

    private MyDatabaseHelper databaseHelper;
}
