package com.example.huanhm.duolingoclone.DashboardFagments.SearchingViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.huanhm.duolingoclone.R;

public class WordViewHolder extends RecyclerView.ViewHolder {
    TextView  tvWord,tvDefinition,tvExample;

    public WordViewHolder(@NonNull View itemView) {
        super(itemView);
        getView(itemView);
    }

    private void getView(View view) {
        tvWord = view.findViewById(R.id.word);
        tvDefinition = view.findViewById(R.id.definition);
        tvExample = view.findViewById(R.id.example);
    }

    public void setTvWord(String tvWord) {
        this.tvWord.setText(tvWord);
    }

    public void setTvDefinition(String tvDefinition) {
        this.tvDefinition.setText(tvDefinition);
    }

    public void setTvExample(String tvExample) {
        this.tvExample.setText(tvExample);
    }
}
