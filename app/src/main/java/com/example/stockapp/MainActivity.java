package com.example.stockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayoutMediator;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.graphics.Color;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.ArrayAdapter;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    Activity context = this;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SharedPreferences preffav;
    SharedPreferences.Editor editor2;
    private RequestQueue reqQueue;
    public List<String> suggestions;
    public List<String> temp;
    public ArrayAdapter<String> arrayAdapter;
    float totalWorth;
    int first=0;
    NestedScrollView mainNested;
    ProgressBar progressBar;
    TextView fetch1;
    float isshare;
    ArrayList<Stock_card> stockCards;
    TextView Ttitle;
    List<Stock_card> sectionOneItems ;
    List<Stock_card> sectionTwoItems ;
    String finalAllkeys1,finalAllkeys2;
    String allkeys,allkeys2;
    String sendTicker;

    List<section> sectionList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.Theme_StockApp);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        fetch1=(TextView) findViewById(R.id.fetch1);
        mainNested= (NestedScrollView) findViewById(R.id.mainNested);
        mainNested.setVisibility(View.INVISIBLE);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        fetch1.setVisibility(View.VISIBLE);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        preffav = getApplicationContext().getSharedPreferences("MyPreffav", 0); // 0 - for private mode
        editor2 = preffav.edit();
        if(!pref.contains("netWorth"))
        {
            editor.putFloat("netWorth",20000.00f); // Storing string
            editor.commit();
        }
       // System.out.println(pref.getFloat("netWorth", 0));
        progressBar.setVisibility(View.VISIBLE);
        dateTimeDisplay = findViewById(R.id.currDate);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);
        suggestions = new ArrayList<>();
        temp= new ArrayList<>();
        Ttitle=(TextView) findViewById(R.id.Ttitle);
        NestedScrollView nestedscroll= (NestedScrollView)findViewById(R.id.mainNested);
        nestedscroll.setNestedScrollingEnabled(true);
        sectionOneItems = new ArrayList<>();
        sectionTwoItems = new ArrayList<>();
        initData();
        TextView footer= (TextView) findViewById(R.id.footer);
        footer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiingo.com/"));
                startActivity(browserIntent);
            }
        });
        //Start
       handler.postDelayed(runnable, 15000);

    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initData();
            System.out.println("15 seconds : refreshed");
            handler.postDelayed(this, 15000);
        }
    };

    @Override
    protected void onPause()
    {
        super.onPause();
        handler.removeCallbacks(runnable);
        System.out.println("paused");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainNested.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        fetch1.setVisibility(View.VISIBLE);
        initData();
        handler.postDelayed(runnable, 15000);


    }

    private void initData(){

        Map<String, Stock_card> Pmap=new HashMap<>();
        Map<String, Stock_card> Fmap=new HashMap<>();
        Map<String, ?> prefsMap = pref.getAll();
        Map<String, ?> prefsMap2=preffav.getAll();
        allkeys="";
        allkeys2="";
        for (String key : prefsMap.keySet()) {
           // if(key!="portOrder" && key!="netWorth") {
               // System.out.println(key.trim());
                allkeys = allkeys + key.trim() + ",";
          //  }

        }
        for (String key : prefsMap2.keySet()) {

           // if(key!="favOrder") {
             //   System.out.println(key.trim());
                allkeys2 = allkeys2 + key.trim() + ",";
           // }

        }
        allkeys=allkeys.replace("netWorth,","");
        allkeys=allkeys.replace("portOrder,","");
        allkeys2=allkeys2.replace("favOrder,","");
       // System.out.println("allkeys"+allkeys);
        //System.out.println("allkeys2"+allkeys2);
        if(!pref.contains("portOrder")) {
            editor.putString("portOrder", allkeys);
            editor.commit();
             finalAllkeys1 = allkeys;
             System.out.println("no portorder");

        }
        else
        {
            finalAllkeys1=pref.getString("portOrder",null);
            //System.out.println("yes portorder");
        }
        if(!preffav.contains("favOrder")) {
            editor2.putString("favOrder", allkeys2);
            editor2.commit();
             finalAllkeys2 = allkeys2;
            //System.out.println("no favorder");
        }
        else
        {
            finalAllkeys2=preffav.getString("favOrder",null);
            //System.out.println("yes favorder");
        }
        allkeys=allkeys+allkeys2;
        if(allkeys.length()==0)
        {
            totalWorth=pref.getFloat("netWorth",0f);
        }
        else {
            allkeys = allkeys.substring(0, allkeys.length() - 1);
        }
        System.out.println(allkeys);
        String sectionOneName="PORTFOLIO";
        String sectionTwoName="FAVORITES";
        String url_stock="localhost:8088/stock/"+allkeys;
        System.out.println(url_stock);
        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest request2 = new JsonObjectRequest( url_stock,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            sectionOneItems = new ArrayList<>();
                            sectionTwoItems = new ArrayList<>();
                            totalWorth=0;
                                //System.out.println("got inside");
                                JSONArray jsonObj = response.getJSONArray("data");
                                for (int i = 0; i < jsonObj.length(); i++) {
                                    JSONObject jo = jsonObj.getJSONObject(i);
                                    // System.out.println(jo);
                                    String currKey = jo.getString("ticker");
                                    float ll, prevClose;
                                    if (jo.getString("last") == "null") {
                                        ll = (float) 0.0;
                                    } else {
                                        ll = Float.parseFloat(jo.getString("last"));
                                    }
                                    if (jo.getString("prevClose") == "null") {
                                        prevClose = (float) 0.0;
                                    } else {
                                        prevClose = Float.parseFloat(jo.getString("prevClose"));
                                    }
                                    float diff = ll - prevClose;
                                    if (preffav.contains(currKey) && pref.contains(currKey)) {
                                      //  System.out.println("1");
                                        String currRes = pref.getString(currKey, null);
                                        int slash = currRes.indexOf('/') + 1;
                                        int nextSlash = currRes.indexOf('/', slash);
                                        String substr = currRes.substring(slash, nextSlash);
                                        totalWorth = totalWorth + (Float.parseFloat(substr) * ll);
                                        isshare=Float.parseFloat(substr);
                                        System.out.println(isshare+currKey);
                                        if(isshare<=1.0) {
                                            Fmap.put(currKey, new Stock_card(currKey, String.format("%.2f", ll), String.format("%.2f", isshare) + " share", String.valueOf(String.format("%.2f", diff)), "0"));
                                            Pmap.put(currKey, new Stock_card(currKey, String.format("%.2f", ll), String.format("%.2f", isshare) + " share", String.valueOf(String.format("%.2f", diff)), "1"));
                                        }//sectionOneItems.add(new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), substr + " shares", String.valueOf(String.format("%.2f", diff))));
                                        //sectionTwoItems.add(new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), substr + " shares", String.valueOf(String.format("%.2f", diff))));
                                        else{
                                            Fmap.put(currKey, new Stock_card(currKey, String.format("%.2f", ll), String.format("%.2f", isshare) + " shares", String.valueOf(String.format("%.2f", diff)), "0"));
                                            Pmap.put(currKey, new Stock_card(currKey, String.format("%.2f", ll), String.format("%.2f", isshare) + " shares", String.valueOf(String.format("%.2f", diff)), "1"));
                                        }
                                    } else if (preffav.contains(currKey) && !pref.contains(currKey))  {
                                       // System.out.println("2");
                                        String currRes = preffav.getString(currKey, null);
                                        String[] arrOfStr = currRes.split("@");
                                        Fmap.put(currKey,new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), arrOfStr[0], String.valueOf(String.format("%.2f", diff)),"0"));
                                        //sectionTwoItems.add(new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), arrOfStr[0], String.valueOf(String.format("%.2f", diff))));
                                    } else if (!preffav.contains(currKey) && pref.contains(currKey)) {
                                       // System.out.println("3");
                                        String currRes = pref.getString(currKey, null);
                                        int slash = currRes.indexOf('/') + 1;
                                        int nextSlash = currRes.indexOf('/', slash);
                                        String substr = currRes.substring(slash, nextSlash);
                                        isshare=Float.parseFloat(substr);
                                        totalWorth = totalWorth + (Float.parseFloat(substr) * ll);
                                        if(isshare<=1) {
                                            Pmap.put(currKey, new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), String.format("%.2f", isshare) + " share", String.valueOf(String.format("%.2f", diff)), "1"));
                                        }
                                        else
                                        {
                                            Pmap.put(currKey, new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), String.format("%.2f", isshare) + " shares", String.valueOf(String.format("%.2f", diff)), "1"));
                                        }
                                       // sectionOneItems.add(new Stock_card(currKey, String.valueOf(String.format("%.2f", ll)), substr + " shares", String.valueOf(String.format("%.2f", diff))));


                                    }
                                }
                                String arr1[]= finalAllkeys1.split(",");
                                String arr2[]= finalAllkeys2.split(",");

                                for(String c1: arr1)
                                {
                                    if(Pmap.containsKey(c1)) {
                                        sectionOneItems.add(Pmap.get(c1));
                                    }
                                }
                            for(String c1: arr2)
                            {
                                if(Fmap.containsKey(c1)) {
                                    sectionTwoItems.add(Fmap.get(c1));
                                }
                            }

