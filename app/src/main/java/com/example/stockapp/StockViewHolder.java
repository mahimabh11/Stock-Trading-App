package com.example.stockapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder{

    ImageView trend;
    TextView card_ticker;
    TextView card_desc;
    TextView card_last;
    TextView card_change;
    ImageButton openCompany;
    ConstraintLayout cl;
    TextView SectionFlag;
    private final Context context;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        trend=itemView.findViewById(R.id.trend);
        card_ticker=itemView.findViewById(R.id.card_ticker);
        card_desc=itemView.findViewById(R.id.card_desc);
        card_last=itemView.findViewById(R.id.card_last);
        card_change=itemView.findViewById(R.id.card_change);
        openCompany=itemView.findViewById(R.id.company_link);
        context = itemView.getContext();
        cl=itemView.findViewById(R.id.cl);
        itemView.setBackgroundColor(Color.BLACK);
        SectionFlag=itemView.findViewById(R.id.sectionFlag);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, company.class);
                intent.putExtra("msg", card_ticker.getText().toString());
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent,1);
             //   activity.overridePendingTransition(0, 0);
            }
        });;
        openCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, company.class);
                intent.putExtra("msg", card_ticker.getText().toString());
                Activity activity = (Activity) context;
                activity.startActivityForResult(intent,1);
               // activity.overridePendingTransition(0, 0);
            }
        });
    }
}








