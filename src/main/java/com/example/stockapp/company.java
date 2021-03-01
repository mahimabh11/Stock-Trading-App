package com.example.stockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class company extends AppCompatActivity {

    private Menu menu;
    private GridView gridView;
    RecyclerView recyclerView;
    ArticlesAdapter adap;
    ArrayList<Article> articles;
    final Context context = this;
    Dialog dialogTrade ;
    Dialog congoMsg;
    int flagger;
    SharedPreferences pref;
    SharedPreferences preffav;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;
    ProgressBar progressBar;
    NestedScrollView nestedscroll;
    TextView fetch;
    long datemili=System.currentTimeMillis();
    TextView comp ;
    TextView name ;
    String str;
    WebView wv;
    float price;
    DecimalFormat myformat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_company);
        flagger=0;
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        preffav=getApplicationContext().getSharedPreferences("MyPreffav", 0); // 0 - for private mode
        editor = pref.edit();
        editor2=preffav.edit();
        fetch= findViewById(R.id.fetch);
        progressBar=(ProgressBar) findViewById(R.id.progressBar2);
        nestedscroll=(NestedScrollView) findViewById(R.id.nestedscroll);
        nestedscroll.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fetch.setVisibility(View.VISIBLE);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setTitle("");
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        str = intent.getStringExtra("msg");
        dialogTrade= new Dialog(context);
        dialogTrade.setContentView(R.layout.buysell);
        congoMsg= new Dialog(context);
        congoMsg.setContentView(R.layout.congrats);
        Button cmsg=(Button) congoMsg.findViewById(R.id.cmsg);
        cmsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
             congoMsg.dismiss();
            }
        });;
        TextView available=(TextView) dialogTrade.findViewById(R.id.available);
        TextView disMsg=(TextView) congoMsg.findViewById(R.id.disMsg);

        this.gridView = (GridView)findViewById(R.id.statsGridView);
        wv= (WebView) findViewById(R.id.webView1);
        wv.getSettings().setJavaScriptEnabled(true);
        //String url = "https://api.tiingo.com/tiingo/daily/aapl?token=f18511783e1cce638f841728d49fc80cd0749058";
        String url="http://localhost:8088/detail/"+str;
        String url_stock="http://localhost:8088/stock/"+str;
        String url_news="http://localhost:8088/news/"+str;
        //String url_news="";

        updateP();
        nestedscroll.setNestedScrollingEnabled(true);
        recyclerView=findViewById(R.id.newsview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adap= new ArticlesAdapter();
        recyclerView.setAdapter(adap);
        articles= new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        getData(url_news);


        comp = (TextView)findViewById(R.id.company);
        name = (TextView)findViewById(R.id.name);
        TextView change = (TextView)findViewById(R.id.change);
        TextView last = (TextView)findViewById(R.id.last);
        TextView about= (TextView) findViewById(R.id.aboutC);
        TextView moreless= (TextView)findViewById(R.id.moreless);


        RequestQueue requestQueue = Volley.newRequestQueue(company.this);
        JsonObjectRequest request = new JsonObjectRequest( url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            System.out.println("company set");
                        JSONObject jsonObj=response.getJSONObject("data");
                       // System.out.println(jsonObj);
                        comp.setText(jsonObj.getString("ticker"));
                        name.setText(jsonObj.getString("name"));
                        about.setText(jsonObj.getString("description"));
                        TextView tradeheading= (TextView) dialogTrade.findViewById(R.id.tradeheading);
                        tradeheading.setText("Trade " +jsonObj.getString("name") +" shares");

                            flagger=flagger+1;
                            //System.out.println(flagger);
                            changer();
                        }catch (Exception e)
                        {
                            flagger=flagger+1;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Company call "+error);
                    }
                });
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
               System.out.println("retrying");
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                System.out.println("Volley error "+"Volley timeout error for company details");
            }
        });
        requestQueue.add(request);


       // txt_id.setText("data");
        final Boolean[] isCheck = {true};

        moreless.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View view) {
                                          if (isCheck[0]) {
                                              about.setMaxLines(30);
                                              isCheck[0] = false;
                                              moreless.setText("Show less");
                                          } else {
                                              about.setMaxLines(2);
                                              isCheck[0] = true;
                                              moreless.setText("Show more...");
                                          }
                                      }
                                  });

        //for stock
        RequestQueue requestQueue2 = Volley.newRequestQueue(company.this);
        JsonObjectRequest request2 = new JsonObjectRequest( url_stock,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{

                            JSONArray jsonObj=response.getJSONArray("data");
                            JSONObject jo=jsonObj.getJSONObject(0);
                            //  System.out.println(jo);
                            float ll;
                            if(jo.getString("last") == "null")
                            {
                                ll= (float) 0.0;
                            }
                            else {
                                ll=Float.parseFloat(jo.getString("last"));
                            }
                            last.setText("$"+String.format("%.2f",ll));

                            float slast = ll;
                            price=ll;
                            String cp ="Current Price: "+String.format("%.2f",ll);
                            String low,bp,op,mid,high,vol;//

                            System.out.println("price set "+ price);

                            float pClose;
                            if(jo.getString("prevClose") == "null")
                            {
                                pClose= (float) 0.0;
                            }
                            else {
                                pClose=Float.parseFloat(jo.getString("prevClose"));
                            }
                            float diff=slast-pClose;
                            if(diff>0)
                            {
                                int green=ContextCompat.getColor(context, R.color.green);
                                change.setTextColor(green);
                                change.setText("$"+String.format("%.2f",diff));
                            }
                            else if(diff<0)
                            {
                                int red= ContextCompat.getColor(context, R.color.red);
                                change.setTextColor(red);
                                float tdiff=diff*-1;
                                change.setText("-$"+String.format("%.2f",tdiff));
                            }
                            else
                            {
                                change.setTextColor(Color.BLACK);
                                change.setText("$"+String.format("%.2f",diff));
                            }
                            float bid,midval;
                            if(jo.getString("bidPrice")== "null")
                            {
                                bid= (float) 0.0;
                                bp="Bid Price: 0.0";
                            }
                            else {
                                bid = Float.parseFloat(jo.getString("bidPrice"));
                                bp ="Bid Price: "+String.format("%.2f",bid);
                            }
                            if(jo.getString("mid") == "null")
                            {
                                midval= (float) 0.0;
                                mid = "Mid: 0.0";
                            }
                            else {
                                midval = Float.parseFloat(jo.getString("mid"));
                                mid = "Mid: "+String.format("%.2f",midval);//
                            }
                            float open,highh,loww; double vv;
                            if(jo.getString("open") == "null")
                            {
                                open= (float) 0.0;
                                op="Open Price: 0.0";
                            }
                            else {
                                open = Float.parseFloat(jo.getString("open"));
                                op = "Open Price: "+String.format("%.2f",open);//
                            }
                            if(jo.getString("high") == "null")
                            {
                                highh= (float) 0.0;
                                high="High: 0.0";
                            }
                            else {
                                highh = Float.parseFloat(jo.getString("high"));
                                high = "High: "+String.format("%.2f",highh);//
                            }
                            if(jo.getString("volume") == "null")
                            {
                                vv= (float) 0.0;
                                vol="Volume: 0.0";
                            }
                            else {
                                vv = Double.parseDouble(jo.getString("volume"));
                                //System.out.println("vv"+vv);
                                vol ="Volume: "+ String.format("%,.2f", vv);
                            }
                            if(jo.getString("low") == "null")
                            {
                                loww= (float) 0.0;
                                low="Low: 0.0";
                            }
                            else {
                                loww = Float.parseFloat(jo.getString("low"));
                                low="Low: "+String.format("%.2f",loww);
                            }
                          //  System.out.println("here");


                            String[] quotes = {cp,low,bp,op,mid,high,vol};
                            System.out.println("grid set ");
                            gridView.setAdapter(new gridAdapter(context,quotes));
                            flagger=flagger+1;
                            //System.out.println(flagger);
                            changer();
                        }catch (Exception e)
                        {
                            flagger=flagger+1;
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Stock call "+error);
                    }
                });
        request2.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                System.out.println("retrying");
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                System.out.println("Volley error "+"Volley timeout error for company stocks");
            }
        });
        requestQueue2.add(request2);
        //ProgressBar progressBar=(ProgressBar) findViewById(R.id.progressBar2);

        Button trader=(Button) findViewById(R.id.trader);
        trader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView totall= (TextView) dialogTrade.findViewById(R.id.total);
                totall.setText("0 x $"+price+"/share = $0.00");
                available.setText("$"+ String.valueOf(String.format("%.2f",pref.getFloat("netWorth",0f))) +" available to buy "+str);
                dialogTrade.show();
                Button buy=(Button) dialogTrade.findViewById(R.id.buy);
                Button sell=(Button) dialogTrade.findViewById(R.id.sell);
                EditText noshares=(EditText) dialogTrade.findViewById(R.id.noshares);
                noshares.clearFocus();
                noshares.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
                noshares.setText("");
                if(noshares.isFocused())
                {
                    noshares.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_200));
                }
                noshares.addTextChangedListener(new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        noshares.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_200));
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        //noshares.setTextCursorDrawable(ContextCompat.getColorStateList(getApplicationContext(), R.color.purple_200));
                        //noshares.setTextCursorDrawable(R.drawable.cursor);
                         if(s.toString().equals("."))
                        {
                        totall.setText("0 x $"+price+"/share = $0.00");
                        }
                        else if(s.length()!=0)
                        {
                            totall.setText(s+" x $"+price+"/share = $"+ String.format("%.2f",Float.parseFloat(s.toString())*price));
                        }
                        else
                        {
                            totall.setText("0 x $"+price+"/share = $0.00");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                buy.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if((noshares.getText().toString()).length()==0 ||(noshares.getText().toString()).equals("."))
                        {
                            Toast.makeText(company.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                            return;
                        }
                        float shares=Float.parseFloat(noshares.getText().toString());
                        if(Float.parseFloat(noshares.getText().toString())==0) {
                            Toast.makeText(company.this, "Cannot buy less than 0 shares", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String res=pref.getString(str, null);
                       // System.out.println("in buy");
                        float netWorth=pref.getFloat("netWorth", 0);
                        if(pref.contains(str)) { //entry exists
                           // if (res.contains("portfolio")) {
                              //  System.out.println("contains portfolio");
                                int slash=res.indexOf('/')+1;
                                int nextSlash= res.indexOf('/',slash);
                                String substr=res.substring(slash,nextSlash);
                                float currShares=Float.parseFloat(substr);
                                float total=currShares+shares;
                                //updating netWorth
                                float newWorth=netWorth- (shares * price);
                                if(newWorth<0)
                                {
                                   // System.out.println("not enough money to buy");
                                    Toast.makeText(company.this, "Not enough money to buy", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else {
                                    editor.remove(str); // will delete key
                                    editor.commit();

                                    res = res.replace(substr, String.valueOf(total));
                                    editor.putString(str, res); // Storing string
                                    editor.commit();

                                    editor.remove("netWorth");
                                    editor.commit();
                                    editor.putFloat("netWorth", Float.parseFloat(String.format("%.2f", newWorth)));
                                    editor.commit();
                                 //   System.out.println("newworth" + newWorth);
                                }

//                            } else { //entry exists but no shares
//
//                                float newWorth=netWorth- (shares * price);
//                                if(newWorth<0)
//                                {
//                                    Toast.makeText(company.this, "Not enough money to buy", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//                                System.out.println("not portfolio exists");
//                                editor.remove(str); // will delete key email
//                                editor.commit();
//                                res=res + " portfolio /"+ String.valueOf(shares)+"/";
//                                editor.putString(str,res); // Storing string
//                                editor.commit();
//
//                                editor.remove("netWorth");
//                                editor.commit();
//                                editor.putFloat("netWorth",Float.parseFloat(String.format("%.2f",newWorth)));
//                                editor.commit();
//                                System.out.println("newworth"+newWorth);

                          //  }
                        }else { //entry does not exist
                         //   System.out.println("not exist");
                            float newWorth=netWorth- (shares * price);
                            if(newWorth<0)
                            {
                                Toast.makeText(company.this, "Not enough money to buy", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String s=name.getText().toString()   +" @ " + "/"+String.valueOf(shares)+"/";
                            editor.putString(str,s); // Storing string
                            editor.commit();

                            editor.remove("netWorth");
                            editor.commit();
                            editor.putFloat("netWorth",Float.parseFloat(String.format("%.2f",newWorth)));
                            editor.commit();
                          //  System.out.println("newworth"+newWorth);

                            if(pref.contains("portOrder"))
                            {
                                String order=pref.getString("portOrder",null);
                                editor.remove("portOrder");
                                editor.commit();
                                order=order+str+",";
                                editor.putString("portOrder",order); //putting updated favOrder
                                editor.commit();
                            }

                        }
                        updateP();
                        dialogTrade.dismiss();
                        myformat= new DecimalFormat("#.####");
                        if(shares>1) {
                            disMsg.setText("You have successfully bought " + myformat.format(shares) + " shares of " + str);
                        }
                        else
                        {
                            disMsg.setText("You have successfully bought " + myformat.format(shares) + " share of " + str);
                        }
                        congoMsg.show();
                    }

                });
                sell.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if((noshares.getText().toString()).length()==0 ||(noshares.getText().toString()).equals(".") )
                        {
                            Toast.makeText(company.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                            return;
                        }
                        float shares=Float.parseFloat(noshares.getText().toString());
                        if(shares==0) {
                            Toast.makeText(company.this, "Cannot sell less than 0 shares", Toast.LENGTH_LONG).show();
                            return;
                        }
                       // EditText noshares=(EditText) dialogTrade.findViewById(R.id.noshares);
                        String res=pref.getString(str, null);
                        float netWorth=pref.getFloat("netWorth", 0);
                        if(pref.contains(str)) {// entry exists
                           // if (res.contains("portfolio")) {
                               // System.out.println("contains portfolio");
                                int slash=res.indexOf('/')+1;
                                int nextSlash= res.indexOf('/',slash);
                                String substr=res.substring(slash,nextSlash);

                                if(substr.length()!=0) {
                                    float currShares = Float.parseFloat(substr);
                                   // System.out.println("subis" + substr);
                                    float tshares = currShares - shares;
                                   // System.out.println("tshares"+tshares);
                                    if (tshares > 0) { //shares remaining
                                        editor.remove(str); // will delete key
                                        editor.commit();

                                        res = res.replace(substr, String.valueOf(tshares));
                                        editor.putString(str, res); // Storing string
                                        editor.commit();

                                        float newWorth = netWorth + (shares * price);
                                        editor.remove("netWorth");
                                        editor.commit();
                                        editor.putFloat("netWorth",Float.parseFloat(String.format("%.2f",newWorth)));
                                        editor.commit();
                                      //  System.out.println("tshares>0");
                                    } else if (tshares < 0) //display toast alone, no changes
                                    {
                                       // System.out.println("<0");
                                        Toast.makeText(company.this, "Not enough shares to sell", Toast.LENGTH_LONG).show();
                                        return;

                                    } else //sell all existing shares
                                    {
                                        //System.out.println("=0");
                                        editor.remove(str); // will delete key
                                        editor.commit();

                                        if(pref.contains("portOrder"))
                                        {
                                            String order=pref.getString("portOrder",null);
                                            editor.remove("portOrder");
                                            editor.commit();
                                            order=order.replace(str+",",""); //removing current ticker from favOrder
                                            editor.putString("portOrder",order);
                                            editor.commit();
                                        }

                                        float newWorth = netWorth + (shares * price);
                                        editor.remove("netWorth");
                                        editor.commit();
                                        editor.putFloat("netWorth",Float.parseFloat(String.format("%.2f",newWorth)));
                                        editor.commit();
                                       // System.out.println("newworth" + newWorth);
                                    }
                                    updateP();
                                    dialogTrade.dismiss();
                                    myformat= new DecimalFormat("#.####");
                                    if(shares>1) {
                                        disMsg.setText("You have successfully sold " + myformat.format(shares) + " shares of " + str);
                                    }
                                    else
                                    {
                                        disMsg.setText("You have successfully sold " + myformat.format(shares) + " share of " + str);
                                    }
                                    congoMsg.show();
                                }
                                /// temp code
//                                else
//                                {
//                                    editor.remove("netWorth");
//                                    editor.remove(str);
//                                    editor.commit();
//                                }


//                            } else { //entry does not have portfolio and gas favorite
//                                Toast.makeText(company.this, "No shares to sell", Toast.LENGTH_LONG).show();
//
//                            }
                        }else { //entry does not exist
                            //System.out.println("no entry");
                            Toast.makeText(company.this, "Not enough shares to sell", Toast.LENGTH_LONG).show();

                        }

                    }

                });


            }
        });
       // System.out.println("reached here");
