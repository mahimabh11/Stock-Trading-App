package com.example.stockapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class mainRecyclerAdap extends RecyclerView.Adapter<mainRecyclerAdap.ViewHolder> {

    ViewGroup currentParent;
    List<section> sectionList;
    StockAdapter childRecyclerAdapter;
    RecyclerView childRecyclerView;
    mainRecyclerAdap.ViewHolder Holder;
    int flag1;
    ConstraintLayout clayout;
    public mainRecyclerAdap(List<section> sectionList) {
        this.sectionList=sectionList;
        //System.out.println("maindap print");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // System.out.println("maindap onview");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sectionlayout,parent,false);
        childRecyclerView=view.findViewById(R.id.childRecyclerView);
        currentParent=parent;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mainRecyclerAdap.ViewHolder holder, int position) {
        //System.out.println("onBV adap");
        section section = sectionList.get(position);
        String sectionName = section.getSectionName();
        String netW= section.getNetW();
        String netvalue=section.getNetvalue();
        List<Stock_card> items = section.getStockList();
        holder.sectionName.setText(sectionName);
        holder.netW.setText(netW);
        holder.netvalue.setText(netvalue);
        if(sectionName=="FAVORITES")
        {
            flag1=1;
            holder.netW.setVisibility(View.GONE);
            //holder.netW.setHeight(0);
            holder.netvalue.setVisibility(View.GONE);
            //holder.netvalue.setHeight(0);
        }
        else
        {
            flag1=0;
            holder.netW.setVisibility(View.VISIBLE);
            //holder.netW.setHeight(0);
            holder.netvalue.setVisibility(View.VISIBLE);
            //holder.netvalue.setHeight(0);
        }
       // System.out.println("maindap onbind");

        childRecyclerAdapter = new StockAdapter(items);
        if(flag1==1) {
            enableSwipeToDeleteAndUndo(childRecyclerAdapter);
        }
        ItemTouchHelper.Callback callback = new ItemMoveCallback(childRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(holder.childRecyclerView);
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(currentParent.getContext()));
        //holder.childRecyclerView.addItemDecoration(new DividerItemDecoration(currentParent.getContext(),DividerItemDecoration.VERTICAL));
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);
        this.Holder=holder;


    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    private void enableSwipeToDeleteAndUndo( StockAdapter childRecyclerAdapter) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(currentParent.getContext(),childRecyclerAdapter) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Stock_card item = childRecyclerAdapter.getData().get(position);

                childRecyclerAdapter.removeItem(position);



            }

        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(childRecyclerView);
    }





    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sectionName;
        TextView netW;
        TextView netvalue;
        RecyclerView childRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          //  System.out.println("in view holder mainadap");
            sectionName=itemView.findViewById(R.id.sectionName);
            netW=itemView.findViewById(R.id.netW);
            netvalue=itemView.findViewById(R.id.netvalue);
            childRecyclerView=itemView.findViewById(R.id.childRecyclerView);
        }
    }
}
