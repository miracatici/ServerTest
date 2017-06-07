package com.example.servertest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class FreshService extends Service {

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		checkServer();
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private void checkServer(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				Socket client = null;				// Connection socket
				BufferedReader input = null;	// Input for client side, message comes from server side 
				String line = "";
				try {
					client = new Socket("178.62.193.160",8890);
					input = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
					while (true){
						line = input.readLine();
						NotificationCompat.Builder mBuilder =
								new NotificationCompat.Builder(FreshService.this)
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentTitle("line")
								.setContentText("A message comes from server: " + line)
								.setDefaults(Notification.DEFAULT_SOUND);
						NotificationManager mNotificationManager =
							    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
							// mId allows you to update the notification later on.
							mNotificationManager.notify(1,mBuilder.build());
						if(line.equals("bye")){
							break;
						}
					}
					client.close();
					input.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}						
			}
		}).start();
	}
}
