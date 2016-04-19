package com.example.meihui.remember;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.meihui.remember.model.Vocabulary;
import com.example.meihui.remember.utils.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VocabularyFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper=new MyDatabaseHelper(getContext(),"remember.db",null,2);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_vocabulary,container);
        SplistName= (Spinner) view.findViewById(R.id.vocabulary_select_list);
        SpSortType= (Spinner) view.findViewById(R.id.vocabulary_sort_type);
        //查询生词本名并绑定生词名列表
        List<String> listNames=databaseHelper.selectListName();
        if(listNames==null) {
            //Log.d("test",listNames.toString());
            SplistName.setVisibility(View.INVISIBLE);
            SpSortType.setVisibility(View.INVISIBLE);
            return view;
        }
        SplistName.setVisibility(View.VISIBLE);
        SpSortType.setVisibility(View.VISIBLE);

        ArrayAdapter spinAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,listNames);
        SplistName.setAdapter(spinAdapter);

        //根据单词本名绑定单词列表
        LvVocabulary= (ListView) view.findViewById(R.id.vocabularyList);
        lvAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1);
        setLvVocabularyAdapter(SplistName.getSelectedItem().toString());
        LvVocabulary.setAdapter(lvAdapter);

        //
        SplistName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLvVocabularyAdapter(SplistName.getSelectedItem().toString());
                lvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置单词列表上下文菜单
        LvVocabulary.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        LvVocabulary.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.context_menu_vocabulary, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_voc:
                        for (int i = 0; i < lvAdapter.getCount(); i++) {
                            if (LvVocabulary.isItemChecked(i)) {
                                Vocabulary voc = (Vocabulary) lvAdapter.getItem(i);
                                databaseHelper.delete(voc.getId().toString());
                            }
                        }
                        mode.finish();
                        setLvVocabularyAdapter(SplistName.getSelectedItem().toString());
                        lvAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

        });
        //设置排序类型监听事件

        SpSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int sortType=SpSortType.getSelectedItemPosition();
                switch (sortType){
                    case 0:
                        currentSortType=SORT_BY_TIME;
                        Collections.sort(vocabularyList, new Vocabulary.TimeComparator());
                        break;
                    case 1:
                        currentSortType=SORT_BY_PROGRESS;
                        Collections.sort(vocabularyList,new Vocabulary.ProgressComparator());
                        break;
                    case 2:
                        currentSortType=SORT_BY_ALPHABET;
                        Collections.sort(vocabularyList,new Vocabulary.AlphabetComparator());
                        break;
                    default:
                        break;
                }
                //lvAdapter.notifyDataSetChanged();
                lvAdapter.clear();
                lvAdapter.addAll(vocabularyList);
                Log.d("test", vocabularyList.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设置单词列表点击事件
        LvVocabulary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vocabulary voc= (Vocabulary) lvAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), WordActivity.class);
                intent.putExtra(WordActivity.EXTRA_KEY, voc.getKey());
                startActivity(intent);
            }
        });

        return view;
    }

    private void setLvVocabularyAdapter(String listName){

        vocabularyList=databaseHelper.selectVocabularyListByListName(listName);
        Log.d("test", vocabularyList.toString());
        lvAdapter.clear();
        lvAdapter.addAll(vocabularyList);

    }


    @Override
    public void onResume() {
        super.onResume();
        if(lvAdapter!=null) {
            setLvVocabularyAdapter(SplistName.getSelectedItem().toString());
            lvAdapter.notifyDataSetChanged();
        }
    }


    private Spinner SplistName,SpSortType;
    private ListView LvVocabulary;

    private MyDatabaseHelper databaseHelper;
    private ArrayAdapter lvAdapter;
    private List<Vocabulary> vocabularyList;

    private static String SORT_BY_TIME="timestamp";
    private static String SORT_BY_PROGRESS="progress";
    private static String SORT_BY_ALPHABET="key";

    private String currentSortType=SORT_BY_TIME;

}
