package com.example.huanhm.duolingoclone.DashboardFagments.SearchingRecyclerViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huanhm.duolingoclone.DashboardFagments.SearchingViewHolder.WordViewHolder;
import com.example.huanhm.duolingoclone.R;
import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse.WordDefinition;
import com.example.huanhm.duolingoclone.UrbanDictionaryService.UrbanDictionaryResponse.WordList;

import java.util.List;

public class SearchingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private WordList wordList;

    public SearchingRecyclerViewAdapter(Context context, WordList wordList) {
        this.context = context;
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        View wordView = inflater.inflate(R.layout.word_item,viewGroup,false);
        viewHolder = new WordViewHolder(wordView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        WordViewHolder wordViewHolder = (WordViewHolder)viewHolder;
        configureWordViewHolder(wordViewHolder,i);
    }

    private void configureWordViewHolder(WordViewHolder wordViewHolder,int position) {
        WordDefinition wordDefinition = wordList.getList().get(position);
        wordViewHolder.setTvWord(wordDefinition.getWord());
        wordViewHolder.setTvDefinition(wordDefinition.getDefinition());
        wordViewHolder.setTvExample(wordDefinition.getExample());
    }

    @Override
    public int getItemCount() {
        return wordList.getList().size();
    }
}
