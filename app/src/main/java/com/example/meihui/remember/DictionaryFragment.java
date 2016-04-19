package com.example.meihui.remember;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.meihui.remember.utils.RawHelper;

import java.util.ArrayList;
import java.util.List;

public class DictionaryFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_dictionary,container);

        EtSearchWord= (EditText) v.findViewById(R.id.dic_search_et);
        BtnSearch= (Button) v.findViewById(R.id.dic_search_btn);

        BtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String searchWord = EtSearchWord.getText().toString().trim();
                if (searchWord != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getActivity(), WordActivity.class);
                            intent.putExtra(WordActivity.EXTRA_KEY, searchWord);
                            startActivity(intent);
                        }
                    }).start();
                } else {
                    Toast.makeText(getContext(), "请输入单词", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LvSuggestion= (ListView) v.findViewById(R.id.dic_suggestion_list);
        suggestionAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        offLineDic=RawHelper.readOfflineDicOrdered(getContext());
        suggestionAdapter.addAll(offLineDic);
        LvSuggestion.setAdapter(suggestionAdapter);

       EtSearchWord.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               String input = s.toString();
               suggestionAdapter.clear();
               suggestionAdapter.addAll(getSuggestionList(input));
               suggestionAdapter.notifyDataSetChanged();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        LvSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(),WordActivity.class);
                intent.putExtra(WordActivity.EXTRA_KEY,suggestionAdapter.getItem(position));
                startActivity(intent);
            }
        });
        return v;
    }

    private List<String> getSuggestionList(String input) {
        List<String> suggestionList=new ArrayList<String>();

        for(String s:offLineDic){
            boolean isMatch=true;
            for(int i=0;i<input.length();i++){
                if(s.length()<input.length()){
                    isMatch=false;
                    break;
                }else if(!(s.charAt(i)+"").equalsIgnoreCase((input.charAt(i)+""))){
                    isMatch=false;
                    break;
                }
            }
            if(isMatch){
                suggestionList.add(s);
            }else{
                //因为词汇表是有序的，如果找到了最后一项匹配性则无需再进行查找
                if(suggestionList.size()>0){
                    break;
                }
            }
        }
        return suggestionList;
    }


    private EditText EtSearchWord;
    private Button BtnSearch;

    private ListView LvSuggestion;
    private ArrayAdapter<String> suggestionAdapter;
    private List<String> offLineDic;
}