//                            System.out.println("p");
//                            System.out.println(sectionOneItems);
//                            System.out.println("f");
//                            System.out.println(sectionTwoItems);

                            totalWorth=totalWorth+ pref.getFloat("netWorth",0.0f);
                            sectionList.clear();
                            sectionList.add((new section(sectionOneName,"Net Worth",String.valueOf(String.format("%.2f",totalWorth)),sectionOneItems)));
                            sectionList.add((new section(sectionTwoName,"","",sectionTwoItems)));

                            RecyclerView mainRecyclerView;
                            mainRecyclerView = findViewById(R.id.mainRecyclerView);
                            mainRecyclerAdap mainRecyclerAdapter = new mainRecyclerAdap(sectionList);
                            mainRecyclerView.setAdapter(mainRecyclerAdapter);
                            mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            ViewCompat.setNestedScrollingEnabled(mainRecyclerView,false);
                            mainNested.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            fetch1.setVisibility(View.GONE);


                        }catch (Exception e)
                        {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Idhar bhi error "+error);
                        if(allkeys.length()==0) {
                            sectionOneItems = new ArrayList<>();
                            sectionTwoItems = new ArrayList<>();
                            totalWorth = 0;
                            totalWorth = totalWorth + pref.getFloat("netWorth", 0.0f);
                            sectionList.clear();
                            sectionList.add((new section(sectionOneName, "Net Worth", String.valueOf(String.format("%.2f", totalWorth)), sectionOneItems)));
                            sectionList.add((new section(sectionTwoName, "", "", sectionTwoItems)));

                            RecyclerView mainRecyclerView;
                            mainRecyclerView = findViewById(R.id.mainRecyclerView);
                            mainRecyclerAdap mainRecyclerAdapter = new mainRecyclerAdap(sectionList);
                            mainRecyclerView.setAdapter(mainRecyclerAdapter);
                            mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            ViewCompat.setNestedScrollingEnabled(mainRecyclerView, false);


                            mainNested.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            fetch1.setVisibility(View.GONE);
                        }
                    }
                });
        request2.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue2.add(request2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(true);
        // Get SearchView autocomplete object.
        searchView.setQueryHint("");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownHeight(1100);
        searchAutoComplete.setDropDownBackgroundResource(R.color.detail);

        //arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        //searchAutoComplete.setAdapter(arrayAdapter);
        // Create a new ArrayAdapter and add data to search auto complete object.
        //String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
       // ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);



        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                String[] splitted = queryString.split("\\s+");
                sendTicker=splitted[0];
               // Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();

               // MainActivity.this.startActivityForResult(intent,1);
               // overridePendingTransition(0, 0);
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                 Intent intent= new Intent(MainActivity.this, company.class);
                 intent.putExtra("msg",sendTicker);
                 startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()<3){
                    // list.clear();
                    suggestions.clear();
                    return false;
                }
                //suggestions.clear();
                reqQueue = Volley.newRequestQueue(context);
               // String url="http://localhost:8088/auto/"+newText;
                //String url="https://api.tiingo.com/tiingo/utilities/search?query="+newText+"&token=f18511783e1cce638f841728d49fc80cd0749058";
                String url="https://localhost:8088/auto/"+newText;
                System.out.println(url);

                JsonObjectRequest jsonArrayRequest= new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    temp.clear();
                                //System.out.println(response);
                                JSONArray jsonObject1=response.getJSONArray("data");
                                //System.out.println(jsonObject1);

                                int length=jsonObject1.length();
                                for(int i=0;i<length;i++)
                                    {
                                        JSONObject jo=jsonObject1.getJSONObject(i);
                                        //System.out.println(jo);
//                                        String ticker=jo.getString("ticker");
//                                        String name=jo.getString("name");
                                       // System.out.println(jo.getString("ticker"));
                                       // String combine=ticker + " - " +name;
                                        temp.add(i, jo.getString("ticker")+ " - " +jo.getString("name")) ;
                                    }
                                    //System.out.println(suggestions);
                                    suggestions=temp;
                                    arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_dropdown_item_1line, suggestions);
                                    searchAutoComplete.setAdapter(arrayAdapter);

                                    arrayAdapter.notifyDataSetChanged();

                            }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                System.out.println("Error"+error);


                            }
                        });
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        8000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                reqQueue.add(jsonArrayRequest);

              //  Toast.makeText(MainActivity.this, "you typed " + newText, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //searchAutoComplete.addTextChangedListener();
        
       // System.out.println("here it ends");

        return super.onCreateOptionsMenu(menu);


    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//         super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode == 1) {
//
//            if(resultCode == RESULT_OK){
//                System.out.println("result ok");
//            }
//            if (resultCode == RESULT_CANCELED) {
//                System.out.println("result cancelled");
//            }
//        }
   // }//onActivityResult



}