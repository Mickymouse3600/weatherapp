package co.example.mickymouse.whatstheweather;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText Cname;
    TextView Rname;

    public void FindWeather (View view) {

        String EncodeCity= null;
        String City= Cname.getText().toString();

        Log.i("City Name", City);

        InputMethodManager mgr= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(Cname.getWindowToken(),0);

        try {
            EncodeCity = URLEncoder.encode(City , "UTF-8");

            if (EncodeCity==""){


                Toast.makeText(getApplicationContext(),"Please Enter City Name",Toast.LENGTH_LONG);
                }else {

                DownloadTask task = new DownloadTask();
                task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + EncodeCity + "&appid=815b7f25bce72c4a8e0bb05be3901973");
            }
        } catch (UnsupportedEncodingException e) {


            e.printStackTrace();



        }






    }



    public class DownloadTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();


                 }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String Message="";

                JSONObject jsonObject = new JSONObject(result);

                String weatherinfo = jsonObject.getString("weather");

                Log.i("Website data", weatherinfo);

                JSONArray arr = new JSONArray(weatherinfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String Main="";
                    String Description="";

                     Main=jsonPart.getString("main");
                     Description=jsonPart.getString("description");

                     if (Main!="" && Description!="" ) {

                         Message+= Main + " : " + Description +"\r\n";



                     }

                }
                    if (Message!=""){

                    Rname.setText(Message);

                    Message="";


                    } else {

                        Toast.makeText(getApplicationContext(),"Couldn't Find the Weather",Toast.LENGTH_LONG);

                    }



            } catch (JSONException e)
            {
                e.printStackTrace();
            }


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cname= (EditText) findViewById(R.id.Cname);
        Rname= (TextView)findViewById(R.id.Rname);

    }
}