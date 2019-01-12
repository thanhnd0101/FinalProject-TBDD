package com.example.huanhm.duolingoclone.DashboardFagments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huanhm.duolingoclone.DashboardFagments.SearchingRecyclerViewAdapter.SearchingRecyclerViewAdapter;
import com.example.huanhm.duolingoclone.R;
import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse.WordList;
import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class SearchingFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;

    private SearchingRecyclerViewAdapter searchingRecyclerViewAdapter;
    private WordList wordList;
    private Context mContext;

    public SearchingFragment(Context context) {
        this.mContext = context;
    }

    public static SearchingFragment getSearchingFragmentInstance(Context context) {
        SearchingFragment homeTabFragment = new SearchingFragment(context);
        Bundle args = new Bundle();
        homeTabFragment.setArguments(args);
        homeTabFragment.setRetainInstance(true);
        return homeTabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment_searching,container,false);
        initView(view);
        setSearchFunction();
        return view;
    }

    private void setSearchFunction() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                if(s.length() != 0){
                    s = s.replaceAll(" ","+");
                    getWordDefinitionList(s);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void getWordDefinitionList(String s) {
        Call<WordList> call = UrbanDictionaryService.getDefinition(s);
        call.enqueue(new Callback<WordList>() {
            @Override
            public void onResponse(@NonNull Call<WordList> call, @NonNull Response<WordList> response) {
                if(response.isSuccessful()){
                    wordList = response.body();
                    searchingRecyclerViewAdapter = new SearchingRecyclerViewAdapter(mContext.getApplicationContext(),wordList);
                    recyclerView.setAdapter(searchingRecyclerViewAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WordList> call, @NonNull Throwable t) {

            }
        });
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.searchhing_searchView);
        recyclerView = view.findViewById(R.id.searching_recyclerView);
        setRecyclerLayoutManager();
    }

    private void setRecyclerLayoutManager() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

}
