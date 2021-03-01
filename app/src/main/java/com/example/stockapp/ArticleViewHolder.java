package com.example.stockapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.URLEncoder;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView ntitle;
    TextView source;
    TextView ago;
    TextView link;
    TextView imglink;
    private final Context context;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.image);
        ntitle=itemView.findViewById(R.id.ntitle);
        source=itemView.findViewById(R.id.source);
        ago=itemView.findViewById(R.id.time_ago);
        link=itemView.findViewById(R.id.link);
        imglink=itemView.findViewById(R.id.imglink);
        context = itemView.getContext();

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getText().toString()));
                //System.out.println(link.getText().toString());
                context.startActivity(browserIntent);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.sharer);
                ImageView dimg= (ImageView) dialog.findViewById(R.id.dialogimg);
                Picasso.get().load(imglink.getText().toString()).into(dimg);
                TextView dtitle= (TextView) dialog.findViewById(R.id.dialogtitle);
                ImageButton itwitter= (ImageButton) dialog.findViewById(R.id.twitter);
                ImageButton ichrome= (ImageButton) dialog.findViewById(R.id.chrome);

                dtitle.setText(ntitle.getText());
                itwitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // System.out.println("clicked twitter");
                        try {
                            String query = URLEncoder.encode(link.getText().toString(), "utf-8");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=Check out this Link:&url=" +query+"&hashtags=CSCI571StockApp"));
                            context.startActivity(browserIntent);
                        }catch (Exception e)
                        {
                            System.out.println(e);
                        }
                    }
                });
                ichrome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getText().toString()));
                        // System.out.println(tempurl);
                        context.startActivity(browserIntent);
                    }
                });
                System.out.println("in long click");
                dialog.show();
                return true;
            }
        });
    }

}
