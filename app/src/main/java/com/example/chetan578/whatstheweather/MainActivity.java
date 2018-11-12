package com.example.chetan578.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weatherReport;
    Button button;

    public void weatherCheck(View view)
    {

        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid=fb4067c6592f7dc22de04fec9db5fec4");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;

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

            } catch(IOException e )
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find weather of this city", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("information",result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject others=jsonObject.getJSONObject("main");
                int temp=others.getInt("temp");
                int farenheit = (int) (temp-273.15);
                String humidity=others.getString("humidity");
                String message="";
                String weatherInfo=jsonObject.getString("weather");
                Log.i("weather",weatherInfo);
                JSONArray array =new JSONArray(weatherInfo);
                JSONObject part=array.getJSONObject(0);
                String mainWeather=part.getString("main");
                String description =part.getString("description");
                if((!mainWeather.equals("")) && (!description.equals("")))
                {
                    message += mainWeather +" : "+description + "\r\n" + "Humidity :"+humidity+ "%"+ "\r\n"+"temperature: " +farenheit +"deg C"+"\r\n";
                }
                if((!message.equals("")))
                {
                    weatherReport.setText(message);
                }
                else{
                    Toast.makeText(getApplicationContext(), "could not find weather of this city", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find weather of this city", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=findViewById(R.id.cityName);
        weatherReport=findViewById(R.id.weatherReport);
        button=findViewById(R.id.button);
        Log.i("city",cityName.getText().toString());

    }
}
