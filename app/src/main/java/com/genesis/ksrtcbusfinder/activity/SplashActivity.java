package com.genesis.ksrtcbusfinder.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.genesis.ksrtcbusfinder.R;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar=findViewById(R.id.progressBar);
        Splash splash=new Splash();
        splash.execute();

    }
    class Splash extends AsyncTask<Void,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int i=0;
            while(i<=100){
                publishProgress(i);
                i++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

        }
    }
}

