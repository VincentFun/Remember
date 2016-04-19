package com.example.meihui.remember;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.meihui.remember.utils.OnlineSearch;



public class TransFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trans,container);
        EtInput= (EditText) view.findViewById(R.id.trans_input);
        TvOutput= (TextView) view.findViewById(R.id.trans_output);
        BtnGoTrans= (Button) view.findViewById(R.id.trans_go);

        BtnGoTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String input=EtInput.getText().toString();
                        String output= OnlineSearch.transSentences(input);
                        Message message=new Message();
                        message.obj=output;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        return view;
    }

    private EditText EtInput;
    private TextView TvOutput;
    private Button BtnGoTrans;

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            String output= (String) msg.obj;
            if (output==null){
                TvOutput.setText("未找到翻译结果");
            }else{
                TvOutput.setText(output);
            }
        }
    };
}
