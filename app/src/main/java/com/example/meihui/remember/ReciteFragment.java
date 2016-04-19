package com.example.meihui.remember;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import junit.framework.Test;

public class ReciteFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recite,container);
        BtnStudyWord= (Button) view.findViewById(R.id.btn_studyWord);
        BtnStudyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),StudyWordActivity.class);
                startActivity(intent);
            }
        });

        BtnTest= (Button) view.findViewById(R.id.btn_test);
        BtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),TestActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    private Button BtnStudyWord;
    private Button BtnTest;
}
