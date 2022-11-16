package com.example.testnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;





public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchBtn = findViewById(R.id.searchOracleBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // 模拟器中本地ip为 10.0.2.2
                String httpURL = "http://10.0.2.2:8080/sendname";
                TextView httpContent = findViewById(R.id.resultbox);
                httpContent.setText("Searching...");
                try {
                    URL url = new URL(httpURL);
                    QueryTask task = new QueryTask();
                    task.execute(url);
                } catch (MalformedURLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    class QueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            System.out.println(urls.length);
            if (urls.length == 0)
                return null;
            try {
                HttpURLConnection urlConn = (HttpURLConnection) urls[0].openConnection();
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                urlConn.setConnectTimeout(10000);
                urlConn.setReadTimeout(10000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String res = "", line = null;
                while ((line =  reader.readLine()) != null) {
                    res += line + '\n';
                }
                reader.close();
                urlConn.disconnect();
                return res;
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // here, 解析result, 需要导入libs
            try {
                JSONObject jsonObject=new JSONObject(result);
                String sentence=jsonObject.optString("res");
                TextView httpContent = findViewById(R.id.resultbox);
                if (result != null) {
                    httpContent.setText(sentence);
                } else {
                    httpContent.setText("no result");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

