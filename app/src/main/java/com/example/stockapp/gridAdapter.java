package com.example.stockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class gridAdapter extends BaseAdapter{
    private Context context;
    private final String[] list;

    public gridAdapter(Context context,String[] list) {
        this.list = list;
        this.context=context;
    }


    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridview;
        if(convertView==null)
        {
            gridview= new View(context);
            gridview= inflater.inflate(R.layout.grid,null);
            TextView tv=(TextView) gridview.findViewById(R.id.griditem);
            tv.setText(list[position]);
            tv.setOnClickListener(null);
        }
        else
        {
            gridview=(View) convertView;
        }
        return gridview;
    }
}
