package com.example.servertest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	PrintWriter output = null;
	SeekBar speedBar; Button btnConnect, btnStop; 
	TextView value;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		value = (TextView) findViewById(R.id.value);
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connectServer();
			}
		});
		btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setEnabled(false);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				output.println("s");
			}
		});
		speedBar = (SeekBar) findViewById(R.id.speedBar);
		speedBar.setMax(510); speedBar.setProgress(255);
		speedBar.setEnabled(false);
		speedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				progress -= 255;
				if(progress>0){
					output.println("f"+"\t"+Math.abs(progress));
				} else {
					output.println("b"+"\t"+Math.abs(progress));
				}
				value.setText(String.valueOf(progress));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void connectServer(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				Socket client = null;				// Connection socket
				try {
					client = new Socket("192.168.0.1",8890);
					output = new PrintWriter(client.getOutputStream(),true);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}								
			}
		}).start();	
		btnConnect.setEnabled(false);
		btnStop.setEnabled(true);
		speedBar.setEnabled(true);
	}
}
