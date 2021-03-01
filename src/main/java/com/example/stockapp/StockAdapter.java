package com.example.stockapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockAdapter  extends RecyclerView.Adapter<StockViewHolder> implements ItemMoveCallback.ItemTouchHelperContract{
    List<Stock_card> cards;
    int red;
    int green;
    int grey;
    int flagSection;
    int cg;
    ViewGroup parent;
    public StockAdapter(List<Stock_card> cards) {
        this.cards=cards;
    }

//    public void updater()
//    {
//        SharedPreferences sp;
//        SharedPreferences.Editor editorSp;
//        String replacer;
//
//        if(flagSection==1) {
//            sp = parent.getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//            editorSp = sp.edit();
//            replacer="portOrder";
//        }
//        else
//        {
//            sp = parent.getContext().getSharedPreferences("MyPreffav", 0); // 0 - for private mode
//            editorSp = sp.edit();
//            replacer="favOrder";
//        }
//        String list=sp.getString(replacer,null);
//        RequestQueue requestQueue3 = Volley.newRequestQueue(parent.getContext());
//
//        JsonObjectRequest request3 = new JsonObjectRequest( url_stock,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            totalWorth=0;
//                            System.out.println("got inside");
//                            JSONArray jsonObj = response.getJSONArray("data");
//                            for (int i = 0; i < jsonObj.length(); i++) {
//                                JSONObject jo = jsonObj.getJSONObject(i);
//                                // System.out.println(jo);
//                                String currKey = jo.getString("ticker");
//                                float ll, prevClose;
//                                if (jo.getString("last") == "null") {
//                                    ll = (float) 0.0;
//                                } else {
//                                    ll = Float.parseFloat(jo.getString("last"));
//                                }
//                                if (jo.getString("prevClose") == "null") {
//                                    prevClose = (float) 0.0;
//                                } else {
//                                    prevClose = Float.parseFloat(jo.getString("prevClose"));
//                                }
//                                float diff = ll - prevClose;
//
//                            }
//
//                        }catch (Exception e)
//                        {
//
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        requestQueue3.add(request3);
//        for(Stock_card curr: cards)
//        {
//            System.out.println(curr.card_ticker);
//        }
//
//
//    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View stock_View=layoutInflater.inflate(R.layout.stock_card,parent,false);
        red=ContextCompat.getColor(context, R.color.red);
        green=ContextCompat.getColor(context, R.color.green);
        grey=ContextCompat.getColor(context, R.color.grey);
        cg=ContextCompat.getColor(context, R.color.cardbg);
        this.parent=parent;
        return new StockViewHolder(stock_View);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock_card card= cards.get(position);
        float checker=Float.parseFloat(card.card_change);
        holder.card_change.setText(card.card_change);
        holder.card_last.setText(card.card_last);
        holder.card_ticker.setText(card.card_ticker);
        holder.card_desc.setText(card.card_desc);
        holder.SectionFlag.setText(card.SectionFlag);
        flagSection=Integer.parseInt(card.SectionFlag);
        if(checker<0)
        {
            holder.trend.setImageResource(R.drawable.ic_baseline_trending_down_24);
            holder.card_change.setTextColor(red);
        }
        else if(checker>0)
        {
            holder.trend.setImageResource(R.drawable.ic_twotone_trending_up_24);
            holder.card_change.setTextColor(green);
        }
        else
        {
            holder.card_change.setTextColor(Color.BLACK);
        }
        //Picasso.get().load(card.trend).into(holder.trend);



    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        SharedPreferences sp;
        SharedPreferences.Editor editorSp;
        String replacer;
        if(flagSection==1) {
            sp = parent.getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            editorSp = sp.edit();
            replacer="portOrder";
        }
        else
        {
            sp = parent.getContext().getSharedPreferences("MyPreffav", 0); // 0 - for private mode
            editorSp = sp.edit();
            replacer="favOrder";
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cards, i, i + 1);
            }
            System.out.println("Updated");
            editorSp.remove(replacer);
            editorSp.commit();
            String app="";
            for(Stock_card curr: cards)
            {
                app=app+curr.card_ticker+",";
               // System.out.println(curr.card_ticker);
            }
            //app=app.substring(0,app.length()-1);
            editorSp.putString(replacer,app);
            editorSp.commit();
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cards, i, i - 1);
            }
            System.out.println("Updated");
            editorSp.remove(replacer);
            editorSp.commit();
            String app="";
            for(Stock_card curr: cards)
            {
                app=app+curr.card_ticker+",";
               // System.out.println(curr.card_ticker);
            }
           // app=app.substring(0,app.length()-1);
            editorSp.putString(replacer,app);
            editorSp.commit();
        }

        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onRowSelected(StockViewHolder myViewHolder) {
        myViewHolder.cl.setBackgroundColor(grey);
        myViewHolder.card_desc.setTextColor(Color.parseColor("#5A5A5A"));
    }

    @Override
    public void onRowClear(StockViewHolder myViewHolder) {

        myViewHolder.cl.setBackgroundColor(cg);
        myViewHolder.card_desc.setTextColor(Color.parseColor("#a8a6a8"));

    }
    public void removeItem(int position) {
        SharedPreferences sp;
        SharedPreferences.Editor editorSp;
        String replacer;
        sp = parent.getContext().getSharedPreferences("MyPreffav", 0); // 0 - for private mode
        editorSp = sp.edit();
        replacer="favOrder";
        editorSp.remove(cards.get(position).getTicker());
        editorSp.commit();
        String currlist=sp.getString(replacer,null);
        editorSp.remove(replacer);
        editorSp.commit();
        currlist=currlist.replace(cards.get(position).getTicker()+",","");
        editorSp.putString(replacer,currlist);
        editorSp.commit();
        cards.remove(position);
        notifyItemRemoved(position);

    }

    public void restoreItem(Stock_card item, int position) {
        cards.add(position, item);
        notifyItemInserted(position);
    }

    public List<Stock_card> getData() {
        return cards;
    }
}


