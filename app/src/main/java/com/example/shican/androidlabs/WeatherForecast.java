package com.example.shican.androidlabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends Activity {
    private ProgressBar progressBar;
    private TextView minTemp, maxTemp, currentTemp, wind;
    private ImageView weatherImage;
    private Bitmap image;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        currentTemp = (TextView)findViewById(R.id.currentTemp);
        minTemp = (TextView)findViewById(R.id.minTemp);
        maxTemp = (TextView)findViewById(R.id.maxTemp);
        wind = (TextView)findViewById(R.id.windSpeed);
        weatherImage = (ImageView)findViewById(R.id.weatherImage);
        WeatherQuery run = new WeatherQuery();
        run.execute();
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public class WeatherQuery extends AsyncTask<String[], Integer, String[]> {
        HttpURLConnection conn;

        protected String[] doInBackground(String[]... strings) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), "utf-8");
                int eventType = parser.getEventType();
                String tagName = "";
                String[] result = new String[5];
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    parser.next();
                    tagName = parser.getName();
                    if (tagName == null) {
                        tagName = "";
                    }
                    eventType = parser.getEventType();
                    if (eventType == START_TAG) {
                        if (tagName.equalsIgnoreCase("temperature")) {
                            result[0] = parser.getAttributeValue(null, "value");
                            onProgressUpdate(25);
                            result[1] = parser.getAttributeValue(null, "min");
                            onProgressUpdate(50);
                            result[2] = parser.getAttributeValue(null, "max");
                            onProgressUpdate(75);
                        } else if (tagName.equalsIgnoreCase("weather")) {
                            result[3] = parser.getAttributeValue(null, "icon");
                            onProgressUpdate(100);
                        } else if (tagName.equalsIgnoreCase("speed")){
                            result[4]= parser.getAttributeValue(null, "value");
                        }
                    }
                }
                conn.disconnect();
                if (fileExistance(result[3] + ".png")) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(result[3] + ".png");
                        image = BitmapFactory.decodeStream(fis);
                        Log.i(result[3], "Weather image was found locally");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        URL urlImage = new URL("http://openweathermap.org/img/w/" + result[3] + ".png");
                        image = HttpUtils.getImage(urlImage);
                        outputStream = openFileOutput(result[3] + ".png", MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Log.i(result[3], "Weather image was downloaded");
                    } catch (Exception e) {
                    }
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
        }

        protected void onPostExecute(String[] result) {
            currentTemp.setText("Current: "+result[0]+"°C");
            minTemp.setText("      Min: "+result[1]+"°C");
            maxTemp.setText("     Max: "+result[2]+"°C");
            wind.setText("    Wind: "+result[4]+"km/h");
            weatherImage.setImageBitmap(image);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}