//        int c;
//       for(c=1;c<=Integer.MAX_VALUE;c=c+1){
//            if(flagger==3) {
//                nestedscroll.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//                updateP();
//                break;
//            }
//        }
    }

    private void getData(String url_news)
    {
        final String[] firsturl = new String[1];
        RequestQueue requestQueue3 = Volley.newRequestQueue(company.this);
        JsonObjectRequest request3 = new JsonObjectRequest( url_news,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            System.out.println("news set");
                            JSONObject jsonObj=response.getJSONObject("data");
                            JSONArray jar= jsonObj.getJSONArray("articles");
                            //System.out.println(jar);
                            int j; int flag=0;
                            for(j=0;j<jar.length();j++)
                            {
                                if(flag==1)
                                    break;
                                JSONObject curr=jar.getJSONObject(j);
                                JSONObject s=curr.getJSONObject("source");
                                if(curr.getString("url")!=null && curr.getString("urlToImage")!=null && curr.getString("title")!=null && curr.getString("publishedAt")!=null && curr.getString("source")!=null && s.getString("name")!=null)
                                {
                                    ImageView imageView= (ImageView) findViewById(R.id.imageView);

                                    TextView firstsrc=(TextView) findViewById(R.id.firstsrc);
                                    TextView firstpub=(TextView) findViewById(R.id.firstpub);
                                    TextView firsttitle=(TextView) findViewById(R.id.firsttitle);
                                    firsturl[0] =curr.getString("urlToImage");
                                    firstsrc.setText(s.getString("name"));
                                    firsttitle.setText(curr.getString("title"));
                                    Picasso.get().load(curr.getString("urlToImage")).into(imageView);
                                    CardView cv=(CardView) findViewById(R.id.firstnews);
                                    String tempurl=curr.getString("url");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    Date dt = sdf.parse(curr.getString("publishedAt"));
                                    long epoch = dt.getTime();
                                    long timediff=(datemili-epoch)/60000; //number of minutes
                                   // System.out.println("timediff");
                                   // System.out.println(timediff);

                                    String set;
                                    if(timediff < 60) {
                                        set=Long.toString(timediff)+ " minutes ago";
                                        firstpub.setText(set);
                                    }
                                    else if(timediff < 1440) //less than a day
                                    {
                                        timediff=timediff/60; //converting to number of hours
                                        set=Long.toString(timediff)+ " hours ago";
                                        firstpub.setText(set);
                                    }
                                    else if(timediff < 2880) //less than a day
                                    {
                                        timediff=timediff/60; //converting to number of hours
                                        timediff=timediff/24; //number of days
                                        set=Long.toString(timediff)+ " day ago";
                                        firstpub.setText(set);
                                    }
                                    else //more than a day
                                    {
                                        timediff=timediff/60; //converting to number of hours
                                        timediff=timediff/24;
                                        set=Long.toString(timediff)+ " days ago";
                                        firstpub.setText(set);
                                    }
                                    cv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempurl));
                                           // System.out.println(tempurl);
                                            startActivity(browserIntent);
                                        }
                                    });
                                    cv.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            final Dialog dialog = new Dialog(context);
                                            dialog.setContentView(R.layout.sharer);
                                            ImageView dimg= (ImageView) dialog.findViewById(R.id.dialogimg);
                                            Picasso.get().load(firsturl[0]).into(dimg);
                                            TextView dtitle= (TextView) dialog.findViewById(R.id.dialogtitle);
                                            ImageButton itwitter= (ImageButton) dialog.findViewById(R.id.twitter);
                                            ImageButton ichrome= (ImageButton) dialog.findViewById(R.id.chrome);

                                                   dtitle.setText(firsttitle.getText());
                                                    itwitter.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            try {
                                                                String query = URLEncoder.encode(tempurl, "utf-8");
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
                                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tempurl));
                                                           // System.out.println(tempurl);
                                                            startActivity(browserIntent);
                                                        }
                                                    });


                                           // System.out.println("in first long click");
                                            dialog.show();
                                            return true;
                                        }
                                    });
                                    flag=1;
                                }
                            }
                            for(int i=j;i<jar.length();i++)
                            {
                                JSONObject curr=jar.getJSONObject(i);
                                //System.out.println(curr);
//                                System.out.println(curr.getString("urlToImage"));
//                                System.out.println(curr.getString("title"));
//                                System.out.println(curr.getString("publishedAt"));
                                Article article=new Article();
                                article.setImage(curr.getString("urlToImage"));
                                article.setImgLink(curr.getString("urlToImage"));
                                article.setTitle(curr.getString("title"));
                                JSONObject s=curr.getJSONObject("source");
                                article.setSource(s.getString("name"));
                                article.setLink(curr.getString("url"));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                Date dt = sdf.parse(curr.getString("publishedAt"));
                                long epoch = dt.getTime();
                                long timediff=(datemili-epoch)/60000; //number of minutes
                               // System.out.println("timediff");
                                //System.out.println(timediff);

                                String set;
                                if(timediff < 60) {
                                    set=Long.toString(timediff)+ " minutes ago";
                                    article.setAgo(set);
                                }
                                else if(timediff < 1440) //less than a day
                                {
                                    timediff=timediff/60; //converting to number of hours
                                    set=Long.toString(timediff)+ " hours ago";
                                    article.setAgo(set);

                                }
                                else if(timediff < 2880) //less than a day
                                {
                                    timediff=timediff/60; //converting to number of hours
                                    timediff=timediff/24; //number of days
                                    set=Long.toString(timediff)+ " day ago";
                                   article.setAgo(set);
                                }
                                else //more than a day
                                {
                                    timediff=timediff/60; //converting to number of hours
                                    timediff=timediff/24;
                                    set=Long.toString(timediff)+ " days ago";
                                    article.setAgo(set);
                                }
                                articles.add(article);
                               // System.out.println(s.getString("name"));
                                //System.out.println(article);
                            }
                            flagger=flagger+1;
                          //  System.out.println(flagger);
                            changer();
                           // nestedscroll.setVisibility(View.VISIBLE);
                           // progressBar.setVisibility(View.GONE);

                        }catch (Exception e)
                        {
                            flagger=flagger+1;
                            e.printStackTrace();
                            System.out.println(e);
                            changer();
                        }
                        adap.setData(articles);
                        adap.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("News call "+error);
                        flagger=flagger+1;
                    }
                });
        request3.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                System.out.println("retrying");
                return 5000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                System.out.println("Volley error "+"Volley timeout error for company news");
            }
        });
        requestQueue3.add(request3);


    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    private void changer()
    {
        //System.out.println("in changer"+flagger);
        if(flagger==3) {
            TextView about= (TextView) findViewById(R.id.aboutC);
            TextView moreless= (TextView)findViewById(R.id.moreless);

            wv.loadUrl("file:///android_asset/hcharts.html?message="+str);
            nestedscroll.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            fetch.setVisibility(View.GONE);
            about.post(new Runnable() {
                @Override
                public void run() {
                    if(about.getLineCount() <2)
                    {
                      //  System.out.println("about"+about.getLineCount());
                        moreless.setVisibility(View.GONE);
                        about.setGravity(Gravity.CENTER);
                    }
                    else
                    {
                        moreless.setVisibility(View.VISIBLE);
                        about.setMaxLines(2);

                    }
                }
            });

            updateP();

        }
    }
    public void updateP()
    {
        TextView p1=(TextView) findViewById(R.id.p1);
        TextView p2=(TextView) findViewById(R.id.p2);
        if(pref.contains(str))
        {
            String pp=pref.getString(str,null);
//            if(pp.contains("portfolio"))
//            {
                int slash=pp.indexOf('/')+1;
                int nextSlash= pp.indexOf('/',slash);
                String substr=pp.substring(slash,nextSlash);
                float totalVal = Float.parseFloat(substr);
                p1.setText("Shares owned: "+ String.format("%.4f", totalVal));
              //  System.out.println("here "+price);
                totalVal = totalVal * price;
                p2.setText("Market Value: $" +String.format("%.2f", totalVal));

//            }
//            else //no portfolio but is fav
//            {
//                p1.setText("You have 0 shares for "+ str+".");
//                p2.setText("Start Trading!");
//            }
        }
        else //no entry
        {

            p1.setText("You have 0 shares for "+ str+".");
            p2.setText("Start Trading!");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.my_menu2, menu);
        this.menu=menu;
        if(preffav.contains(str))
        {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }
        else
        {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.not_favorite) { // if not set then set
            menu.getItem(1).setVisible(true);
            menu.getItem(0).setVisible(false);
            String S = name.getText().toString() +" @" + " favorite";
            editor2.putString(str,S);
            editor2.commit();

            if(preffav.contains("favOrder"))
            {
            String order=preffav.getString("favOrder",null);
                editor2.remove("favOrder");
                editor2.commit();
            order=order+str+",";
            editor2.putString("favOrder",order); //putting updated favOrder
            editor2.commit();
            }

            Toast.makeText(this, "\""+str+"\""+" was added to favorites",
                    Toast.LENGTH_LONG).show();
            return true;
        }else if (id == R.id.action_favorite) { //if filled
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            editor2.remove(str);
            editor2.commit();

            Toast.makeText(this, "\""+str+"\""+" was removed from favorites",
                    Toast.LENGTH_LONG).show();

            if(preffav.contains("favOrder"))
            {
                String order=preffav.getString("favOrder",null);
                editor2.remove("favOrder");
                editor2.commit();
                order=order.replace(str+",",""); //removing current ticker from favOrder
                editor2.putString("favOrder",order);
                editor2.commit();
            }
            return true;
        }
        if(id== android.R.id.home)
        {
            onBackPressed();
            return true;

        }
        return super.onOptionsItemSelected(item);

        }

    }

