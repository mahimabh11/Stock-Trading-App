package com.example.stockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    ArrayList<Article> articles;
    public ArticlesAdapter() {
       articles= new ArrayList<>();
    }
    public void setData(ArrayList<Article> articles){
        this.articles=articles;
    }
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View articleView=layoutInflater.inflate(R.layout.news_card,parent,false);
        return new ArticleViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article= articles.get(position);
        Picasso.get().load(article.image).fit().into(holder.image);
        holder.ntitle.setText(article.ntitle);
        holder.source.setText(article.source);
        holder.ago.setText(article.ago);
        holder.link.setText(article.link);
        holder.imglink.setText(article.imglink);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
