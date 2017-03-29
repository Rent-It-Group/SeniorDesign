package com.rent_it_app.rent_it.views;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rent_it_app.rent_it.ChatActivity;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.SearchActivity;
import com.rent_it_app.rent_it.SignInActivity;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.ChatUser;
import com.rent_it_app.rent_it.json_models.Conversation;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeywordsFragment extends Fragment {

    private ArrayList<String> keywordList;

    private ArrayAdapter<String> keywordAdapter;

    public ListView list;

    public KeywordsFragment() {
        // Required empty public constructor
    }

    public void setSearchText(String searchText){
        //if(!searchText.isEmpty()){
            keywordAdapter.getFilter().filter(searchText);
        //}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keywords, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CHAT LIST");

        keywordList = new ArrayList<>();
        keywordList.add("bed");
        keywordList.add("jumper");
        keywordList.add("toy");
        keywordList.add("bike");
        keywordList.add("golf");
        keywordList.add("ski");
        keywordList.add("party");
        keywordList.add("supplies");
        keywordList.add("outdoor");
        keywordList.add("gear");
        keywordList.add("soccer");
        keywordList.add("helmet");
        keywordList.add("motorbike");
        keywordList.add("equipment");
        keywordList.add("garden");
        keywordList.add("lamp");
        keywordList.add("lights");
        keywordList.add("books");
        keywordList.add("party");
        keywordList.add("Disney");
        keywordList.add("electronics");
        keywordList.add("projector");
        keywordList.add("Apple");
        keywordList.add("watch");

        keywordAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, keywordList);

        Log.d("KeywordsFragment: ", "onCreateView");

        String searchText = getArguments().getString("searchText");
        Log.d("KeywordsFragment: ", "searchText: " + searchText);

        if(!searchText.isEmpty()){
            keywordAdapter.getFilter().filter(searchText);
        }

        list = (ListView) view.findViewById(R.id.list);

        list.setAdapter(keywordAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int pos, long arg3) {
                Log.d("keywordAdapter", ".getItem(pos):"+keywordAdapter.getItem(pos));
                // Perform query here to database for matching items...
                startActivity(new Intent(getActivity(), SearchActivity.class)
                        .putExtra(Config.EXTRA_DATA, keywordAdapter.getItem(pos)));
            }
        });



        return view;
    }

}
