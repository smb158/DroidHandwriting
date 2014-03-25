package com.example.droidhandwriting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
//public final static String EXTRA_MESSAGE = "com.example.droidhandwriting.MESSAGE";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMessage(View view){
		Intent intent = new Intent(this, Settings.class);
		startActivityForResult(intent, 0);
	}
	
	public void recognize(View view){
		//HttpClient, HttpPost, and UrlEncodedFormEntity
		//ink data q=[30, 121, 82, 121, 121, 121, 255, 0, 121, 21, 120, 85, 120, 1NameValuePair
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key","11773edfd643f813c18d82f56a8104ed"));
		
		Custom_View theCanvas = (Custom_View) findViewById(R.id.Custom_View);
		
		String convertedPoints = theCanvas.getPoints();
		System.out.println(convertedPoints);
		//String convertedPoints = "[30, 121, 82, 121, 121, 121, 255, 0, 121, 21, 120, 85, 120, 185, 255, 255]";
		
		nameValuePairs.add(new BasicNameValuePair("q",convertedPoints));
		
		try {
	        TextView output = (TextView) findViewById(R.id.textView1);
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://cwritepad.appspot.com/reco/usen");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        System.out.println(response.getEntity().getContentLength());
	        //InputStream temp = entity.getContent();
	        
	        StringBuilder sb = new StringBuilder();
	        try {
	            BufferedReader reader = 
	                   new BufferedReader(new InputStreamReader(entity.getContent()), 65728);
	            String line = null;

	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	        }
	        catch (IOException e) { e.printStackTrace(); }
	        catch (Exception e) { e.printStackTrace(); }


	        System.out.println("finalResult " + sb.toString());
	        String final_output;
	        //convert(sb.toString().substring(0, 2), final_output, "GB2312", "UTF8")
	        output.setText(sb.toString());  
	        
	    } 
		catch (Exception e) {
	        //TextView output = (TextView) findViewById(R.id.textView1);
	        //output.setText(e.getMessage());
	        e.printStackTrace();
	    }
		
		//Get data back from server
		//new String(buf, 0, len, "GB2312");
		//new String(buf, 0, len, "utf-8");
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	      if (resultCode == Activity.RESULT_OK) 
	      {
	    	  String[] settingsArray = data.getStringArrayExtra("MESSAGE");
	    	  
	    	  Custom_View drawingCanvas = (Custom_View) findViewById(R.id.Custom_View);
	    	  if(settingsArray[0].equals("True")){
	    		  TextView output = (TextView) findViewById(R.id.textView1);
	    		  output.setText("Output");
		    	  drawingCanvas.resetCanvas();
	    	  }
	    	  drawingCanvas.setPaintColor(settingsArray[1]);
	    	  
	    	  //System.out.println(settingsArray[0]+" "+settingsArray[1]);
	      } 
	}

}
