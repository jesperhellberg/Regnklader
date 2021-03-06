package com.jesperleker.regnklader;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jesperleker.regnklader.stab.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    public TextView textview;
    private final String URL = "http://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/13.464021/lat/59.386888/data.json";
    private SwipeRefreshLayout swipe;
    JSONParse jsonParse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new myFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tablist);
        slidingTabLayout.setViewPager(viewPager);

        textview = (TextView) findViewById(R.id.today_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeArea);
        swipe.setColorSchemeResources(R.color.refresh_progress_1);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                testText();             //Kanske inte så snyggt...
            }
        });

        new JSONParse().execute();
//        jsonParse = new JSONParse();
//        jsonParse.execute();

        refreshUpdate();
    }

    private void refreshUpdate() {
        new JSONParse().execute();
    }

    public void testText(){
        refreshUpdate();
        swipe.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return new JSONParser().getJSONfromURL(URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
//            super.onPostExecute(jsonObject);

            if (jsonObject != null) {
                getForecast(jsonObject);
            }
        }
    }

    private void getForecast(JSONObject jsonObject) {

        try{
            JSONArray arr = jsonObject.getJSONArray("timeSeries");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            Date now = new Date();

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(now);
//            cal.set(Calendar.HOUR_OF_DAY, 17);
//            cal.set(Calendar.MINUTE, 59);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date tomorrow = cal.getTime();

            Double totalRain = 0d;

            for (int i = 0; i < arr.length(); i++) {
                Date jsonDate = sdf.parse(arr.getJSONObject(i).getString("validTime"));

                if(now.before(jsonDate) && tomorrow.after(jsonDate)){
                    JSONArray parameters =  arr.getJSONObject(i).getJSONArray("parameters");
                    for(int j = 0; j < parameters.length(); j++){
                        if(parameters.getJSONObject(j).getString("name").equals("pmax")){
                           totalRain += parameters.getJSONObject(j).getJSONArray("values").getDouble(0);
                        }
                    }
                }
            }
            textview = (TextView) findViewById(R.id.today_text);

            if (totalRain > 0) textview.setText("Ta med regnkläder, det kommer att regna " + String.format("%.2f", totalRain) + " mm idag.");
            else textview.setText("Idag behövs inga regnkläder");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
