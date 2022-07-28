package com.cst2335.duon0065;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;

public class WeatherForecast extends AppCompatActivity {

    TextView currTemp;
    TextView minimumTemp;
    TextView maximumTemp;
    TextView uv;
    ProgressBar progressBar;
    ImageView weatherPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        weatherPic = findViewById(R.id.imageView);
        currTemp = findViewById(R.id.textCurrentTemp);
        minimumTemp = findViewById(R.id.textMinTemp);
        maximumTemp = findViewById(R.id.textMaxTemp);
        uv = findViewById(R.id.textUVRating);

        ForecastQuery fq = new ForecastQuery();
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
                "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        String UV;
        String minTemp;
        String maxTemp;
        String currentTemp;
        String iconName;

        @Override
        public String doInBackground(String... args) {

            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                        }

                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                URL uvURL = new URL(args[1]);
                HttpURLConnection uvConnection = (HttpURLConnection) uvURL.openConnection();
                InputStream uvResponse = uvConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject uvReport = new JSONObject(result);

                float uvRating = (float) uvReport.getDouble("value");

                UV = Float.toString(uvRating);


                String imgURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                Bitmap image = null;
                URL img_Url = new URL(imgURL);
                HttpURLConnection connection = (HttpURLConnection) img_Url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    if (fileExistence(iconName + ".png")) {
                        FileInputStream fis = null;

                        try { fis = openFileInput(iconName + ".png"); }
                        catch (FileNotFoundException e) {e.printStackTrace(); }

                        Bitmap bm = BitmapFactory.decodeStream(fis);

                        Log.e("Found", "Found image: " + iconName);
                        weatherPic.setImageBitmap(bm);

                    } else {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        Log.e("Not found", "Not found, downloading: " + iconName);
                        FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        weatherPic.setImageBitmap(image);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(100);
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        @Override
        protected void onPostExecute(String fromDoInBackground) {
            Log.e("HTTP", fromDoInBackground);
            currTemp.setText(currentTemp);
            minimumTemp.setText(minTemp);
            maximumTemp.setText(maxTemp);
            uv.setText(UV);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean fileExistence(String fName) {
        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();
    }

}